package com.bankciti.exception;

public class InvalidRateException extends Exception {

    public InvalidRateException() {
        super("Rate provided should follow this format (x.xx) and cannot have more than 3 numbers to the right of the decimal point.");
    }

}
