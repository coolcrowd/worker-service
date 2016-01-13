package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.RequestHelper;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PopulationRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PopulationsOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.WorkerOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import spark.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The class query is responsible for the query-part of the CQRS-pattern. Therefore it provides the getNext-method used
 * for the /next query.
 * @author LeanderK
 * @version 1.0
 */
public class Query implements RequestHelper {
    private final HashMap<String, TaskChooserAlgorithm> strategies =  new HashMap<>();
    private final PopulationsOperations populationsOperations;
    private final ExperimentOperations experimentOperations;
    private final WorkerOperations workerOperations;

    public Query(PopulationsOperations populationsOperations, ExperimentOperations experimentOperations, WorkerOperations workerOperations, Platforms platforms) {
        this.populationsOperations = populationsOperations;
        this.experimentOperations = experimentOperations;
        this.workerOperations = workerOperations;
        registerTaskChooser(new AntiSpoof(experimentOperations));
    }

    /**
     * used to register a new TaskStrategy.
     * @param taskChooserAlgorithm the TaskChooserAlgorithm to register
     */
    private void registerTaskChooser(TaskChooserAlgorithm taskChooserAlgorithm) {
        strategies.put(taskChooserAlgorithm.getName(), taskChooserAlgorithm);
    }

    /**
     * this method returns an instance of View, determining what the worker should see next.
     * <p>
     *     it is indented to be called when the Router gets an /next-Request.
     * </p>
     * @param request the SparkJava-Request
     * @return an instance of View
     */
    public View getNext(Request request) {
        return getNext(prepareView(request), request);
    }

    /**
     * this method returns an instance of View, determining what the worker should see next.
     * @param builder the builder of the view containing a workerID or -1 if none provided
     * @param request the request
     * @return an instance of View
     */
    private View getNext(View.Builder builder, Request request) {
        if (builder.getWorkerId() == -1) {
            builder =  handleNoWorkerID(builder, request);
        }
        Optional<View> Calibration = getCalibration(builder, request);
        if (Calibration.isPresent()) {
            return Calibration.get();
        }
        Optional<View> strategyStep = getStrategyStep(builder, request);
        if (strategyStep.isPresent()) {
            return strategyStep.get();
        }
        Optional<View> email = getEmail(builder, request);
        if (email.isPresent()) {
            return email.get();
        }
        return workerFinished(builder, request);
    }

    /**
     * initializes a new ViewBuilder
     * @param request the Request
     * @return an instance of view with the workerID or -1 if none provided
     */
    private View.Builder prepareView(Request request) {
        //TODO better error message/catch exceptions
        View.Builder builder = View.newBuilder();
        String worker = request.queryParams("worker");
        if (worker != null) {
            builder.setWorkerId(Integer.parseInt(worker));
        } else {
            builder.setWorkerId(-1);
        }
        return builder;
    }

    /**
     * handels the case where no workerID was provided (workerID = -1).
     * This method just asks the Platform what to ID the worker should have and then calls getNext.
     * @param builder the builder to use
     * @param request the request
     * @return an instance of View.
     */
    private View.Builder handleNoWorkerID(View.Builder builder, Request request) {
        String platformName = assertParameter(request, "platform");
        return platforms.handleNoWorkerID(request, platformName)
                .map(builder::setWorkerId)
                .orElse(builder);
    }

    /**
     * may returns the Calibration if there are needed Calibration left unanswered.
     * @param builder the builder to user
     * @param request the request
     * @return an instance of view if the worker has to fill in some Calibration, or empty if not.
     */
    private Optional<View> getCalibration(View.Builder builder, Request request) {
        String platformName = assertParameter(request, "platform");
        int experiment = assertParameterInt(request, "experiment");
        if (platforms.hasNativeQualifications(platformName)) {
            return Optional.empty();
        } else {
            Map<PopulationRecord, List<String>> Calibration = populationsOperations.getCalibration(experiment, platforms.getID(platformName), builder.getWorkerId());
            if (Calibration.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(constructCalibrationView(Calibration, builder));
            }
        }
    }

    /**
     * constructs the Calibration view from the qualifications-data.
     * @param qualifications the qualifications
     * @param builder the builder to use
     * @return an instance of View with the Type Calibration and the Calibration set.
     */
    private View constructCalibrationView(Map<PopulationRecord, List<String>> qualifications, View.Builder builder) {
        List<View.Calibration> Calibration = qualifications.entrySet().stream()
                .map(entry -> View.Calibration.newBuilder()
                        .setQuestion(entry.getKey().getProperty())
                        .setId(entry.getKey().getIdpopulation())
                        .addAllAnswerOptions(entry.getValue())
                        .build()
                )
                .collect(Collectors.toList());
        return builder.addAllCalibrations(Calibration)
                .setType(View.Type.CALIBRATION)
                .build();
    }

    /**
     * may returns the next TaskView if the worker has not finished the assignment.
     * @param builder the builder to use
     * @param request the request
     * @return a Task-View filled with the assignment, or empty if finished
     */
    private Optional<View> getStrategyStep(View.Builder builder, Request request) {
        int experiment = assertParameterInt(request, "experiment");
        return experimentOperations.getExperiment(experiment)
                .map(ExperimentRecord::getAlgorithmTaskChooser)
                .flatMap(algo -> Optional.ofNullable(strategies.get(algo)))
                .flatMap(strategy -> strategy.next(builder, request, experiment));

    }

    /**
     * may returns a View with the Type email if the platform needs an email and the user lacks one
     * @param builder the builder to use
     * @param request the request
     * @return an instance of view with the type email or empty
     */
    private Optional<View> getEmail(View.Builder builder, Request request) {
        String platformName = assertParameter(request, "platform");
        boolean hasNoEmail = true;
        if (builder.getWorkerId() != -1) {
            hasNoEmail = !workerOperations.hasEmail(builder.getWorkerId());
        }
        if (platforms.needsEmail(platformName) && hasNoEmail) {
            return Optional.of(builder.setType(View.Type.EMAIL).build());
        } else {
            return Optional.empty();
        }
    }

    /**
     * notifies the platform that the worker has finished the assignment and constructs the Finished View
     * @param builder the builder to use
     * @param request the request
     * @return an View with the Type FINISHED
     */
    private View workerFinished(View.Builder builder, Request request) {
        String platformName = assertParameter(request, "platform");
        //TODO: notify?
        return builder.setType(View.Type.FINISHED)
                .build();
    }
}
