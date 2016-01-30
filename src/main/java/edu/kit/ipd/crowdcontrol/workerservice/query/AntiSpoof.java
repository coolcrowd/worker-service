package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.jooq.lambda.tuple.Tuple2;
import spark.Request;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is a 3-Phase TaskChooserAlgorithm, in the first phase only creative tasks are given to workers, then the worker
 * get also Rating tasks. In the last phase the workers only work on rating-tasks.
 * @author LeanderK
 * @version 1.0
 */
public class AntiSpoof extends TaskChooserAlgorithm {
    static final String NAME = "anti-spoof";
    static final String DESCRIPTION = "This AntiSpoof algorithm divides the runtime of the experiment into 3 phases. In " +
            "the first phase the workers are only allowed to work on creative tasks. Then in the second " +
            "phase, the worker can work on both the creative and the rating task. The last phase consists only of the " +
            "assignment to rate.";
    private static final String REGEX_ABSOLUTE = "ab";
    private static final String REGEX_PERCENTAGE = "pc";
    static final String REGEX = "(([0-9]+"+ REGEX_ABSOLUTE +")|((100|[0-9]?[0-9])"+ REGEX_PERCENTAGE +"))";
    static final String DB_REGEX = "^" + REGEX + "$";
    private static final String PARAM_DESCRIPTION = "Set the amount of answers the phase %d represents. The following " +
            "formats are supported: [0-9]+ab for setting an absolute number of answers. An Example: 123ab sets the" +
            "duration of the phase to 123 answers. Another way to set duration is the percentage of all answers and " +
            "letters 'pc'. For this format an example would be 30pc, which sets the duration of the phase to 30 percent" +
            " of the total answers of the experiment";


    /**
     * creates an new AntiSpoof
     *
     * @param experimentOperations the ExperimentOperations used to communicate with the database.
     * @param taskOperations the TaskOperations used to communicate with the database
     */
    public AntiSpoof(ExperimentOperations experimentOperations, TaskOperations taskOperations) {
        super(experimentOperations, taskOperations);
    }

    /**
     * the name used to Identify the Strategy.
     *
     * @return a string describing the strategy
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * the description of the strategy
     *
     * @return a string describing the strategy
     */
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * the needed parameter for the Strategy.
     *
     * @return a map with the keys the description and the values the regex describing the allowed values
     */
    @Override
    public List<TaskChooserParameter> getParameters() {
        //TODO: we don't need the second phase?!
        return Stream.of(1, 2)
                .map(phase -> new Tuple2<>(String.format(PARAM_DESCRIPTION, phase), String.valueOf(phase)))
                .map(descriptionPhase -> new TaskChooserParameter(descriptionPhase.v1, DB_REGEX, descriptionPhase.v2))
                .collect(Collectors.toList());
    }

    /**
     * this method gets called when the worker is ready to work on the rating/creative tasks.
     * Which task or whether the worker is already finished decides this method via return type.
     * Empty means the worker is already finished, or a view which specifies what the worker should work on.
     *
     * @param builder      the builder to use
     * @param request      the request
     * @param experimentID the ID of the experiment
     * @param platform     the platform the worker is working on
     * @param skipCreative whether to skip the Creative-Task
     * @param skipRating   whether to skip the Rating-Task
     * @return empty if finished or view
     */
    @Override
    public Optional<View> next(View.Builder builder, Request request, int experimentID, String platform,
                               boolean skipCreative, boolean skipRating) {
        int answersCount = taskOperations.getAnswersCount(experimentID);
        Map<Integer, Integer> phases = experimentOperations.getTaskChooserParam(experimentID).entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Integer.parseInt(entry.getKey()),
                        entry -> getParameterValue(entry.getValue(), experimentID)
                ));
        ExperimentRecord experiment = experimentOperations.getExperiment(experimentID);
        if (answersCount <= phases.get(1)) {
            //phase 1: no rating
            return constructView(builder, experimentID, skipCreative, true);
        } else if (answersCount <= (phases.get(1) + phases.get(2))) {
            //phase 2: rating + creative
            return constructView(builder, experimentID, skipCreative, skipRating);
        } else if (answersCount < experiment.getNeededAnswers()){
            System.err.println(String.format("AntiSpoof should be in phase 3, but there are answers missing, " +
                    "experiment = %d, worker = %d, answersCount = %d", experimentID, builder.getWorkerId(), answersCount));
            //phase 3: if the user made an error we would be missing some of the creative-answers
            return constructView(builder, experimentID, skipCreative, skipRating);
        } else {
            return constructView(builder, experimentID, true, skipRating);
        }
    }

    /**
     * this method returns the absolute number of answers for the parameter.
     * <p>
     * if the user chose an absolute number, it returns the number. If the parameter was a percentage, it calculates
     * the absolute number and returns it.
     * @param value the parameter-value
     * @param experimentID the experiment the parameter belongs to
     * @return the absolute number of answers the value represents
     */
    int getParameterValue(String value, int experimentID) {
        if (value.contains(REGEX_ABSOLUTE)) {
            return Integer.parseInt(value.replace(REGEX_ABSOLUTE, ""));
        } else {
            int doubleDigitPercentage = Integer.parseInt(value.replace(REGEX_PERCENTAGE, ""));
            ExperimentRecord experiment = experimentOperations.getExperiment(experimentID);
            return (int) (experiment.getNeededAnswers() * (doubleDigitPercentage / 100.0));
        }
    }
}
