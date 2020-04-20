package pluginCommunicationHandler;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.psi.*;
import ide.CodeChangeListener;
import ide.PsiHandler;
import opencloverController.cloverApiRunner;
import mavenRunner.cloverRunner;
import opencloverController.cloverParser;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;
import test.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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
        PluginSingleton.getInstance().setProjectRootFolderPath("/Users/ramang/Documents/Developer/tests-project-for-plugin/");
        //test all methods in tests
        PsiHandler psiHandler = new PsiHandler();
        List<PsiMethod> methods = psiHandler.getAllTests(project);
        List<Test> tests = new ArrayList<>();
        for (PsiMethod test : methods) {
            HashSet<PsiMethod> a = psiHandler.traverseBodyToFindAllMethodUsages(test.getBody());
            tests.add(new Test(test, Objects.requireNonNull(test.getContainingClass()), a));
        }

        HashSet<String> packageFileString = new HashSet<>();

        //create global hash map in singleton
        for (Test test : tests) {
            TestSingleton.getInstance().getTestClasses().put(test.getName(), test.getTest_class_name());
            for (String name : test.getContainedMethods_names()) {
                if (TestSingleton.getInstance().getTestMap().get(name) == null) {
                    List<String> testNames = new ArrayList<>();
                    testNames.add(test.getName());
                    TestSingleton.getInstance().getTestMap().put(name, testNames);
                } else {
                    TestSingleton.getInstance().getTestMap().get(name).add(test.getName());
                }
            }
            //get all file and packces for run /main/Calculator.js
//            for (PsiMethod m : test.getContainedMethods()){
//                String filename = ((PsiJavaFile)m.getContainingFile()).getName().split("\\.")[0];
//                String package_name = ((PsiJavaFile)m.getContainingFile()).getPackageName();
//                packageFileString.add(package_name+"/"+filename+".js");
//            }
        }

//        PluginSingleton.getInstance().setPackage_file_paths(packageFileString);
//        // run mvn clover
//        try {
//            cloverRunner.runClover();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        // openclover api html report
//        cloverApiRunner.runHtmlReporter();

        // run init test coverage
//        try {
//            cloverParser.getTestCoverageWithinClasses();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        // get all classes in tests
        List<PsiClass> allClassesForProject = psiHandler.getAllClasses(project);
        //
        for (PsiClass c : allClassesForProject){
            String filename = ((PsiJavaFile)c.getContainingFile()).getName().split("\\.")[0];
            String package_name = ((PsiJavaFile)c.getContainingFile()).getPackageName();
            PluginSingleton.getInstance().getPackage_file_paths().add(package_name+"/"+filename+".js");
        }
        try {
            cloverParser.getTestCoverageWithinClasses();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // for every class
        for (PsiClass c : allClassesForProject){
            // check if report is generated
            if (TestSingleton.getInstance().getCoverageByClass().containsKey(c.getName())){
                System.out.println(c.getName());
                System.out.println(TestSingleton.getInstance().getCoverageByClass().get(c.getName()));
                // if y, get all elements on line of code
                psiHandler.mapLinesToTests(c, TestSingleton.getInstance().getCoverageByClass().get(c.getName()));
            }else {
                continue;
            }

        }
    }

}
