package org.belog.stackcalculator;

import org.apache.log4j.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("Main > main > BEGIN");

        try {
            logger.info("Main > main > creating new Calculator");
            Calculator calculator = new Calculator();
            logger.info("Main > main > new Calculator was created");

            calculator.calculate("src\\main\\resources\\commands.txt");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        logger.info("Main > main > END");
    }
}