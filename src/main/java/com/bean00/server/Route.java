package com.bean00.server;

import com.bean00.httpmessages.Request;
import com.bean00.httpmessages.Response;

import io.vavr.CheckedFunction1;

public class Route {
    private String routeMethod;
    private String routeURL;
    private CheckedFunction1<Request, Response> controller;

    public Route(String routeMethod, String routeURL, CheckedFunction1<Request, Response> controller) {
        this.routeMethod = routeMethod;
        this.routeURL = routeURL;
        this.controller = controller;
    }

    public boolean urlsMatch(Request request) {
        return routeURL.equals("*") || routeURL.equals(request.getRequestURL());
    }

    public boolean methodsMatch(Request request) {
        return routeMethod.equals(request.getRequestMethod());
    }

    public Response execute(Request request) throws Throwable {
        return controller.apply(request);
    }

    public String getRouteMethod() {
        return routeMethod;
    }

}
