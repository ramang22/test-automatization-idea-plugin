package ide;

import org.junit.jupiter.api.Test;
import testRunner.ParallelTestExecution;
import testRunner.QueueProcessor;
import testRunner.TestRunner;

import java.util.ArrayList;
import java.util.List;

public class Tester {
    List<TestRunner> tests = new ArrayList<>();

    public Tester() {

    }

    public Tester(List<TestRunner> tests) {
        this.tests = tests;
    }

    public void runTests() throws InterruptedException {
        ParallelTestExecution executor = new ParallelTestExecution(this.tests, 2);
        executor.startTestExecution();
        QueueProcessor queueMaster = new QueueProcessor(this.tests, executor::stopTesting);
        queueMaster.executeQueueManager();
        Thread.sleep(1000);
        executor.terminateTestImmediately();
        queueMaster.terminateService();
    }

    public List<TestRunner> getTests() {
        return tests;
    }

    public void setTests(List<TestRunner> tests) {
        this.tests = tests;
    }
}
