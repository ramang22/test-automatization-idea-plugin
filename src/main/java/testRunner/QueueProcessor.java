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
            callBack.onFinishPrioritization(tests);
        }
    }

}
