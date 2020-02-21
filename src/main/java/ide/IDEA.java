package ide;

import testRunner.TestRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class IDEA {
    public List<TestRunner> getAllMethodsByClass(Class testClass) {
        List<TestRunner> allTests = new ArrayList<>();
        Method[] methods = testClass.getDeclaredMethods();
        for (Method method : methods) {
            allTests.add(new TestRunner(method));
        }
        return allTests;
    }
}
