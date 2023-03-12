package com.console.ticket.exception;

public class ParseException extends RuntimeException{

    public ParseException() {
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
