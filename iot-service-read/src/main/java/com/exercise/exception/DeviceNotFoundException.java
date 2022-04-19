package com.exercise.exception;

public class DeviceNotFoundException extends Exception {
    public DeviceNotFoundException() {
    }

    public DeviceNotFoundException(String message) {
        super(message);
    }
}
