package org.belog.factoryemulator.roles;

import org.apache.log4j.Logger;

import org.belog.factoryemulator.Factory;
import org.belog.factoryemulator.products.Product;
import org.belog.factoryemulator.storages.Storage;

public class Supplier <T extends Product> implements Runnable {
    // logger
    private static final Logger logger = Logger.getLogger(Supplier.class.getName());

    private Factory factory;    // factory that we work for
    private Storage storage;    // where will we send details to

    // detail parameters
    private Class detailClass;
    private String detailType;
    private int detailPrice;    // price for detail that we supply to factory

    // supplier parameters
    private String supplierType;
    private int supplierID;
    private int suppliesCount = 0;
    private int sleepTimeMs;

    public Supplier (Factory f, String supplierType, int ID, String detailType, Class detailClass, Storage s, int sleepTimeMs, int detailPrice) {
        factory = f;
        storage = s;

        this.detailClass = detailClass;
        this.detailType = detailType;
        this.detailPrice = detailPrice;

        this.supplierType = supplierType;
        supplierID = ID;
        this.sleepTimeMs = sleepTimeMs;

        logger.info(supplierType + " " + supplierID + " :: CREATED");
    }

    @Override
    public void run() {
        logger.info(supplierType + " " + supplierID + " :: START WORKING");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                // creating detail
                logger.info(supplierType + " " + supplierID + " :: PREPARING DETAIL FOR SUPPLY");
                suppliesCount++;
                String supplyID = generateSupplyID();
                T detail = (T) detailClass.getDeclaredConstructor(String.class).newInstance(detailType + "_" + supplyID);

                // putting detail in storage
                logger.info(supplierType + " " + supplierID + " :: SENDING DETAIL TO STORAGE");
                storage.put(detail);
                logger.info(supplierType + " " + supplierID + " :: DETAIL SENDED");

                // taking money for detail
                factory.getMoney(detailPrice);

                // getting some rest
                Thread.sleep(sleepTimeMs);
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();

                // finishing
                Thread.currentThread().interrupt();
            }
        }

        logger.info(supplierType + " " + supplierID + " :: FINISHED WORKING");
    }

    private String generateSupplyID() { return supplierID + "_" + suppliesCount; }
}