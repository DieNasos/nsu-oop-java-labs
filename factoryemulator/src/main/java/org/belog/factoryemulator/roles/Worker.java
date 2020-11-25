package org.belog.factoryemulator.roles;

import org.apache.log4j.Logger;

import org.belog.factoryemulator.Factory;
import org.belog.factoryemulator.products.Car;
import org.belog.factoryemulator.products.details.AccessoriesPack;
import org.belog.factoryemulator.products.details.Body;
import org.belog.factoryemulator.products.details.Engine;
import org.belog.factoryemulator.storages.*;

public class Worker implements Runnable {
    // logger
    private static final Logger logger = Logger.getLogger(Worker.class.getName());

    // factory that we work for
    private Factory factory;

    // storages
    private Storage<AccessoriesPack> as;
    private Storage<Body> bs;
    private Storage<Engine> es;
    private Storage<Car> cs;

    // worker parameters
    private int workerID;
    private int salary; // money that we get for created car
    private int madeCarsCount = 0;

    public Worker (Factory f, int ID, Storage<AccessoriesPack> as, Storage<Body> bs, Storage<Engine> es, Storage<Car> cs, int salary) {
        factory = f;

        // setting storages
        this.as = as;
        this.bs = bs;
        this.es = es;
        this.cs = cs;

        workerID = ID;
        this.salary = salary;

        logger.info("WORKER " + workerID + " :: CREATED");
    }

    @Override
    public void run() {
        logger.info("WORKER " + workerID + " :: START WORKING");

        // task (runs ONCE!)
        try {
            // getting details from storages
            logger.info("WORKER " + workerID + " :: GETTING DETAILS FROM STORAGES");
            AccessoriesPack accessoriesPack = as.get();
            Body body = bs.get();
            Engine engine = es.get();
            logger.info("WORKER " + workerID + " :: GOT ALL DETAILS FROM STORAGES");

            // creating car and putting it in cars storage
            Car car = createCar(body, engine, accessoriesPack);
            cs.put(car);
            logger.info("WORKER " + workerID + " :: PUTTED CREATED CAR IN STORAGE");

            // taking our salary
            factory.getMoney(salary);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();

            // finishing
            Thread.currentThread().interrupt();
        }

        logger.info("WORKER " + workerID + " :: FINISHED WORKING");
    }

    private Car createCar(Body b, Engine e, AccessoriesPack ap) {
        // creating and returning new car
        madeCarsCount++;    // + 1 car made by us
        String carID = generateNewCarID();
        logger.info("WORKER " + workerID + " :: CREATING CAR " + carID);
        return new Car(b, e, ap, carID);
    }

    private String generateNewCarID() { return "CAR_" + workerID + "_" + madeCarsCount; }
}