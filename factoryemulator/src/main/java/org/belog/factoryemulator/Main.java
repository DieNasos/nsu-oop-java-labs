package org.belog.factoryemulator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Factory factory = new Factory();
            factory.start();

            // waiting for some input
            Scanner in = new Scanner(System.in);
            String str = in.nextLine();

            factory.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}