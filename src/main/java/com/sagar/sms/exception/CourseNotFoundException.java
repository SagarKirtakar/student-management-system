package com.sagar.sms.exception;

public class CourseNotFoundException extends RuntimeException{

    public CourseNotFoundException() {
        super();
    }

    public CourseNotFoundException(String msg) {
        super(msg);
    }

}
