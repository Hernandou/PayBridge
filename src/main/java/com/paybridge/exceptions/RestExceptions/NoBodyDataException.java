package com.paybridge.exceptions.RestExceptions;

public class NoBodyDataException extends Exception {

    public NoBodyDataException() {
        super("No body data provided");
    }

    public NoBodyDataException(String message) {
        super(message);
    }

}
