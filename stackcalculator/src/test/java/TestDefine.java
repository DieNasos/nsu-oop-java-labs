import org.belog.stackcalculator.commandfactory.commands.*;

import org.junit.*;
import java.util.HashMap;
import java.util.Stack;
import org.apache.log4j.Logger;

public class TestDefine {

    private static final Logger testLogger = Logger.getLogger(TestDefine.class.getName());

    @Test
    public void test() {

        Stack<Double> stack = new Stack<Double>();
        HashMap<String, Double> map = new HashMap<String, Double>();
        String parameters = "variable 5.0";

        Define define = new Define();
        define.execute(map, stack, parameters);

        Double expectedResult = 5.0;

        if (!map.get("variable").equals(expectedResult)) {
            testLogger.error("TestDefine > test > WORKS INCORRECT :: EXPECTED: " + expectedResult.toString() + ", ACTUAL: " + map.get("variable").toString());
        }
        else {
            testLogger.info("TestDefine > test > works correct :: expected: " + expectedResult.toString() + ", actual: " + map.get("variable").toString());
        }

        Assert.assertEquals(expectedResult, map.get("variable"));
    }
}