package edu.kit.ipd.crowdcontrol.workerservice;

import edu.kit.ipd.crowdcontrol.workerservice.database.operations.OperationsDataHolder;
import org.junit.Test;
import ratpack.handling.Context;
import ratpack.http.Headers;
import ratpack.http.MediaType;
import ratpack.http.Request;
import ratpack.http.Response;
import ratpack.path.PathTokens;
import ratpack.util.MultiValueMap;

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
        Context context = prepareContext(test1, false, true);
        String result = assertQuery(context, "test");
        assertTrue(test1.equals(result));
    }

    @Test(expected = BadRequestException.class)
    public void testAssertQueryBadRequest() throws Exception {
        String test1 = OperationsDataHolder.nextRandomString();
        Context context = prepareContext(test1, true, true);
        assertQuery(context, "test");
    }

    @Test
    public void testAssertQueryInt() throws Exception {
        int test = (int) (Math.random() * 30);
        Context context = prepareContext(String.valueOf(test), false, true);
        int result = assertQueryInt(context, "test");
        assertTrue(result == test);
    }

    @Test(expected = BadRequestException.class)
    public void testAssertQueryIntBadRequest() throws Exception {
        int test = (int) (Math.random() * 30);
        Context context = prepareContext(String.valueOf(test), true, true);
        assertQueryInt(context, "test");
    }
    /*
    @Test
    public void testTransform() throws Exception {
        Request request = prepareCONTEXT(null, false, true);
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
        Request request = prepareCONTEXT(null, false, false);
        Response mock = mock(Response.class);
        String test1 = OperationsDataHolder.nextRandomString();
        View build = View.newBuilder().setDescription(test1).build();
        transform(request, mock, build);
    }*/

    private Context prepareContext(String test, boolean empty, boolean validHeader) {
        Context context = mock(Context.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        Headers headers = mock(Headers.class);
        when(context.getRequest()).thenReturn(request);
        when(context.getResponse()).thenReturn(response);
        when(request.getHeaders()).thenReturn(headers);
        MediaType mediaType = mock(MediaType.class);
        when(request.getContentType()).thenReturn(mediaType);
        when(mediaType.getType()).thenReturn("x");
        when(request.getQueryParams()).thenReturn(mock(MultiValueMap.class));
        PathTokens pathTokens = mock(PathTokens.class);
        String returnQueryParam = test;
        if (empty)
            returnQueryParam = null;
        when(request.getQueryParams().get("test")).thenReturn(returnQueryParam);
        when(context.getRequest().getQueryParams().containsKey("test")).thenReturn(!empty);
        String header = "application/json";
        if (!validHeader)
            header = "image";
        when(headers.get("accept")).thenReturn(header);
        return context;
    }
}