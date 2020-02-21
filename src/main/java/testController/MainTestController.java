package testController;

import com.intellij.openapi.project.Project;
import ide.IDEA;
import ide.Tester;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import test.UnitTests;

import java.io.File;
import java.net.URL;
import java.util.*;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

public class MainTestController {

    //https://github.com/rastocny/mock-management/blob/master/src/mock/manager/data/psiparser/PsiParser.java
    public List<PsiClass> getAllClasses(Project project) {
        String[] classNames = PsiShortNamesCache.getInstance(project).getAllClassNames();
        PsiShortNamesCache cache = PsiShortNamesCache.getInstance(project);
        List<PsiClass> psiClasses = new ArrayList<>();

        for (String className : classNames) {
            psiClasses.addAll(Arrays.asList(cache.getClassesByName(className, GlobalSearchScope.projectScope(project))));
        }
        return psiClasses;
    }

    public void runAllTests(String path) throws InterruptedException {
        File root = new File(path);
        for (File file : Objects.requireNonNull(root.listFiles())) {
            IDEA methodGather = new IDEA();
            Tester tester = new Tester(methodGather.getAllMethodsByClass(file.getClass()));
            tester.runTests();
        }


    }
}
