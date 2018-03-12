package com.bean00.server;

import com.bean00.httpmessages.HttpHeaders;
import com.bean00.httpmessages.Request;
import com.bean00.httpmessages.Response;
import com.bean00.httpmessages.Status;
import io.vavr.CheckedFunction1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class RequestProcessor {
    private List<Route> routes = new ArrayList<>();

    public void addRoute(String routeMethod, String routeURL, CheckedFunction1<Request, Response> controller) {
        Route route = new Route(routeMethod, routeURL, controller);

        routes.add(route);
    }

    public Response processRequest(Request request) throws Throwable {
        Response response;
        Set<String> allowedMethods = new TreeSet<>();

        for (Route route : routes) {
            boolean urlsMatch = route.urlsMatch(request);
            boolean methodsMatch = route.methodsMatch(request);

            if (urlsMatch && methodsMatch) {
                return route.execute(request);
            } else if (urlsMatch) {
                allowedMethods.add(route.getRouteMethod());
            }
        }

        if (!allowedMethods.isEmpty()) {
            response = buildMethodNotAllowedResponse(allowedMethods);
        } else {
            response = new Response(Status.NOT_FOUND);
        }

        return response;
    }

    private Response buildMethodNotAllowedResponse(Set<String> allowedMethodsSet) {
        List<String> allowedMethodsList = new ArrayList<>(allowedMethodsSet);
        String allowedMethodsString = allowedMethodsList.stream().collect(Collectors.joining(", "));

        HttpHeaders headers = new HttpHeaders();
        headers.setHeader("Allow", allowedMethodsString);

        return new Response(Status.METHOD_NOT_ALLOWED, headers);
    }

}
