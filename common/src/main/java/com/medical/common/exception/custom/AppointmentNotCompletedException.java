package com.medical.common.exception.custom;


public class AppointmentNotCompletedException extends RuntimeException {
    public AppointmentNotCompletedException(String message) {
        super(message);
    }
}
