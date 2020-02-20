package testRunner;

import prioritization.TestPrioritization;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueueProcessor implements Runnable {
    List<TestRunner> tests = new ArrayList<>();
    TestPrioritization prioritizer = new TestPrioritization();
    ExecutorService service = Executors.newSingleThreadExecutor();
    private boolean running = true;
    QueueCallBack callBack;

    public QueueProcessor(List<TestRunner> x, QueueCallBack callBack) {
        tests = x;
        this.callBack = callBack;

    }

    public void executeQueueManager() {
        service.execute(this);
    }

    public void terminateService(){
        service.shutdownNow();
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            tests = prioritizer.prioritizeTest(tests);
            try {
                System.out.println("Test prioritizer is going to sleep");
                Thread.sleep(1000);
                System.out.println("Test prioritizer wake up");
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            callBack.onFinishPrioritization(tests);

        }
    }

}
