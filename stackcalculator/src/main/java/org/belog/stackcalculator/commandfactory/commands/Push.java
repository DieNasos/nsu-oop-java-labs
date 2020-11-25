package org.belog.stackcalculator.commandfactory.commands;

import org.belog.stackcalculator.commandfactory.Command;

import org.belog.stackcalculator.exceptions.NullStackException;

import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class Push extends Command {

    private static final Logger logger = Logger.getLogger(Push.class.getName());

    @Override
    public void execute(HashMap<String, Double> map, Stack<Double> stack, String parameters) {

        logger.info("Push > execute > BEGIN");

        if (stack == null) {

            logger.error("Push > execute > STACK IS NOT INITIALIZED");
            throw new NullStackException("STACK IS NOT INITIALIZED");
        }

        stack.push(map.get(parameters));

        logger.info("Push > execute > END");
    }
}