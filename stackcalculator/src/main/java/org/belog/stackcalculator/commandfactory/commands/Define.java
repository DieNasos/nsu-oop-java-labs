package org.belog.stackcalculator.commandfactory.commands;

import org.belog.stackcalculator.commandfactory.Command;
import org.belog.stackcalculator.exceptions.NullMapException;

import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class Define extends Command {

    private static final Logger logger = Logger.getLogger(Define.class.getName());

    @Override
    public void execute(HashMap<String, Double> map, Stack<Double> stack, String parameters) throws NullMapException {
        logger.info("Define > execute > BEGIN");

        String variableName = "";
        String variableValueStr = "";
        int currentSymbolIndex = 0;
        Double variableValue;

        while (!Character.isWhitespace(parameters.charAt(currentSymbolIndex))) {
            variableName += parameters.charAt(currentSymbolIndex);
            currentSymbolIndex++;
        }

        for (int i = currentSymbolIndex + 1; i < parameters.length(); i++) {
            variableValueStr += parameters.charAt(i);
        }

        logger.info("Define > execute > variableName = " + variableName);
        logger.info("Define > execute > variableValueStr = " + variableValueStr);

        variableValue = Double.parseDouble(variableValueStr);

        if (map == null) {
           logger.error("Define > execute > VARIABLES MAP IS NOT INITIALIZED");
           throw new NullMapException("VARIABLES MAP IS NOT INITIALIZED");
        }

        map.put(variableName, variableValue);
        logger.info("Define > execute > END");
    }
}