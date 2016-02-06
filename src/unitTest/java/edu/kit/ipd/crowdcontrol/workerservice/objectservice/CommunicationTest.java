package edu.kit.ipd.crowdcontrol.workerservice.objectservice;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.http.client.HttpClient;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author LeanderK
 * @version 1.0
 */
public class CommunicationTest {
    private String url = "http://www.abc.com";

    @Test
    public void testSubmitWorkerURL() throws Exception {
        Communication communication = new Communication(url, "a", "b") {
            @Override
            public CompletableFuture<Optional<Integer>> tryGetWorkerID(String platform, Map<String, String[]> queryParameter) {
                assertTrue(queryParameter.get("email")[0].equals("a"));
                return CompletableFuture.completedFuture(Optional.of(1));
            }
        };
        CompletableFuture<Integer> result = communication.submitWorker("a", "b", new HashMap<>());
        checkURL(result, "/workers");
    }

    @Test
    public void testSubmitAnswerURL() throws Exception {
        Communication communication = new Communication(url, "a", "b");
        CompletableFuture<Integer> result = communication.submitAnswer("a", 1, 2);
        checkURL(result, "/experiments/1/answers");
    }

    @Test
    public void testSubmitRatingURL() throws Exception {
        Communication communication = new Communication(url, "a", "b");
        CompletableFuture<Void> result = communication.submitRating(1, "a", 2, 3, 4, new ArrayList<>());
        checkURL(result, "/experiments/2/answers/3/ratings");
    }

    @Test
    public void testSubmitCalibrationURL() throws Exception {
        Communication communication = new Communication(url, "a", "b");
        CompletableFuture<HttpResponse<JsonNode>> result = communication.submitCalibration(1, 2);
        checkURL(result, "/workers/2/calibrations/");
    }

    @Test
    public void testTryGetWorkerIDURL() throws Exception {
        Communication communication = new Communication(url, "a", "b");
        HashMap<String, String[]> params = new HashMap<>();
        params.put("key", new String[]{"value"});
        CompletableFuture<Optional<Integer>> result = communication.tryGetWorkerID("plat", params);
        checkURL(result, "/experiments/1/answers");
    }

    private <T> boolean checkURL(CompletableFuture<T> future, String checkUrl) {
        return future.handle((t, throwable) -> {
            String message = throwable.getMessage();
            String url = message.substring(message.indexOf(this.url) + this.url.length(), message.length());
            return url.equals(checkUrl) || (!url.contains("?") && url.equals(checkUrl + "?"));
        }).join();
    }
}