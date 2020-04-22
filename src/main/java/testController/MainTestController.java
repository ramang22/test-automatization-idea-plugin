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


        List<PsiTreeChangeEvent> events = TestSingleton.getInstance().getEvents();
        HashSet<String> testNames = new HashSet<>();
//        for (PsiTreeChangeEvent event : events) {
//            // TODO : make filter for event changes, filter out useless changes
//            PsiElement psiTreeElement = event.getParent();
//            PsiMethod parentMethod = psiTreeElement instanceof PsiMethod ? (PsiMethod) psiTreeElement : PsiTreeUtil.getTopmostParentOfType(psiTreeElement, PsiMethod.class);
//            if (parentMethod != null) {
//                //TODO : selection
//                String methodName = parentMethod.getName();
//                if (TestSingleton.getInstance().getEventsForMethod().containsKey(methodName)) {
//                    TestSingleton.getInstance().getEventsForMethod().get(methodName).add(event);
//                } else {
//                    List<PsiTreeChangeEvent> new_event_list = new ArrayList<>();
//                    new_event_list.add(event);
//                    TestSingleton.getInstance().getEventsForMethod().put(methodName, new_event_list);
//                }
//                if (TestSingleton.getInstance().getTestMap().containsKey(methodName)) {
//                    testNames.addAll(TestSingleton.getInstance().getTestMap().get(methodName));
//                    for (String test_name : TestSingleton.getInstance().getTestMap().get(methodName)) {
//                        if (TestSingleton.getInstance().getTestMethod_event().containsKey(test_name)) {
//                            TestSingleton.getInstance().getTestMethod_event().get(test_name).add(event);
//                        } else {
//                            List<PsiTreeChangeEvent> new_list = new ArrayList<>();
//                            new_list.add(event);
//                            TestSingleton.getInstance().getTestMethod_event().put(test_name, new_list);
//                        }
//                    }
//                }
//            }
//        }

        for (PsiTreeChangeEvent event : events) {
            PsiElement element = event.getOldChild();
            if (element == null) {
                System.out.println("Event element is null");
                continue;
            }
            // check if is event elemement in coverage
            // if no continue, if yes - get all tests linked to element
            if (TestSingleton.getInstance().getPsiElementToTests().containsKey(element)) {
                System.out.println("Found element " + element.getText());
                testNames.addAll(TestSingleton.getInstance().getPsiElementToTests().get(element));
            } else {
                System.out.println("Old child Element "+element.getText()+" not in hash map : " + element.hashCode());
                System.out.println("Old parent Element "+event.getOldParent().getText()+" not in hash map : " + element.hashCode());
                System.out.println("New child parent Element "+event.getNewChild().getText()+" not in hash map : " + element.hashCode());
                System.out.println("New parent Element "+event.getNewParent().getText()+" not in hash map : " + element.hashCode());
            }
        }
        /*
         *       TODO Test Prioritization
         *          1. for every test check last result in db
         *          2. get failed test for priority, then passed tests
         *          3. for failed tests by test time (lower first), for passed tests do same
         */

        // run all tests
        for (String test_method : testNames) {
            if (TestSingleton.getInstance().getTestClasses().containsKey(test_method)) {
                String className = TestSingleton.getInstance().getTestClasses().get(test_method);
                testRunner.runTest(className, test_method);
            }
        }

        // TODO clean test singleton, delete changes from last run
        TestSingleton.getInstance().getEvents().clear();
        TestSingleton.getInstance().getTestMethod_event().clear();
        TestSingleton.getInstance().getEventsForMethod().clear();

    }
}
