package testController;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
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
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
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
//            try {
//                PsiJavaFile javaFile = (PsiJavaFile) parentMethod.getParent().getContainingFile();
//                PsiPackage pkg = JavaPsiFacade.getInstance(PluginSingleton.getInstance().getProject()).findPackage(javaFile.getPackageName());
//                System.out.println(pkg.getName());
//                System.out.println(this.getClass().getCanonicalName());
//                System.out.println(parentMethod.getContainingClass().getQualifiedName());
//                Class c = Class.forName(parentMethod.getContainingClass().getQualifiedName());
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
        }
        HashSet<String> testNames = new HashSet<>();
        for (String method : methodsForTesting) {
            testNames.addAll(TestSingleton.getInstance().getTestMap().get(method));
        }

//        //todo prioritization
    }
}
