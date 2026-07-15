package com.sagar.sms.exception;

public class CourseCodeAlreadyExistsException extends RuntimeException{

    public CourseCodeAlreadyExistsException() {
        super();
    }

    public CourseCodeAlreadyExistsException(String msg) {
        super(msg);
    }
}
