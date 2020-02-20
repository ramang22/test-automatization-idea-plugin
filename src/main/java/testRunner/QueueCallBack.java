package testRunner;

import java.util.List;

public interface QueueCallBack {
    void onFinishPrioritization(List<TestRunner> shuffledTests);
}
