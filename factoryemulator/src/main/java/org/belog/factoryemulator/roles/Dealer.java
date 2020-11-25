package org.belog.factoryemulator.roles;

import org.apache.log4j.Logger;

import org.belog.factoryemulator.Factory;
import org.belog.factoryemulator.products.Car;
import org.belog.factoryemulator.storages.Storage;

public class Dealer implements Runnable {
    // logger
    private static final Logger logger = Logger.getLogger(Dealer.class.getName());

    private Factory factory;    // factory that we work for
    private Storage cs; // cars storage
    private int carPrice;   // money that we'll send to factory per 1 car

    // dealer parameters
    private int dealerID;
    private int sleepTimeMs;

    public Dealer(Factory factory, int ID, Storage carsStorage, int sleepTimeMs, int carPrice) {
        this.factory = factory;
        cs = carsStorage;
        this.carPrice = carPrice;

        dealerID = ID;
        this.sleepTimeMs = sleepTimeMs;

        logger.info("DEALER " + dealerID + " :: CREATED");
    }

    @Override
    public void run() {
        logger.info("DEALER " + dealerID + " :: START WORKING");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                // taking car from storage and getting info about it
                logger.info("DEALER " + dealerID + " :: GETTING CAR...");
                Car car = (Car) cs.get();
                logger.info("DEALER " + dealerID + " :: GOT CAR " + car.getID() + ", DETAILS: " + car.getEngineID() + ", " + car.getBodyID() + ", " + car.getAccessoriesPackID());

                // selling car
                factory.giveMoney(carPrice);
                logger.info("DEALER " + dealerID + " :: CAR " + car.getID() + " SELLED FOR " + carPrice);

                // getting some rest
                Thread.sleep(sleepTimeMs);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                e.printStackTrace();

                // finishing
                Thread.currentThread().interrupt();
            }
        }

        logger.info("DEALER " + dealerID + " :: FINISHED WORKING");
    }
}