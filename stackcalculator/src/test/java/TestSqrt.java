import org.belog.stackcalculator.commandfactory.commands.*;

import org.junit.*;
import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class TestSqrt {

    private static final Logger testLogger = Logger.getLogger(TestSqrt.class.getName());

    @Test
    public void test() {

        Stack<Double> stack = new Stack<Double>();
        Double value = 25.0;
        stack.push(value);
        HashMap<String, Double> map = new HashMap<String, Double>();
        String parameters = new String();

        Sqrt sqrt = new Sqrt();
        sqrt.execute(map, stack, parameters);

        Double expectedResult = 5.0;

        if (!stack.lastElement().equals(expectedResult)) {
            testLogger.error("TestSqrt > test > WORKS INCORRECT :: EXPECTED: " + expectedResult.toString() + ", ACTUAL: " + stack.lastElement().toString());
        }
        else {
            testLogger.info("TestSqrt > test > works correct :: expected: " + expectedResult.toString() + ", actual: " + stack.lastElement().toString());
        }

        Assert.assertEquals(expectedResult, stack.lastElement());
    }
}