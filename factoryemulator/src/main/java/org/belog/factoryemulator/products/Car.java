package org.belog.factoryemulator.products;

import org.belog.factoryemulator.products.details.AccessoriesPack;
import org.belog.factoryemulator.products.details.Body;
import org.belog.factoryemulator.products.details.Engine;

public class Car extends Product {
    // details
    private AccessoriesPack accessoriesPack;
    private Body body;
    private Engine engine;

    public Car(Body b, Engine e, AccessoriesPack ap, String ID) {
        super(ID);  // setting product's ID

        accessoriesPack = ap;
        body = b;
        engine = e;
    }

    // details IDs getters
    public String getAccessoriesPackID() { return accessoriesPack.getID(); }
    public String getBodyID() { return body.getID(); }
    public String getEngineID() { return engine.getID(); }
}