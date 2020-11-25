package org.belog.stackcalculator.commandfactory.commands;

import org.belog.stackcalculator.commandfactory.Command;
import org.belog.stackcalculator.exceptions.NotEnoughElementsException;
import org.belog.stackcalculator.exceptions.NullStackException;
import org.belog.stackcalculator.exceptions.RootFromNegativeException;

import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class Sqrt extends Command {

    private static final Logger logger = Logger.getLogger(Sqrt.class.getName());

    @Override
    public void execute(HashMap<String, Double> map, Stack<Double> stack, String parameters) {

        logger.info("Sqrt > execute > BEGIN");

        if (stack == null) {

            logger.error("Sqrt > execute > STACK IS NOT INITIALIZED");
            throw new NullStackException("STACK IS NOT INITIALIZED");
        }

        if (stack.size() < 1) {

            logger.error("Sqrt > execute > LESS THAN 1 ELEMENT IN STACK");
            throw new NotEnoughElementsException("LESS THAN 1 ELEMENT IN STACK");
        }

        Double value = stack.lastElement();
        stack.pop();

        if (value < 0) {

            logger.error("Sqrt > execute > IT IS IMPOSSIBLE TO EXTRACT ROOT FROM A NEGATIVE NUMBER");
            throw new RootFromNegativeException("IT IS IMPOSSIBLE TO EXTRACT ROOT FROM A NEGATIVE NUMBER");
        }

        Double result = Math.sqrt(value);

        logger.info("Sqrt > execute > operation result: SQRT(" + value.toString() + ") = " + result.toString());

        stack.push(result);

        logger.info("Sqrt > execute > END");
    }
}