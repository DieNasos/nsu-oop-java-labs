package org.belog.stackcalculator.commandfactory.commands;

import org.belog.stackcalculator.commandfactory.Command;
import org.belog.stackcalculator.exceptions.DivisionByZeroException;
import org.belog.stackcalculator.exceptions.NotEnoughElementsException;
import org.belog.stackcalculator.exceptions.NullStackException;

import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class Div extends Command {

    private static final Logger logger = Logger.getLogger(Div.class.getName());

    @Override
    public void execute(HashMap<String, Double> map, Stack<Double> stack, String parameters) {

        logger.info("Div > execute > BEGIN");

        if (stack == null) {
            logger.error("Div > execute > STACK IS NOT INITIALIZED");
            throw new NullStackException("STACK IS NOT INITIALIZED");
        }

        if (stack.size() < 2) {
            logger.error("Div > execute > LESS THAN 2 ELEMENTS IN STACK");
            throw new NotEnoughElementsException("LESS THAN 2 ELEMENTS IN STACK");
        }

        Double value1 = stack.lastElement();
        stack.pop();

        Double value2 = stack.lastElement();
        stack.pop();

        if (value1 == 0) {
            logger.error("Div > execute > DIVISION BY ZERO");
            throw new DivisionByZeroException("DIVISION BY ZERO");
        }

        Double result = value2 / value1;

        logger.info("Div > execute > operation result: " + value2.toString() + " / " + value1.toString() + " = " + result.toString());

        stack.push(result);

        logger.info("Div > execute > END");
    }
}