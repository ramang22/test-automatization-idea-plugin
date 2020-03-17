package testController;

import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiTreeUtil;
import ide.CodeChangeListener;
import ide.IDEA;
import ide.PsiHandler;
import ide.Tester;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;
import test.Test;
import test.UnitTests;
import testRunner.TestRunner;

import java.io.File;
import java.net.URL;
import java.util.*;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

public class MainTestController {

    //https://github.com/rastocny/mock-management/blob/master/src/mock/manager/data/psiparser/PsiParser.java


    public void runAllTests() throws InterruptedException, NoSuchMethodException {


        List<PsiTreeChangeEvent> events = TestSingleton.getInstance().getEvents();
        HashSet<String> methodsForTesting = new HashSet<>();
        for (PsiTreeChangeEvent event : events) {
            PsiElement psiTreeElement = event.getParent();
            PsiMethod parentMethod = psiTreeElement instanceof PsiMethod ? (PsiMethod) psiTreeElement : PsiTreeUtil.getTopmostParentOfType(psiTreeElement, PsiMethod.class);
            if (parentMethod != null) {
                methodsForTesting.add(parentMethod.getName());

            }
        }

        HashSet<String> testNames = new HashSet<>();
        for (String method : methodsForTesting) {
            testNames.addAll(TestSingleton.getInstance().getTestMap().get(method));
        }

        //todo prioritization

        List<TestRunner> tests = new ArrayList<>();
        for (String test : testNames ){
            tests.add(new TestRunner(Test.class.getMethod(test)));
        }

        PluginSingleton.getInstance().getTester().setTests(tests);
        PluginSingleton.getInstance().getTester().runTests();
    }
}
