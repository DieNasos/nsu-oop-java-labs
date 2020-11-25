package org.belog.factoryemulator.storages;

import java.util.LinkedList;
import java.util.Observable;
import org.apache.log4j.Logger;

import org.belog.factoryemulator.products.Product;

public class Storage<T extends Product> extends Observable {
    // logger
    private static final Logger logger = Logger.getLogger(Storage.class.getName());

    // content
    private LinkedList<T> products;

    // storage parameters
    private int storageSize;
    private String storageType;

    public Storage(String type, int size) {
        // creating storage
        storageSize = size;
        storageType = type;
        products = new LinkedList<>();

        logger.info(storageType + " :: CREATED");
    }

    public synchronized void put(T product) throws InterruptedException {
        // putting product in storage

        while (products.size() >= storageSize) {
            // storage is full
            logger.info(storageType + " :: STORAGE IS FULL");
            wait(); // waiting until somebody gets last product from us
        }

        products.add(product);   // adding product
        logger.info(storageType + " :: GOT PRODUCT " + product.getID() + ", FILLED: " + products.size() + "/" + storageSize);
        notify();   // we got new product and ready to give it

        // sending amount of products to controller (if there is one)
        this.setChanged();
        this.notifyObservers(products.size());
        logger.info(storageType + " :: NOTIFYING CONTROLLER :: (" + products.size() + " PRODUCTS IN STORAGE)");
    }

    public synchronized T get() throws InterruptedException {
        while (true) {
            if (!products.isEmpty()) {
                // taking last product...
                T product = products.getLast();

                // ...removing it from storage...
                products.pop();
                notify();   // storage is not full anymore (if it was)

                // ...and returning
                logger.info(storageType + " :: REMOVING PRODUCT " + product.getID() + ", FILLED: " + products.size() + "/" + storageSize);

                // sending amount of products to controller (if there is one)
                this.setChanged();
                this.notifyObservers(products.size());
                logger.info(storageType + " :: NOTIFYING CONTROLLER :: (" + products.size() + " PRODUCTS IN STORAGE)");

                return product;
            } else {
                wait(); // waiting for pack to give it
            }
        }
    }

    public int getStorageSize() { return storageSize; }
}