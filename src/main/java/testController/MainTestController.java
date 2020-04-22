package testController;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.util.PsiTreeUtil;

import highlighter.CodeHighlighter;
import opencloverController.cloverParser;
import org.json.JSONException;
import org.xml.sax.SAXException;
import pluginResources.HighlightSingleton;
import pluginResources.TestSingleton;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import mavenRunner.*;
import opencloverController.*;

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


        /*
         *       TODO Test Prioritization
         *          1. for every test check last result in db
         *          2. get failed test for priority, then passed tests
         *          3. for failed tests by test time (lower first), for passed tests do same
         */

        // run all tests
        for (String test_method : TestSingleton.getInstance().getTestsForExecution()) {
                String className = TestSingleton.getInstance().getTestClasses().get(test_method);
                testRunner.runTest(className, test_method);
        }

        // TODO clean test singleton, delete changes from last run
        TestSingleton.getInstance().getEvents().clear();
        TestSingleton.getInstance().getTestMethod_event().clear();
        TestSingleton.getInstance().getEventsForMethod().clear();

    }
}
