package com.bankciti.exception;

public class DateFormatException extends Exception {

    public DateFormatException() {
        super("*** Date entered needs to follow this format: MM/YYYY ***");
    }

}
