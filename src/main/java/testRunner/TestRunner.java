package testRunner;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import test.UnitTests;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

public class TestRunner implements Runnable {
    public Method name;
    public long result;
    public int executed;

    public TestRunner(Method testName) {
        name = testName;
        result = 0;
        executed = 0;
    }

    public TestRunner(){
        result = 0;
        executed = 0;

    }

    @Override
    public void run() {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        //selectPackage("test"),
                        //selectClass(FirstUnitTest.class),
                        selectMethod("tests.Tests#add")
                )
                .build();
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        TestExecutionSummary summary = listener.getSummary();
        result = summary.getTestsSucceededCount();
        System.out.println(result);
        executed = 1;
    }

    public void printExecutionStatus(){
        StringBuilder msg = new StringBuilder();
        if (this.executed == 1){
            msg.append("EXECUTED, ");
        }else if  (this.executed == 0){
            msg.append("NOT EXECUTED ");
        }else {
            msg.append("FAILED EXECUTION ");
        }
        msg.append("Test : ").append(name).append(" ,EXECUTION STATUS -> ");
        if (this.result == 1){
            msg.append("SUCCEED ");
        }else if (this.result == 0){
            msg.append("FAIL ");
        }else {
            msg.append("FAILED EXECUTION");
        }
        System.out.println(msg.toString());
    }

}
