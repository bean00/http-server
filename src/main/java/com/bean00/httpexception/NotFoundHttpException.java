package com.bean00.httpexception;

public class NotFoundHttpException extends RuntimeException {

    public NotFoundHttpException() {
        super("Not found");
    }

}
