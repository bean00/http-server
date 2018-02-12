package com.bean00;

public class BadRequestHttpException extends RuntimeException {

    public BadRequestHttpException() {
        super("Bad request");
    }

}
