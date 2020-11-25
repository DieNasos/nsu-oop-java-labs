package org.belog.stackcalculator.exceptions;

public class DivisionByZeroException extends StackCalculatorException {
    public DivisionByZeroException(String message) {
        super(message);
    }
}