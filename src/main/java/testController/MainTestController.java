package testController;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import database.DbController;
import database.TestResultDb;
import highlighter.CodeHighlighter;
import ide.PsiHandler;
import logger.PluginLogger;
import opencloverController.CloverParser;
import org.json.JSONException;
import pluginResources.HighlightSingleton;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;
import mavenRunner.*;
import opencloverController.*;
import prioritization.PrioritizationValuator;
import test.Event;
import test.Test;

import java.sql.SQLException;
import java.util.*;

public class MainTestController {
    /**
     * instatnce of logger
     */
    private final PluginLogger logger = new PluginLogger(MainTestController.class);

    /**
     * method for re run coverage button
     */
    public void runCoverage() {

        PluginSingleton.getInstance().getPackage_file_paths().clear();
        TestSingleton.getInstance().getTestMethod_CustomEvent().clear();
        TestSingleton.getInstance().getTestMethod_CustomEvent_forExecution().clear();
        TestSingleton.getInstance().getTestClasses().clear();

        PsiHandler psiHandler = new PsiHandler();

        DbController db_controller = new DbController();

        try {
            db_controller.checkIfDbExists();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        List<PsiMethod> methods = psiHandler.getAllTests(PluginSingleton.getInstance().getProject());

        for (PsiMethod testMethod : methods) {
            // check if test is in db
            db_controller.addTestToDb(testMethod.getName());
            HashSet<PsiMethod> a = psiHandler.traverseBodyToFindAllMethodUsages(testMethod.getBody());
            Test test = new Test(testMethod, Objects.requireNonNull(testMethod.getContainingClass()), a);
            TestSingleton.getInstance().getTestClasses().put(test.getName(), test.getTest_class_name());
        }

        // run mvn clover
        try {
            CloverRunner.runClover();
        } catch (InterruptedException e) {
            logger.log(PluginLogger.Level.ERROR, e.getMessage());
        }
        // openclover api html report
        CloverApiRunner.runHtmlReporter();

        //run init test coverage
        try {
            CloverParser.getTestCoverageWithinClasses();
        } catch (JSONException e) {
            logger.log(PluginLogger.Level.ERROR, e.getMessage());
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
            CloverParser.getTestCoverageWithinClasses();
        } catch (JSONException e) {
            logger.log(PluginLogger.Level.ERROR, e.getMessage());
        }
        // for every class
        for (PsiClass c : allClassesForProject) {
            // check if report is generated
            if (TestSingleton.getInstance().getCoverageByClass().containsKey(c.getName())) {
                // if y, get all elements on line of code
                psiHandler.mapLinesToTests(c, TestSingleton.getInstance().getCoverageByClass().get(c.getName()));
            }
        }


    }

    /**
     * method for prioritization and test execution
     */
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
                if (!results.get(0).getExec_time().isEmpty()){
                    exec_time = Double.parseDouble(results.get(0).getExec_time());
                }
            }
            priorityQue.add(new PrioritizationValuator(test_name, exec_value, exec_time));

        }
//        priorityQue.sort((o1, o2) -> (int) (o1.getHistoryValue() == o2.getHistoryValue() ? o1.getTimeValue() - o2.getTimeValue() :
//                o1.getHistoryValue() - o2.getHistoryValue()));

        final double THRESHOLD = .0000001;
        priorityQue.sort((o1, o2) -> (Math.abs(o1.getHistoryValue() - o2.getHistoryValue()) < THRESHOLD &&
                Math.abs(o1.getTimeValue() - o2.getTimeValue()) < THRESHOLD) ? 0 :
                (Math.abs(o1.getHistoryValue() - o2.getHistoryValue()) < THRESHOLD) ?
                        (o1.getTimeValue() > o2.getTimeValue() ? 1 : -1) :
                        (o1.getHistoryValue() > o2.getHistoryValue() ? 1 : -1));
        // run all tests
        for (PrioritizationValuator test_method : priorityQue) {
            String className = TestSingleton.getInstance().getTestClasses().get(test_method.getTest_name());
            TestRunner.runTest(className, test_method.getTest_name());
        }


    }

    /**
     * method for highlighting events
     */
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
