package testController;

import com.intellij.psi.PsiElement;
import highlighter.CodeHighlighter;
import opencloverController.cloverParser;
import org.json.JSONException;
import pluginResources.TestSingleton;
import mavenRunner.*;
import opencloverController.*;

import java.util.HashMap;
import java.util.List;

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

        // run all tests
        for (String test_method : TestSingleton.getInstance().getTestsForExecution()) {
                String className = TestSingleton.getInstance().getTestClasses().get(test_method);
            System.out.println(className);
                testRunner.runTest(className, test_method);
        }

        // TODO clean test singleton, delete changes from last run
        TestSingleton.getInstance().getTestMethod_event_forExecution().clear();
    }
}
