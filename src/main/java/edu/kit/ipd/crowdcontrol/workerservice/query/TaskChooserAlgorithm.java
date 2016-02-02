package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.BadRequestException;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentNotFoundException;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import spark.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * TaskChooserAlgorithm represents an algorithm which decides whether the worker should work on an Creative-Task,
 * Rating-Task or is finished.
 * @author LeanderK
 * @version 1.0
 */
public abstract class TaskChooserAlgorithm {
    protected final ExperimentOperations experimentOperations;
    protected final TaskOperations taskOperations;
    private final String pictureRegex = "\\{! ?\\S+ ?\\S+? ?\\}";
    private Pattern picturePattern = Pattern.compile("(" + pictureRegex + ")");
    private Pattern pictureUrlLicensePattern = Pattern.compile("\\{! ?(?<url>\\S+) ?(?<license>\\S+)? ?\\}");

    /**
     * creates an new TaskChooserAlgorithm
     * @param experimentOperations the ExperimentOperations used to communicate with the database.
     * @param taskOperations the TaskOperations used to communicate with the database
     */
    public TaskChooserAlgorithm(ExperimentOperations experimentOperations, TaskOperations taskOperations) {
        this.experimentOperations = experimentOperations;
        this.taskOperations = taskOperations;
    }

    /**
     * the name used to Identify the Strategy.
     * @return a string describing the strategy
     */
    public abstract String getName();

    /**
     * the description of the strategy
     * @return a string describing the strategy
     */
    public abstract String getDescription();

    /**
     * the needed parameter for the Strategy.
     * @return a map with the keys the description and the values the sql-regex describing the allowed values
     */
    public abstract List<TaskChooserParameter> getParameters();

    /**
     * this method gets called when the worker is ready to work on the rating/creative tasks.
     * Which task or whether the worker is already finished decides this method via return type.
     * Empty means the worker is already finished, or a view which specifies what the worker should work on.
     * @param builder the builder to use
     * @param request the request
     * @param experimentID the ID of the experiment
     * @param platform the platform the worker is working on
     * @param skipCreative whether to skip the Creative-Task
     * @param skipRating whether to skip the Rating-Task
     * @return empty if finished or view
     */
    public abstract Optional<View> next(View.Builder builder, Request request, int experimentID, String platform,
                                        boolean skipCreative, boolean skipRating);

    /**
     * constructs the an WorkerView depending on the passed parameters and the given answers/ratings of a worker.
     * @param builder the builder to use
     * @param experimentID the experiment worked on
     * @param skipCreative whether to skip the creative-phase
     * @param skipRating whether to skip the rating-phase
     * @return an view with the type Answer, Rating or empty
     */
    protected Optional<View> constructView(View.Builder builder, int experimentID, boolean skipCreative, boolean skipRating) {
        ExperimentRecord experiment = experimentOperations.getExperiment(experimentID);
        if (!skipCreative) {
            int answered = taskOperations.getAnswersCount(experimentID, builder.getWorkerId());
            if (answered < experiment.getAnwersPerWorker()) {
                return Optional.of(constructAnswerView(builder, experimentID, experiment.getAnwersPerWorker() - answered));
            }
        }
        if (!skipRating) {
            int rated = taskOperations.getRatingsCount(experimentID, builder.getWorkerId());
            if (rated < experiment.getRatingsPerWorker()) {
                return constructRatingView(builder, experimentID, experiment.getRatingsPerWorker() - rated);
            }
        }
        return Optional.empty();
    }

    /**
     * constructs an AnswerView
     * @param builder the builder to use
     * @param experimentID the id of the experiment
     * @param amount the amount to answer
     * @return an instance of View with the Type Answer and the information needed for an to display an answer
     * @throws BadRequestException if the experiment was not found
     */
    protected View constructAnswerView(View.Builder builder, int experimentID, int amount) throws BadRequestException {
        return prepareBuilder(builder, experimentID)
                .setType(View.Type.ANSWER)
                .setMaxAnswersToGive(amount)
                .build();
    }

    /**
     * constructs an RatingView
     * @param builder the builder to use
     * @param experimentID the ID of the experiment
     * @param amount the amount to rate
     * @return an instance of View with the Type Rating and the information needed for an to display an rating,
     *          or empty if no answers are available
     * @throws BadRequestException if the experiment was not found
     */
    protected Optional<View> constructRatingView(View.Builder builder, int experimentID, int amount) throws BadRequestException {
        List<View.Answer> toRate = taskOperations.prepareRating(builder.getWorkerId(), experimentID, amount)
                .stream()
                .map(record -> View.Answer.newBuilder()
                        .setAnswer(record.getAnswer())
                        .setId(record.getIdAnswer())
                        .build())
                .collect(Collectors.toList());
        if (toRate.isEmpty()) {
            System.err.println(String.format("no answers available to rate for experiment = %d, " +
                    "worker = %d", experimentID, builder.getWorkerId()));
            return Optional.empty();
        }
        return Optional.of(prepareBuilder(builder, experimentID)
                .addAllAnswersToRate(toRate)
                .setType(View.Type.RATING)
                .build());
    }

    /**
     * prepares the builder by setting all common information about the experiment.
     * It will set the Title, TaskID, Description, Pictures and Constraints.
     * @param builder the builder to use
     * @param experimentID the current experiment
     * @return an instance of builder with all common experiment-Data set.
     * @throws BadRequestException if the experiment is not existing
     */
    protected View.Builder prepareBuilder(View.Builder builder, int experimentID) throws BadRequestException {
        ExperimentRecord experimentRecord = null;
        try {
            experimentRecord = experimentOperations.getExperiment(experimentID);
        } catch (ExperimentNotFoundException e) {
            throw new BadRequestException("experiment not found : " + experimentID);
        }

        String mixedDescription = experimentRecord.getDescription();
        Matcher matcher = picturePattern.matcher(mixedDescription);

        List<View.Picture> pictures = new ArrayList<>();
        while(matcher.find()) {
            Matcher urlLicense = pictureUrlLicensePattern.matcher(matcher.group());
            if (!urlLicense.matches()) {
                throw new InternalServerErrorException("the regex to capture the picture url and license failed" + matcher.group());
            }

            View.Picture.Builder pictureBuilder = View.Picture.newBuilder()
                    .setUrl(urlLicense.group("url"));
            if (urlLicense.group("license") != null) {
                pictureBuilder = pictureBuilder.setUrlLicense(urlLicense.group("license"));
            }
            pictures.add(pictureBuilder.build());
        }

        String cleanDescription = mixedDescription.replaceAll("" + pictureRegex + "\\s?", "");
        List<View.Constraint> constraints = experimentOperations.getConstraints(experimentID)
                .map(constraint ->
                        View.Constraint.newBuilder()
                                .setId(constraint.getIdConstraint())
                                .setName(constraint.getConstraint())
                                .build()
                );

        return builder
                .setTitle(experimentRecord.getTitle())
                .setDescription(cleanDescription)
                .addAllPictures(pictures)
                .addAllConstraints(constraints);
    }
}
