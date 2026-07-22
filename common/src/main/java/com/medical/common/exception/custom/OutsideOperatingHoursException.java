package com.medical.common.exception.custom;


public class OutsideOperatingHoursException extends RuntimeException {
    public OutsideOperatingHoursException(String message) {
        super(message);
    }
}
