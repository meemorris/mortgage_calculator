package com.bankciti.exception;

public class MortgageTermException extends Exception {

    public MortgageTermException() {
        super("*** The longest mortgage term available in the United States is 50 years. ***");
    }

}
