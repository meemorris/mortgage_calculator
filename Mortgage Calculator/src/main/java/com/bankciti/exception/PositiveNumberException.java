package com.bankciti.exception;

public class PositiveNumberException extends Exception {

    public PositiveNumberException() {
        super("*** Please enter a positive value. ***");
    }
}
