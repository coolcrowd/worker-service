package edu.kit.ipd.crowdcontrol.workerservice.query;

import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PlatformRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PlatformOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PopulationsOperations;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.junit.Test;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author LeanderK
 * @version 1.0
 */
public class QueryTest {
    private final JsonFormat.Parser parser = JsonFormat.parser();

    private Request prepareRequest(int experimentID, String platform, Map<String, String[]> queryMap) {
        Request mock = mock(Request.class);
        when(mock.params("platform")).thenReturn(platform);
        when(mock.params("experiment")).thenReturn(String.valueOf(experimentID));
        QueryParamsMap queryParamsMap = mock(QueryParamsMap.class);
        when(queryParamsMap.toMap()).thenReturn(queryMap);
        when(mock.queryMap()).thenReturn(queryParamsMap);
        return mock;
    }

    private PlatformOperations preparePlatformOperations(String platform, boolean NeedsEmail, boolean calibrations) {
        PlatformOperations platformOperations = mock(PlatformOperations.class);
        PlatformRecord platformRecord = mock(PlatformRecord.class);
        when(platformRecord.getNeedsEmail()).thenReturn(NeedsEmail);
        when(platformRecord.getRenderCalibrations()).thenReturn(calibrations);
        when(platformOperations.getPlatform(platform)).thenReturn(platformRecord);
        return platformOperations;
    }

    private PopulationsOperations preparePopulationOperations(int experimentID, String platform, int workerID, boolean belongsToWrongPopulation) {
        PopulationsOperations populations = mock(PopulationsOperations.class);
        when(populations.belongsToWrongPopulation(experimentID, platform, workerID)).thenReturn(belongsToWrongPopulation);
        return populations;
    }

    private Communication prepareCommunication(String platform, Optional<Integer> workerID) {
        Communication communication = mock(Communication.class);
        when(communication.tryGetWorkerID(platform, null))
                .thenReturn(CompletableFuture.completedFuture(workerID));
        return communication;
    }

    @Test
    public void testExpectEmail() throws Exception {
        int experimentID = 1;
        String platform = "a";
        Request request = prepareRequest(experimentID, platform, null);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = preparePlatformOperations(platform, true, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        Query query =  new Query(null, null, platformOperations, communication, null);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.EMAIL));
    }

    @Test(expected = InternalServerErrorException.class)
    public void testExpectEmailFail() throws Exception {
        int experimentID = 1;
        String platform = "a";
        Request request = prepareRequest(experimentID, platform, null);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        Query query =  new Query(null, null, platformOperations, communication, null);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.EMAIL));
    }

    @Test
    public void testExpectGetWorkerID() throws Exception {
        int experimentID = 1;
        String platform = "a";
        int workerID = 3;
        Request request = prepareRequest(experimentID, platform, null);
        //when(request.queryParams("worker")).thenReturn(String.valueOf(workerID));
        Response response = mock(Response.class);
        PlatformOperations platformOperations = preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.of(workerID));
        PopulationsOperations populationsOperations = preparePopulationOperations(experimentID, platform, workerID, true);
        Query query =  new Query(populationsOperations, null, platformOperations, communication, null);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
    }

}