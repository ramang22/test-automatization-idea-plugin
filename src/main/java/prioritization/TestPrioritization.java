package prioritization;

import testRunner.TestRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestPrioritization {
    public TestPrioritization() {

    }

    public List<TestRunner> prioritizeTest(List<TestRunner> tests) {
        Collections.shuffle(tests);
        for (TestRunner test : tests){
            test.result = 0;
            test.executed = 0;
        }
        return tests;
    }
}
