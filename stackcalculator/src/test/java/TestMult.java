import org.belog.stackcalculator.commandfactory.commands.*;

import org.junit.*;
import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class TestMult {

    private static final Logger testLogger = Logger.getLogger(TestMult.class.getName());

    @Test
    public void test() {

        Stack<Double> stack = new Stack<Double>();
        Double value1 = 5.0;
        Double value2 = 5.0;
        stack.push(value1);
        stack.push(value2);
        HashMap<String, Double> map = new HashMap<String, Double>();
        String parameters = new String();

        Mult mult = new Mult();
        mult.execute(map, stack, parameters);

        Double expectedResult = 25.0;

        if (!stack.lastElement().equals(expectedResult)) {
            testLogger.error("TestMult > test > WORKS INCORRECT :: EXPECTED: " + expectedResult.toString() + ", ACTUAL: " + stack.lastElement().toString());
        }
        else {
            testLogger.info("TestMult > test > works correct :: expected: " + expectedResult.toString() + ", actual: " + stack.lastElement().toString());
        }

        Assert.assertEquals(expectedResult, stack.lastElement());
    }
}