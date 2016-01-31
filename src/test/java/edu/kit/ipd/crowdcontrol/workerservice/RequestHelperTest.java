package edu.kit.ipd.crowdcontrol.workerservice;

import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.OperationsDataHolder;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.junit.Test;
import spark.Request;
import spark.Response;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author LeanderK
 * @version 1.0
 */
public class RequestHelperTest implements RequestHelper {

    @Test
    public void testAssertQuery() throws Exception {
        String test1 = OperationsDataHolder.nextRandomString();
        Request request = prepareRequest(test1, false, true);
        String result = assertQuery(request, "test");
        assertTrue(test1.equals(result));
    }

    @Test(expected = BadRequestException.class)
    public void testAssertQueryBadRequest() throws Exception {
        String test1 = OperationsDataHolder.nextRandomString();
        Request request = prepareRequest(test1, true, true);
        assertQuery(request, "test");
    }

    @Test
    public void testAssertQueryInt() throws Exception {
        int test = (int) (Math.random() * 30);
        Request request = prepareRequest(String.valueOf(test), false, true);
        int result = assertQueryInt(request, "test");
        assertTrue(result == test);
    }

    @Test(expected = BadRequestException.class)
    public void testAssertQueryIntBadRequest() throws Exception {
        int test = (int) (Math.random() * 30);
        Request request = prepareRequest(String.valueOf(test), true, true);
        assertQueryInt(request, "test");
    }

    @Test
    public void testTransform() throws Exception {
        Request request = prepareRequest(null, false, true);
        Response mock = mock(Response.class);
        JsonFormat.Parser parser = JsonFormat.parser();
        String test1 = OperationsDataHolder.nextRandomString();
        View build = View.newBuilder().setDescription(test1).build();
        String transform = transform(request, mock, build);
        View.Builder result = View.newBuilder();
        parser.merge(transform, result);
        assertTrue(result.getDescription().equals(test1));
    }

    @Test(expected = NotAcceptableException.class)
    public void testTransformNotAcceptable() throws Exception {
        Request request = prepareRequest(null, false, false);
        Response mock = mock(Response.class);
        String test1 = OperationsDataHolder.nextRandomString();
        View build = View.newBuilder().setDescription(test1).build();
        transform(request, mock, build);
    }

    private Request prepareRequest(String test, boolean empty, boolean validHeader) {
        Request mock = mock(Request.class);
        String[] array = new String[1];
        array[0] = test;
        if (empty)
            array = new String[0];
        when(mock.queryParamsValues("test")).thenReturn(array);
        String header = "application/json";
        if (!validHeader)
            header = "image";
        when(mock.headers("accept")).thenReturn(header);
        return mock;
    }
}