package org.belog.factoryemulator;

import java.util.LinkedList;
import java.util.Properties;
import org.apache.log4j.Logger;

import org.belog.factoryemulator.products.Car;
import org.belog.factoryemulator.products.details.AccessoriesPack;
import org.belog.factoryemulator.products.details.Body;
import org.belog.factoryemulator.products.details.Engine;
import org.belog.factoryemulator.roles.Controller;
import org.belog.factoryemulator.roles.Dealer;
import org.belog.factoryemulator.roles.Supplier;
import org.belog.factoryemulator.storages.*;

public class Factory {
    private static final Logger logger = Logger.getLogger(Factory.class.getName());
    private Properties properties;

    private LinkedList<Thread> threads;
    private LinkedList<Storage> storages;

    private int currentBalance;

    public Factory() {
        try {
            // loading properties
            properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/configurations.properties"));

            // creating list for threads
            threads = new LinkedList<>();

            // creating list for storages
            storages = new LinkedList<>();

            // getting moneys at start
            currentBalance = Integer.parseInt(properties.getProperty("MONEY_AT_START"));

            logger.info("FACTORY :: FACTORY CREATED");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            logger.info("FACTORY :: START WORKING");
            createStorages();
            createController();
            createSuppliers();
            createDealers();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            logger.info("FACTORY :: STOPPING WORKING");
            logger.info("TOTAL BALANCE: " + currentBalance);

            for (Thread thread : threads) {
                // stopping threads
                thread.interrupt();
                thread.join();
            }

            logger.info("FACTORY :: FINISHED WORKING");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void createStorages() {
        // creating storages
        logger.info("FACTORY :: CREATING STORAGES...");
        storages.add(new Storage<AccessoriesPack>("ACCESSORIES_STORAGE", Integer.parseInt(properties.getProperty("ACCESSORIES_STORAGE_SIZE"))));
        storages.add(new Storage<Body>("BODIES_STORAGE", Integer.parseInt(properties.getProperty("BODIES_STORAGE_SIZE"))));
        storages.add(new Storage<Engine>("ENGINES_STORAGE", Integer.parseInt(properties.getProperty("ENGINES_STORAGE_SIZE"))));
        storages.add(new Storage<Car>("CARS_STORAGE", Integer.parseInt(properties.getProperty("CARS_STORAGE_SIZE"))));
    }

    private void createController() {
        try {
            // creating controller
            logger.info("FACTORY :: CREATING CONTROLLER...");

            threads.add(new Thread(
                        new Controller(
                                this, storages.get(0), storages.get(1), storages.get(2), storages.get(3),
                                Integer.parseInt(properties.getProperty("NUMBER_OF_POOLS")),
                                Integer.parseInt(properties.getProperty("WORKERS_SALARY")))));

            threads.getLast().start();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void createSuppliers() {
        try {
            // creating suppliers
            logger.info("FACTORY :: CREATING SUPPLIERS...");

            int number_of_accessories_suppliers = Integer.parseInt(properties.getProperty("NUMBER_OF_ACCESSORIES_SUPPLIERS"));
            int current_supplier_ID = 0;

            while (current_supplier_ID < number_of_accessories_suppliers) {
                threads.add(new Thread(
                        new Supplier<AccessoriesPack>(
                                this, "ACCESSORIES_PACKS_SUPPLIER", current_supplier_ID,
                                "AP", AccessoriesPack.class, storages.get(0),
                                Integer.parseInt(properties.getProperty("SUPPLIERS_SLEEP_TIME_MS")),
                                Integer.parseInt(properties.getProperty("DETAIL_PRICE")))));

                threads.getLast().start();
                current_supplier_ID++;
            }

            threads.add(new Thread(
                    new Supplier<Body>(
                            this, "BODIES_SUPPLIER", current_supplier_ID,
                            "BODY", Body.class, storages.get(1),
                            Integer.parseInt(properties.getProperty("SUPPLIERS_SLEEP_TIME_MS")),
                            Integer.parseInt(properties.getProperty("DETAIL_PRICE")))));
            threads.getLast().start();

            threads.add(new Thread(
                    new Supplier<Engine>(
                            this, "ENGINES_SUPPLIER", current_supplier_ID + 1,
                            "ENGINE", Engine.class, storages.get(2),
                            Integer.parseInt(properties.getProperty("SUPPLIERS_SLEEP_TIME_MS")),
                            Integer.parseInt(properties.getProperty("DETAIL_PRICE")))));
            threads.getLast().start();

            logger.info("FACTORY :: SUPPLIERS CREATED");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void createDealers() {
        try {
            // creating dealers
            logger.info("FACTORY :: CREATING DEALERS...");

            int number_of_dealers = Integer.parseInt(properties.getProperty("NUMBER_OF_DEALERS"));
            int current_dealer_ID = 0;

            while (current_dealer_ID < number_of_dealers) {
                threads.add(new Thread(
                        new Dealer(
                                this, current_dealer_ID, storages.get(3),
                                Integer.parseInt(properties.getProperty("DEALERS_SLEEP_TIME_MS")),
                                Integer.parseInt(properties.getProperty("CAR_PRICE")))));

                threads.getLast().start();
                current_dealer_ID++;
            }

            logger.info("FACTORY :: DEALERS CREATED");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void getMoney(int money) {
        this.currentBalance -= money;
        printBalance();
    }

    public void giveMoney(int money) {
        this.currentBalance += money;
        printBalance();
    }

    private void printBalance() { logger.info("FACTORY :: CURRENT BALANCE: " + currentBalance); }
}