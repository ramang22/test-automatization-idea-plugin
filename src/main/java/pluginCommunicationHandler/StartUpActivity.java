package pluginCommunicationHandler;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import ide.CodeChangeListener;
import ide.PsiHandler;
import org.jetbrains.annotations.NotNull;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;
import test.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StartUpActivity implements StartupActivity {
    public StartUpActivity() throws IOException {
    }

    @Override
    public void runActivity(@NotNull Project project) {
        //setup code change listener
        PsiManager psiManager = PsiManager.getInstance(project);
        psiManager.addPsiTreeChangeListener(new CodeChangeListener());

        //set project variables
        PluginSingleton.getInstance().setProject(project);
        //test all methods in tests
        PsiHandler psiHandler = new PsiHandler();
        List<PsiMethod> methods = psiHandler.getAllTests(project);
        List<Test> tests = new ArrayList<>();
        for (PsiMethod test : methods) {
            HashSet<PsiMethod> a = psiHandler.traverseBodyToFindAllMethodUsages(test.getBody());
            tests.add(new Test(test, Objects.requireNonNull(test.getContainingClass()), a));
        }

        //create global hash map in singleton
        for (Test test : tests) {
            TestSingleton.getInstance().getTestClasses().put(test.getName(),test.getTest_class_name());
            for (String name : test.getContainedMethods_names()) {
                if (TestSingleton.getInstance().getTestMap().get(name) == null) {
                    List<String> testNames = new ArrayList<>();
                    testNames.add(test.getName());
                    TestSingleton.getInstance().getTestMap().put(name, testNames);
                } else {
                    TestSingleton.getInstance().getTestMap().get(name).add(test.getName());
                }
            }
        }

    }



}
