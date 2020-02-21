package testRunner;

import java.util.List;
import java.util.concurrent.*;

public class ParallelTestExecution implements Runnable {
    private static ExecutorService SERVICE = null;

    private BlockingQueue<TestRunner> tests = new ArrayBlockingQueue<>(1000);
    private boolean running = true;

    public ParallelTestExecution(List<TestRunner> oldTests, int maxNumberOfThreads) {
        tests.addAll(oldTests);
        SERVICE = Executors.newFixedThreadPool(maxNumberOfThreads);
    }

    public void startTestExecution() {
        for (int i = 0; i < tests.size(); i++) {
            SERVICE.submit(this);
        }
    }

    public void terminateTestImmediately() {
        this.SERVICE.shutdownNow();
        this.stop();
    }

    public void stopTesting(List<TestRunner> newTests) {
        tests.clear();
        System.out.println("Adding new tests");
        tests.addAll(newTests);
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                TestRunner test = tests.take();
                test.run();
                test.printExecutionStatus();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public BlockingQueue<TestRunner> getTests() {
        return tests;
    }

    public void setTests(List<TestRunner> tests) {
        this.tests.addAll(tests);
    }
}
