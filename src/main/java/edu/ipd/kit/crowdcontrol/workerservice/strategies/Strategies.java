package edu.ipd.kit.crowdcontrol.workerservice.strategies;

import edu.ipd.kit.crowdcontrol.workerservice.BadRequestException;
import edu.ipd.kit.crowdcontrol.workerservice.RequestHelper;
import edu.ipd.kit.crowdcontrol.workerservice.crowdplatform.Platforms;
import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.PlatformsRecord;
import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.PopulationRecord;
import edu.ipd.kit.crowdcontrol.workerservice.database.operations.PlatformOperations;
import edu.ipd.kit.crowdcontrol.workerservice.database.operations.PopulationsOperations;
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
public class Strategies implements RequestHelper {
    private final HashMap<String, Strategy> strategies =  new HashMap<>();
    private final PlatformOperations platformOperations;
    private final PopulationsOperations populationsOperations;
    private final Platforms platforms;

    public Strategies(PlatformOperations platformOperations, PopulationsOperations populationsOperations, Platforms platforms) {
        this.platformOperations = platformOperations;
        this.populationsOperations = populationsOperations;
        this.platforms = platforms;
        registerStrategy(new AntiSpoof());
    }

    public void registerStrategy(Strategy strategy) {
        strategies.put(strategy.getName(), strategy);
    }

    public ViewOuterClass.View getNext(Request request) {
        //TODO Error handling
        String workerID = request.queryParams("worker");
        if (workerID == null) {
            return handleNoWorkerID(request)
                    .orElse()
        } else {
            return getStrategyStep(request, workerID);
        }
    }

    private ViewOuterClass.View getStrategyStep(Request request, String workerID) {
        int id = Integer.parseInt(workerID);
        return getCalibrations(request)
                .orElseGet(() -> {
                    String platformName = assertParameter(request, "platform");
                    Strategy strategy = strategies.get(platformName);
                    //TODO platform not existing
                    return strategy.next(request);
                });
    }

    public Optional<ViewOuterClass.View> getCalibrations(Request request) {
        int worker = assertParameterInt(request, "worker");
        String platformName = assertParameter(request, "platform");
        int experiment = assertParameterInt(request, "experiment");
        return platformOperations.getPlatform(platformName)
                .filter(record -> !record.getNativeQualifications())
                .map(PlatformsRecord::getIdplatforms)
                .map(platformID -> populationsOperations.getCalibrations(experiment, platformID, worker))
                .map(this::constructCalibrationsView);
    }

    private Optional<ViewOuterClass.View> handleNoWorkerID(Request request) {
        String platformName = assertParameter(request, "platform");
        Boolean needsEmail = platforms.needsEmail(request, platformName)
                .orElseThrow(() -> new BadRequestException("unknown platform: " + platformName));
        if (needsEmail) {
            return Optional.of(ViewOuterClass.View.newBuilder()
                    .setType(ViewOuterClass.View.Type.EMAIL)
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private ViewOuterClass.View constructCalibrationsView(Map<PopulationRecord, List<String>> qualifications) {
        List<ViewOuterClass.View.Calibrations> calibrations = qualifications.entrySet().stream()
                .map(entry -> ViewOuterClass.View.Calibrations.newBuilder()
                        .setQuestion(entry.getKey().getProperty())
                        .setDescription(entry.getKey().getDescription())
                        .addAllAnswerOptions(entry.getValue())
                        .build()
                )
                .collect(Collectors.toList());
        return ViewOuterClass.View.newBuilder()
                .addAllCalibrations(calibrations)
                .setType(ViewOuterClass.View.Type.CALIBRATION)
                .build();
    }
}
