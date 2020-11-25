package org.belog.stackcalculator.commandfactory;

import org.belog.stackcalculator.exceptions.NoCommandException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class CommandFactory {

    private static CommandFactory instance = null;
    private static final Logger logger = Logger.getLogger(CommandFactory.class.getName());
    private Properties properties;

    private CommandFactory() throws IOException {

        File file = new File("src\\main\\resources\\configurations.properties");

        if (!file.exists()) {
            logger.error("CommandFactory > CommandFactory > CONFIGURATIONS FILE WAS NOT FOUND");
        }

        properties = new Properties();
        properties.load(new FileReader(file));

        if (properties.isEmpty()) {
            logger.error("CommandFactory > CommandFactory > CONFIGURATIONS FILE IS EMPTY");
        }
    }

    public static CommandFactory getInstance() throws IOException {
        CommandFactory localInstance = instance;

        if (localInstance == null) {
            synchronized (CommandFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CommandFactory();
                }
            }
        }

        return localInstance;
    }

    public Command createCommand(String commandName) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        logger.info("CommandFactory > createCommand > BEGIN");

        String commandClassName = properties.getProperty(commandName);

        if (commandClassName == null) {
            logger.error("CommandFactory > createCommand > NO SUCH A COMMAND :: " + commandName);
            throw new NoCommandException("NO SUCH A COMMAND :: " + commandName);
        }

        logger.info("CommandFactory > createCommand > commandClassName = " + commandClassName);

        Class<Command> commandClass = (Class<Command>) Class.forName(commandClassName);
        Constructor<Command> commandConstructor = commandClass.getDeclaredConstructor();
        Command command = commandConstructor.newInstance();

        logger.info("CommandFactory > createCommand > END :: returning command");

        return command;
    }
}