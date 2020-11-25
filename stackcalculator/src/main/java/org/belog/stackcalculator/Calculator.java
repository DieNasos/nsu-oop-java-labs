package org.belog.stackcalculator;

import org.belog.stackcalculator.commandfactory.Command;
import org.belog.stackcalculator.commandfactory.CommandFactory;
import org.belog.stackcalculator.exceptions.NoCommandException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class Calculator {

    private static final Logger logger = Logger.getLogger(Calculator.class.getName());

    private HashMap<String, Double> map;
    private Stack<Double> stack;

    public Calculator() {
        logger.info("Calculator > Calculator > BEGIN");
        stack = new Stack<Double>();
        map = new HashMap<String, Double>();
        logger.info("Calculator > Calculator > END");
    }

    private void executeCommand(String commandLine) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, NoSuchMethodException {
        logger.info("Calculator > executeCommand > BEGIN :: commandLine = " + commandLine);

        String commandName = "";
        String commandParameters = "";
        int currentSymbolIndex = 0;

        while ((currentSymbolIndex < commandLine.length()) && !Character.isWhitespace(commandLine.charAt(currentSymbolIndex))) {
            commandName += commandLine.charAt(currentSymbolIndex);
            currentSymbolIndex++;
        }

        logger.info("Calculator > executeCommand > commandName = " + commandName);

        if (commandLine.length() > 1) {
            for (int i = currentSymbolIndex + 1; i < commandLine.length(); i++) {
                commandParameters += commandLine.charAt(i);
            }
        }

        logger.info("Calculator > executeCommand > commandParameters = " + commandParameters);

        logger.info("Calculator > executeCommand > creating new '" + commandName + "' command");
        Command command = CommandFactory.getInstance().createCommand(commandName);
        logger.info("Calculator > executeCommand > new '" + commandName + "' command was created");

        command.execute(map, stack, commandParameters);

        logger.info("Calculator > executeCommand > END");
    }

    public void calculate(String commandsFileName) {

        logger.info("Calculator > calculate > BEGIN");

        try (BufferedReader commandsFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(commandsFileName)))) {

            String commandLine;
            while ((commandLine = commandsFileReader.readLine()) != null) {
                executeCommand(commandLine);
            }
        }
        catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            logger.error(exception.getMessage());
        }

        logger.info("Calculator > calculate > END");
    }
}