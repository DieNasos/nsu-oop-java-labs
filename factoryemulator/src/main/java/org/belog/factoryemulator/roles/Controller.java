package org.belog.factoryemulator.roles;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import org.belog.factoryemulator.Factory;
import org.belog.factoryemulator.products.Car;
import org.belog.factoryemulator.products.details.AccessoriesPack;
import org.belog.factoryemulator.products.details.Body;
import org.belog.factoryemulator.products.details.Engine;
import org.belog.factoryemulator.storages.Storage;

public class Controller implements Runnable, Observer {
    // logger
    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    // factory
    private Factory factory;

    // details storages
    private Storage<AccessoriesPack> as;
    private Storage<Body> bs;
    private Storage<Engine> es;

    // cars storage
    private Storage<Car> carsStorage;
    private int storageSize;
    private int currNumCars;

    // thread-pool
    private ThreadPoolExecutor tp;

    // workers parameters
    private int workersSalary;

    public Controller(Factory f, Storage<AccessoriesPack> accessoriesStorage, Storage<Body> bodiesStorage, Storage<Engine> enginesStorage, Storage<Car> carsStorage, int numPools, int workersSalary) {
        factory = f;
        this.carsStorage = carsStorage;
        storageSize = carsStorage.getStorageSize();

        // subscribing on cars storage
        carsStorage.addObserver(this);

        logger.info("CONTROLLER :: SUBSCRIBED ON CARS STORAGE");

        // setting details-storages
        as = accessoriesStorage;
        bs = bodiesStorage;
        es = enginesStorage;

        // creating thread-pool
        tp = (ThreadPoolExecutor) Executors.newFixedThreadPool(numPools);

        // setting workers parameters
        this.workersSalary = workersSalary;

        logger.info("CONTROLLER :: CREATED");
    }

    @Override
    public void run() {
        logger.info("CONTROLLER :: START WORKING");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                logger.info("CONTROLLER :: CREATING " + (storageSize - currNumCars - tp.getActiveCount() - tp.getQueue().size()) + " WORKERS");

                for (int i = 0; i < (storageSize - currNumCars - tp.getActiveCount() - tp.getQueue().size()); i++) {
                    tp.execute(new Worker(factory, i, as, bs, es, carsStorage, workersSalary));
                }

                logger.info("CONTROLLER :: " + (storageSize - currNumCars) + " TASKS EXECUTED");

                // getting some rest
                synchronized (this) {
                    logger.info("CONTROLLER :: WAITING FOR NUMBER OF CARS IN STORAGE...");
                    wait(); // waiting for number of cars in cars storage
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                e.printStackTrace();

                // finishing
                tp.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        logger.info("CONTROLLER :: FINISHED WORKING");
    }

    @Override
    public void update(Observable o, Object arg) {
        currNumCars = (Integer) arg;
        logger.info("CONTROLLER :: NUMBER OF CARS UPDATED (" + currNumCars + ")");

        synchronized (this) {
            notify(); // got number of cars in storage
        }
    }
}