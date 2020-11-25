package org.belog.stackcalculator.commandfactory.commands;

import org.belog.stackcalculator.commandfactory.Command;

import org.belog.stackcalculator.exceptions.NotEnoughElementsException;
import org.belog.stackcalculator.exceptions.NullStackException;

import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class Plus extends Command {

    private static final Logger logger = Logger.getLogger(Plus.class.getName());

    @Override
    public void execute(HashMap<String, Double> map, Stack<Double> stack, String parameters) {

        logger.info("Plus > execute > BEGIN");

        if (stack == null) {
            logger.error("Plus > execute > STACK IS NOT INITIALIZED");
            throw new NullStackException("STACK IS NOT INITIALIZED");
        }

        if (stack.size() < 2) {
            logger.error("Plus > execute > LESS THAN 2 ELEMENTS IN STACK");
            throw new NotEnoughElementsException("LESS THAN 2 ELEMENTS IN STACK");
        }

        Double value1 = stack.lastElement();
        stack.pop();

        Double value2 = stack.lastElement();
        stack.pop();

        Double result = value1 + value2;

        logger.info("Plus > execute > operation result: " + value2.toString() + " + " + value1.toString() + " = " + result.toString());

        stack.push(result);

        logger.info("Plus > execute > END");
    }
}