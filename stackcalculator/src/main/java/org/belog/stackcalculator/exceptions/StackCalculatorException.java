package org.belog.stackcalculator.exceptions;

public class StackCalculatorException extends RuntimeException {
    StackCalculatorException(String message) {
        super("StackCalculatorException: " + message);
    }
}