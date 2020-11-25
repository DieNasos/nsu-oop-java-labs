import org.belog.stackcalculator.commandfactory.commands.*;

import org.junit.*;
import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class TestMinus {

    private static final Logger testLogger = Logger.getLogger(TestMinus.class.getName());

    @Test
    public void test() {

        Stack<Double> stack = new Stack<Double>();
        Double value1 = 5.0;
        Double value2 = 5.0;
        stack.push(value1);
        stack.push(value2);
        HashMap<String, Double> map = new HashMap<String, Double>();
        String parameters = new String();

        Minus minus = new Minus();
        minus.execute(map, stack, parameters);

        Double expectedResult = 0.0;

        if (!stack.lastElement().equals(expectedResult)) {
            testLogger.error("TestMinus > test > WORKS INCORRECT :: EXPECTED: " + expectedResult.toString() + ", ACTUAL: " + stack.lastElement().toString());
        }
        else {
            testLogger.info("TestMinus > test > works correct :: expected: " + expectedResult.toString() + ", actual: " + stack.lastElement().toString());
        }

        Assert.assertEquals(expectedResult, stack.lastElement());
    }
}