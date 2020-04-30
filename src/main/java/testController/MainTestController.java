package testController;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import database.DbController;
import database.TestResultDb;
import highlighter.CodeHighlighter;
import ide.PsiHandler;
import opencloverController.cloverParser;
import org.json.JSONException;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;
import mavenRunner.*;
import opencloverController.*;
import prioritization.PrioritizationValuator;

import java.util.*;

public class MainTestController {


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

        CodeHighlighter.removeOldHighlights();

        //copy event map into new map for execution
        HashMap<String, List<PsiElement>> copiedEvents = new HashMap<>(TestSingleton.getInstance().getTestMethod_event());
        TestSingleton.getInstance().setTestMethod_event_forExecution(copiedEvents);
        TestSingleton.getInstance().getTestMethod_event().clear();

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
                exec_value += (float) results.get(i).getResult() / i + 1;
            }
            double exec_time = Double.parseDouble(results.get(0).getExec_time());
            priorityQue.add(new PrioritizationValuator(test_name, exec_value, exec_time));

        }
        priorityQue.sort((o1, o2) -> {
            return (int) (o1.getHistoryValue() == o2.getHistoryValue() ? o1.getTimeValue() - o2.getTimeValue() : o1.getHistoryValue() - o2.getHistoryValue());
//            double t1_value = o1.getHistoryValue();
//            double t2_value = o2.getHistoryValue();
//            if (t1_value > t2_value) {
//                return 1;
//            } else if (t1_value < t2_value) {
//                return -1;
//            } else {
//                double t1_time = o1.getTimeValue();
//                double t2_time = o1.getTimeValue();
//                if (t1_time > t2_time) {
//                    return 1;
//                } else {
//                    return -1;
//                }
//            }
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
