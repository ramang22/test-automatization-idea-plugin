package testController;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import database.DbController;
import database.TestResultDb;
import highlighter.CodeHighlighter;
import ide.PsiHandler;
import logger.PluginLogger;
import opencloverController.cloverParser;
import org.json.JSONException;
import pluginResources.HighlightSingleton;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;
import mavenRunner.*;
import opencloverController.*;
import prioritization.PrioritizationValuator;
import test.Event;

import java.util.*;

public class MainTestController {

    private final PluginLogger logger = new PluginLogger(MainTestController.class);

    public void runCoverage() {

        PluginSingleton.getInstance().getPackage_file_paths().clear();
        TestSingleton.getInstance().getTestToPsiElements().clear();

        // run mvn clover
        PsiHandler psiHandler = new PsiHandler();
        try {
            cloverRunner.runClover();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // openclover api html report
        cloverApiRunner.runHtmlReporter();

        //run init test coverage
        try {
            cloverParser.getTestCoverageWithinClasses();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // get all classes in tests
        List<PsiClass> allClassesForProject = psiHandler.getAllClasses(PluginSingleton.getInstance().getProject());
        //
        for (PsiClass c : allClassesForProject) {
            String filename = ((PsiJavaFile) c.getContainingFile()).getName().split("\\.")[0];
            String package_name = ((PsiJavaFile) c.getContainingFile()).getPackageName();
            PluginSingleton.getInstance().getPackage_file_paths().add(package_name + "/" + filename + ".js");
        }
        try {
            cloverParser.getTestCoverageWithinClasses();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (PsiClass c : allClassesForProject) {
            if (TestSingleton.getInstance().getCoverageByClass().containsKey(c.getName())) {
                psiHandler.mapLinesToTests(c, TestSingleton.getInstance().getCoverageByClass().get(c.getName()));
            }
        }
    }

    public void runAllTests() throws InterruptedException {
        //PluginSingleton.safeAllFiles();
        //copy event map into new map for execution
        HashMap<String, List<Event>> copiedEvents = new HashMap<>(TestSingleton.getInstance().getTestMethod_CustomEvent());
        TestSingleton.getInstance().setTestMethod_CustomEvent_forExecution(copiedEvents);
        TestSingleton.getInstance().getTestMethod_CustomEvent().clear();

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
                exec_value += (float) (results.get(i).getResult()) / (i + 1);
            }
            double exec_time = 999;
            if (results.size() != 0) {
                exec_time = Double.parseDouble(results.get(0).getExec_time());
            }
            priorityQue.add(new PrioritizationValuator(test_name, exec_value, exec_time));

        }
        priorityQue.sort((o1, o2) -> (int) (o1.getHistoryValue() == o2.getHistoryValue() ? o1.getTimeValue() - o2.getTimeValue() :
                o1.getHistoryValue() - o2.getHistoryValue()));

        // run all tests
        for (PrioritizationValuator test_method : priorityQue) {
            String className = TestSingleton.getInstance().getTestClasses().get(test_method.getTest_name());
            testRunner.runTest(className, test_method.getTest_name());
        }


    }

    public void runHighlighter() {
        StringBuilder toolTipText = new StringBuilder();
        toolTipText.append("Failed tests :\n");
        for (String x : HighlightSingleton.getInstance().getTests_to_highlight()) {
            toolTipText.append(x);
            toolTipText.append(" failed\n");
        }

        CodeHighlighter.removeOldHighlights();

        for (String test_name : HighlightSingleton.getInstance().getTests_to_highlight()) {
            CodeHighlighter.highlightTest(test_name, false, toolTipText.toString());
        }
        TestSingleton.getInstance().getTestMethod_CustomEvent_forExecution().clear();
        HighlightSingleton.getInstance().getTests_to_highlight().clear();
    }

}
