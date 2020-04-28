package testController;

import com.intellij.psi.PsiElement;
import database.DbController;
import database.TestResultDb;
import highlighter.CodeHighlighter;
import opencloverController.cloverParser;
import org.json.JSONException;
import pluginResources.TestSingleton;
import mavenRunner.*;
import opencloverController.*;
import prioritization.PrioritizationValuator;

import java.util.*;

public class MainTestController {


    public void runCoverage() {
        // run mvn clover
        try {
            cloverRunner.runClover();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // openclover api html report
        cloverApiRunner.runHtmlReporter();

        // run init test coverage
        try {
            cloverParser.getTestCoverageWithinClasses();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void runAllTests() throws InterruptedException {

        // https://www.programcreek.com/java-api-examples/?class=com.intellij.psi.PsiFile&method=findElementAt

        // TODO FIX clean all previous highlights
        CodeHighlighter.removeOldHighlights();

        //copy event map into new map for execution
        HashMap<String, List<PsiElement>> copiedEvents = new HashMap<>(TestSingleton.getInstance().getTestMethod_event());
        TestSingleton.getInstance().setTestMethod_event_forExecution(copiedEvents);
        TestSingleton.getInstance().getTestMethod_event().clear();
        /*
         *       TODO Test Prioritization
         *          1. for every test check last result in db
         *          2. get failed test for priority, then passed tests
         *          3. for failed tests by test time (lower first), for passed tests do same
         */
        HashSet<String> testMethods = TestSingleton.getInstance().getTestsForExecution();
        DbController db_controller = new DbController();
        HashMap<String, List<TestResultDb>> test_db_result = new HashMap<>();
        int i = 0;
        for (String test : testMethods) {
            test_db_result.put(test, db_controller.getAllTestResultsByName(test));
        }

        List<PrioritizationValuator> priorityQue = new ArrayList<>();
        for (Map.Entry<String, List<TestResultDb>> entry : test_db_result.entrySet()) {
            String test_name = entry.getKey();
            List<TestResultDb> results = entry.getValue();
            double exec_value = 0;
            for (i = 0; i < results.size(); i++) {
                exec_value += (float) results.get(i).getResult() / i+1;
            }
            double exec_time = Double.parseDouble(results.get(0).getExec_time());
            priorityQue.add(new PrioritizationValuator(test_name, exec_value, exec_time));

        }
        priorityQue.sort((o1, o2) -> {
            double t1_value = o1.getHistoryValue();
            double t2_value = o2.getHistoryValue();
            if (t1_value > t2_value) {
                return 1;
            } else if (t1_value < t2_value) {
                return -1;
            } else {
                double t1_time = o1.getTimeValue();
                double t2_time = o1.getTimeValue();
                if (t1_time > t2_time) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        // run all tests
        for (PrioritizationValuator test_method : priorityQue) {
            String className = TestSingleton.getInstance().getTestClasses().get(test_method.getTest_name());
            System.out.println(className);
            testRunner.runTest(className, test_method.getTest_name());
        }

        // TODO clean test singleton, delete changes from last run
        TestSingleton.getInstance().getTestMethod_event_forExecution().clear();
    }
}
