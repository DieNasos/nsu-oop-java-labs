import org.belog.stackcalculator.commandfactory.commands.*;

import org.junit.*;
import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class TestPush {

    private static final Logger testLogger = Logger.getLogger(TestPush.class.getName());

    @Test
    public void test() {

        Stack<Double> stack = new Stack<Double>();
        HashMap<String, Double> map = new HashMap<String, Double>();
        Double value = 5.0;
        map.put("value", value);
        String parameters = "value";

        Push push = new Push();
        push.execute(map, stack, parameters);

        Double expectedResult = 5.0;

        if (!stack.lastElement().equals(expectedResult)) {
            testLogger.error("TestPush > test > WORKS INCORRECT :: EXPECTED: " + expectedResult.toString() + ", ACTUAL: " + stack.lastElement().toString());
        }
        else {
            testLogger.info("TestPush > test > works correct :: expected: " + expectedResult.toString() + ", actual: " + stack.lastElement().toString());
        }

        Assert.assertEquals(expectedResult, stack.lastElement());
    }
}