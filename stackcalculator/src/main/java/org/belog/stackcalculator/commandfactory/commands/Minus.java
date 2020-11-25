package org.belog.stackcalculator.commandfactory.commands;

import org.belog.stackcalculator.commandfactory.Command;

import org.belog.stackcalculator.exceptions.NotEnoughElementsException;
import org.belog.stackcalculator.exceptions.NullStackException;

import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class Minus extends Command {

    private static final Logger logger = Logger.getLogger(Minus.class.getName());

    @Override
    public void execute(HashMap<String, Double> map, Stack<Double> stack, String parameters) {

        logger.info("Minus > execute > BEGIN");

        if (stack == null) {
            logger.error("Minus > execute > STACK IS NOT INITIALIZED");
            throw new NullStackException("STACK IS NOT INITIALIZED");
        }

        if (stack.size() < 2) {
            logger.error("Minus > execute > LESS THAN 2 ELEMENTS IN STACK");
            throw new NotEnoughElementsException("LESS THAN 2 ELEMENTS IN STACK");
        }

        Double value1 = stack.lastElement();
        stack.pop();

        Double value2 = stack.lastElement();
        stack.pop();

        Double result = value2 - value1;

        logger.info("Minus > execute > operation result: " + value2.toString() + " - " + value1.toString() + " = " + result.toString());

        stack.push(result);

        logger.info("Minus > execute > END");
    }
}