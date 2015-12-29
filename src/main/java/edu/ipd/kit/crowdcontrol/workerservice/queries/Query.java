package edu.ipd.kit.crowdcontrol.workerservice.queries;

import edu.ipd.kit.crowdcontrol.workerservice.RequestHelper;
import edu.ipd.kit.crowdcontrol.workerservice.crowdplatform.Platforms;
import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.PlatformsRecord;
import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.PopulationRecord;
import edu.ipd.kit.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.ipd.kit.crowdcontrol.workerservice.database.operations.PlatformOperations;
import edu.ipd.kit.crowdcontrol.workerservice.database.operations.PopulationsOperations;
import edu.ipd.kit.crowdcontrol.workerservice.database.operations.WorkerOperations;
import edu.ipd.kit.crowdcontrol.workerservice.proto.ViewOuterClass;
import spark.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * decides what the worker should be seeing
 * @author LeanderK
 * @version 1.0
 */
public class Query implements RequestHelper {
    private final HashMap<String, TaskChooserAlgorithm> strategies =  new HashMap<>();
    private final PlatformOperations platformOperations;
    private final PopulationsOperations populationsOperations;
    private final ExperimentOperations experimentOperations;
    private final WorkerOperations workerOperations;
    private final Platforms platforms;

    public Query(PlatformOperations platformOperations, PopulationsOperations populationsOperations, ExperimentOperations experimentOperations, WorkerOperations workerOperations, Platforms platforms) {
        this.platformOperations = platformOperations;
        this.populationsOperations = populationsOperations;
        this.experimentOperations = experimentOperations;
        this.workerOperations = workerOperations;
        this.platforms = platforms;
        registerTaskChooser(new AntiSpoof());
    }

    /**
     * used to register a new TaskStrategy.
     * @param taskChooserAlgorithm the TaskStrategy to register
     */
    public void registerTaskChooser(TaskChooserAlgorithm taskChooserAlgorithm) {
        strategies.put(taskChooserAlgorithm.getName(), taskChooserAlgorithm);
    }

    /**
     * this method returns an instance of View, determining what the worker should see next.
     * @param request the Request
     * @return an instance of View
     */
    public ViewOuterClass.View getNext(Request request) {
        return getNext(prepareView(request), request);
    }

    /**
     * this method returns an instance of View, determining what the worker should see next.
     * @param builder the builder of the view containing a workerID or -1 if none provided
     * @param request the request
     * @return an instance of View
     */
    private ViewOuterClass.View getNext(ViewOuterClass.View.Builder builder, Request request) {
        if (builder.getWorkerID() == -1) {
            return handleNoWorkerID(builder, request);
        } else {
            Optional<ViewOuterClass.View> calibrations = getCalibrations(builder, request);
            if (calibrations.isPresent()) {
                return calibrations.get();
            }
            Optional<ViewOuterClass.View> strategyStep = getStrategyStep(builder, request);
            if (strategyStep.isPresent()) {
                return strategyStep.get();
            }
            Optional<ViewOuterClass.View> email = getEmail(builder, request);
            if (email.isPresent()) {
                return email.get();
            }
            return workerFinished(builder, request);
        }
    }

    /**
     * initializes a new ViewBuilder
     * @param request the Request
     * @return an instance of view with the workerID or -1 if none provided
     */
    private ViewOuterClass.View.Builder prepareView(Request request) {
        //TODO better error message/catch exceptions
        ViewOuterClass.View.Builder builder = ViewOuterClass.View.newBuilder();
        String worker = request.queryParams("worker");
        if (worker != null) {
            builder.setWorkerID(Integer.parseInt(worker));
        } else {
            builder.setWorkerID(-1);
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
    private ViewOuterClass.View handleNoWorkerID(ViewOuterClass.View.Builder builder, Request request) {
        String platformName = assertParameter(request, "platform");
        int workerID = platforms.handleNoWorkerID(request, platformName);
        builder.setWorkerID(workerID);
        return getNext(builder, request);
    }

    /**
     * may returns the calibrations if there are needed Calibrations left unanswered.
     * @param builder the builder to user
     * @param request the request
     * @return an instance of view if the worker has to fill in some calibrations, or empty if not.
     */
    private Optional<ViewOuterClass.View> getCalibrations(ViewOuterClass.View.Builder builder, Request request) {
        String platformName = assertParameter(request, "platform");
        int experiment = assertParameterInt(request, "experiment");
        PlatformsRecord platform = platformOperations.getPlatform(platformName);
        if (platform.getNativeQualifications()) {
            return Optional.empty();
        } else {
            Map<PopulationRecord, List<String>> calibrations = populationsOperations.getCalibrations(experiment, platform.getIdplatforms(), builder.getWorkerID());
            if (calibrations.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(constructCalibrationsView(calibrations, builder));
            }
        }
    }

    /**
     * constructs the calibrations view from the qualifications-data.
     * @param qualifications the qualifications
     * @param builder the builder to use
     * @return an instance of View with the Type Calibration and the Calibrations set.
     */
    private ViewOuterClass.View constructCalibrationsView(Map<PopulationRecord, List<String>> qualifications, ViewOuterClass.View.Builder builder) {
        List<ViewOuterClass.View.Calibrations> calibrations = qualifications.entrySet().stream()
                .map(entry -> ViewOuterClass.View.Calibrations.newBuilder()
                        .setQuestion(entry.getKey().getProperty())
                        .setDescription(entry.getKey().getDescription())
                        .addAllAnswerOptions(entry.getValue())
                        .build()
                )
                .collect(Collectors.toList());
        return builder.addAllCalibrations(calibrations)
                .setType(ViewOuterClass.View.Type.CALIBRATION)
                .build();
    }

    /**
     * may returns the next TaskView if the worker has not finished the assignment.
     * @param builder the builder to use
     * @param request the request
     * @return a Task-View filled with the assignment, or empty if finished
     */
    private Optional<ViewOuterClass.View> getStrategyStep(ViewOuterClass.View.Builder builder, Request request) {
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
    private Optional<ViewOuterClass.View> getEmail(ViewOuterClass.View.Builder builder, Request request) {
        String platformName = assertParameter(request, "platform");
        if (platforms.needsEmail(platformName) && !workerOperations.hasEmail(builder.getWorkerID())) {
            return Optional.of(builder.setType(ViewOuterClass.View.Type.EMAIL).build());
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
    private ViewOuterClass.View workerFinished(ViewOuterClass.View.Builder builder, Request request) {
        String platformName = assertParameter(request, "platform");
        platforms.workerFinished(request, platformName);
        return builder.setType(ViewOuterClass.View.Type.FINISHED)
                .build();
    }
}
