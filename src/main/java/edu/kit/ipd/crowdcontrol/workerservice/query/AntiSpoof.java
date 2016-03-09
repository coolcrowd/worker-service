package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentsPlatformOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a 3-Phase TaskChooserAlgorithm, in the first phase only creative tasks are given to workers, then the worker
 * get also Rating tasks. In the last phase the workers only work on rating-tasks.
 * @author LeanderK
 * @version 1.0
 */
public class AntiSpoof extends TaskChooserAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(AntiSpoof.class);
    static final String NAME = "anti_spoof";
    static final String DESCRIPTION = "This AntiSpoof algorithm divides the runtime of the experiment into 3 phases. In " +
            "the first phase the workers are only allowed to work on creative tasks. Then in the second " +
            "phase, the worker can work on both the creative and the rating task. The last phase consists only of the " +
            "assignment to rate. You can set the duration of the first phase either as an absolute number or as an " +
            "percentage of the total needed answers of the experiment. The duration of the second phase is defined as " +
            "the difference of the first phase and the total number of answers needed. The third phase will then run " +
            "until all the remaining ratings got collected.";
    private static final String REGEX_ABSOLUTE = "ab";
    private static final String REGEX_PERCENTAGE = "pc";
    static final String REGEX = "(([0-9]+"+ REGEX_ABSOLUTE +")|((100|[0-9]?[0-9])"+ REGEX_PERCENTAGE +"))";
    static final String DB_REGEX = "^" + REGEX + "$";
    private static final String PARAM_DESCRIPTION = "Set the amount of answers in the first phase. To set the absolute " +
            "number just type the number followed by ab, example \"150ab\" for 150 answers. To set the parameter to a " +
            "percentage type first the percentage followed by pc, example \"30pc\" for 30 percent.";


    /**
     * creates an new AntiSpoof
     *
     * @param experimentOperations the ExperimentOperations used to communicate with the database.
     * @param experimentsPlatformOperations the TaskOperations used to communicate with the database
     */
    public AntiSpoof(ExperimentOperations experimentOperations, ExperimentsPlatformOperations experimentsPlatformOperations) {
        super(experimentOperations, experimentsPlatformOperations);
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
        return Collections.singletonList(new TaskChooserParameter(PARAM_DESCRIPTION, DB_REGEX, "1"));
    }

    /**
     * this method gets called when the worker is ready to work on the rating/creative tasks.
     * Which task or whether the worker is already finished decides this method via return type.
     * Empty means the worker is already finished, or a view which specifies what the worker should work on.
     *
     * @param builder      the builder to use
     * @param context the Context of the Request
     * @param experimentID the ID of the experiment
     * @param platform     the platform the worker is working on
     * @param skipCreative whether to skip the Creative-Task
     * @param skipRating   whether to skip the Rating-Task
     * @return empty if finished or view
     */
    @Override
    public Optional<View> next(View.Builder builder, Context context, int experimentID, String platform,
                               boolean skipCreative, boolean skipRating) {
        int answersCount = experimentsPlatformOperations.getAnswersCount(experimentID);
        Map<Integer, Integer> phases = experimentOperations.getTaskChooserParam(experimentID).entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Integer.parseInt(entry.getKey()),
                        entry -> getParameterValue(entry.getValue(), experimentID)
                ));
        if (phases.get(1) == null) {
            throw new IllegalStateException("Anti-Spoof parameter not set.");
        }
        ExperimentRecord experiment = experimentOperations.getExperiment(experimentID);
        logger.trace("calculating phase for: answersCount={}, phases={}, neededAnswers={}",
                answersCount, phases, experiment.getNeededAnswers());
        if ((answersCount <= phases.get(1)) && (answersCount < experiment.getNeededAnswers())) {
            logger.debug("entering phase 1");
            //phase 1: no rating
            return constructView(builder, context, experimentID, skipCreative, true);
        } else if (answersCount < experiment.getNeededAnswers()) {
            logger.debug("entering phase 2");
            //phase 2: rating + creative
            return constructView(builder, context, experimentID, skipCreative, skipRating);
        } else {
            logger.debug("entering phase 3");
            return constructView(builder, context, experimentID, true, skipRating);
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
