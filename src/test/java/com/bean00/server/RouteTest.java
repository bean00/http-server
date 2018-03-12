package com.bean00.server;

import com.bean00.httpmessages.Method;
import com.bean00.httpmessages.Request;
import io.vavr.CheckedFunction1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RouteTest {

    @Test
    public void urlsMatch_returnsFalse_whenTheURLsDoNotMatch() {
        Request request = new Request(Method.GET, "/file");
        Route route = new Route(Method.GET, "/", (requestParam) -> null);

        boolean urlsMatch = route.urlsMatch(request);

        assertFalse(urlsMatch);
    }

    @Test
    public void urlsMatch_returnsTrue_whenTheURLsAreAnExactMatch() {
        Request request = new Request(Method.GET, "/file");
        Route route = new Route(Method.GET, "/file", (requestParam) -> null);

        boolean urlsMatch = route.urlsMatch(request);

        assertTrue(urlsMatch);
    }

    @Test
    public void urlsMatch_returnsTrue_whenTheRouteURLIsAWildcard() {
        Request request = new Request(Method.GET, "/file");
        Route route = new Route(Method.GET, "*", (requestParam) -> null);

        boolean urlsMatch = route.urlsMatch(request);

        assertTrue(urlsMatch);
    }

    @Test
    public void methodsMatch_returnsFalse_whenTheMethodsDoNotMatch() {
        Request request = new Request(Method.GET, "/");
        Route route = new Route(Method.HEAD, "/", (requestParam) -> null);

        boolean methodsMatch = route.methodsMatch(request);

        assertFalse(methodsMatch);
    }

    @Test
    public void methodsMatch_returnsTrue_whenTheMethodsMatch() {
        Request request = new Request(Method.GET, "/");
        Route route = new Route(Method.GET, "/", (requestParam) -> null);

        boolean methodsMatch = route.methodsMatch(request);

        assertTrue(methodsMatch);
    }

    @Test
    public void execute_callsTheControllerFunction() throws Throwable {
        Request request = new Request(Method.GET, "/");
        CheckedFunction1 controller = spy(CheckedFunction1.class);
        Route route = new Route(Method.GET, "/", controller);

        route.execute(request);

        verify(controller, times(1)).apply(request);
    }

}
