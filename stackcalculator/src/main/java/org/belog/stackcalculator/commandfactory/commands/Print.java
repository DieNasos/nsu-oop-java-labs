package org.belog.stackcalculator.commandfactory.commands;

import org.belog.stackcalculator.commandfactory.Command;

import org.belog.stackcalculator.exceptions.NullStackException;

import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class Print extends Command {

    private static final Logger logger = Logger.getLogger(Print.class.getName());

    @Override
    public void execute(HashMap<String, Double> map, Stack<Double> stack, String parameters) {

        logger.info("Print > execute > BEGIN");

        if (stack == null) {

            logger.error("Print > execute > STACK IS NOT INITIALIZED");
            throw new NullStackException("STACK IS NOT INITIALIZED");
        }

        System.out.println(stack.lastElement());

        logger.info("Print > execute > END");
    }
}