package com.bean00.httpexception;

public class BadRequestHttpException extends RuntimeException {

    public BadRequestHttpException() {
        super("");
    }

    public BadRequestHttpException(String errorMessage) {
        super(errorMessage);
    }

}
