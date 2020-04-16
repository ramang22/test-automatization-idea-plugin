package testController;

import com.atlassian.clover.reporters.html.HtmlReporter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.*;
import database.DbController;
import database.TestDb;
import opencloverController.cloverParser;
import org.json.JSONException;
import org.xml.sax.SAXException;
import pluginResources.TestSingleton;
import mavenTestRunner.testRunner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.*;


public class MainTestController {


    public void runAllTests() throws InterruptedException, IOException, ParserConfigurationException, SAXException, TransformerException, JSONException {


//        String [] cliArgs2 = { "-i", "/Users/ramang/Documents/Developer/tests-project-for-plugin/target/clover/clover.db", "-o", "/Users/ramang/Documents/Developer/tests-project-for-plugin/src/main/java/main/" };
//        HtmlReporter.runReport(cliArgs2);
        cloverParser.getTestCoverageWithinClasses();

//        DbController.insert("Test_add", 1);
//        for (TestDb test : Objects.requireNonNull(DbController.getTestByTestName("Test_add"))){
//            System.out.println("Updating tests");
//            DbController.update(test.getId(), test.getName(), 0 );
//            test.printSelf();
//        }
//        //testRunner.runClover();
//        //clover mvn clean test clover:setup clover:aggregate clover:clover
//
//        // TODO FIX clean all previous highlights
//        //CodeHighlighter.removeOldHighlights();
//
//
//        List<PsiTreeChangeEvent> events = TestSingleton.getInstance().getEvents();
//        HashSet<String> testNames = new HashSet<>();
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
//                testNames.addAll(TestSingleton.getInstance().getTestMap().get(methodName));
//                for (String test_name : TestSingleton.getInstance().getTestMap().get(methodName)) {
//                    if (TestSingleton.getInstance().getTestMethod_event().containsKey(test_name)) {
//                        TestSingleton.getInstance().getTestMethod_event().get(test_name).add(event);
//                    } else {
//                        List<PsiTreeChangeEvent> new_list = new ArrayList<>();
//                        new_list.add(event);
//                        TestSingleton.getInstance().getTestMethod_event().put(test_name, new_list);
//                    }
//                }
//            }
//        }
//
//        /*
//         *       TODO Test Prioritization
//         *          1. for every test check last result in db
//         *          2. get failed test for priority, then passed tests
//         *          3. for failed tests by test time (lower first), for passed tests do same
//         */
//
//        // run all tests
//        for (String test_method : testNames) {
//            if (TestSingleton.getInstance().getTestClasses().containsKey(test_method)) {
//                String className = TestSingleton.getInstance().getTestClasses().get(test_method);
//                testRunner.runTest(className, test_method);
//            }
//        }

        // TODO clean test singleton, delete changes from last run


//        //GET ALL
//        List<PsiTreeChangeEvent> eventsForHighlight = TestSingleton.getInstance().getEvents();
//        HashMap<String, PsiTreeChangeEvent> gutterList = new HashMap<>();
//        gutterList.clear();
//        for (PsiTreeChangeEvent event : eventsForHighlight) {
//
//            CodeHighlighter.highLightLine(event);
//            if (!gutterList.containsKey(parentMethod.getName())) {
//                gutterList.put(parentMethod.getName(), event);
//                //HighlightSingleton.getInstance().getMethodsHeaders().add(containingMethod);
//                CodeHighlighter.highLightLineWithGutter(parentMethod);
//            }
//
//
//        }

//        JupiterTestEngine testEngine  = new JupiterTestEngine();
//
//        LauncherConfig launcherConfig = LauncherConfig.builder()
//                .addTestEngines(testEngine)
//                .build();
//        Launcher launcher = LauncherFactory.create(launcherConfig);
//
//        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
//                .selectors(
//                        //selectPackage("test"),
//                        //selectClass(FirstUnitTest.class),
//                        selectMethod("test.UnitTests#whenSomethingElse_thenSomethingElse")
//                )
//                .build();
//        SummaryGeneratingListener listener = new SummaryGeneratingListener();
//        launcher.registerTestExecutionListeners(listener);
//        launcher.execute(request);
//        TestExecutionSummary summary = listener.getSummary();
//        long result = summary.getTestsSucceededCount();
//        System.out.println(result);

    }
}
