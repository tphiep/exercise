package com.exercise.exception;

public class MongoDBException extends RuntimeException {
    public MongoDBException() {
    }

    public MongoDBException(String message) {
        super(message);
    }
}
