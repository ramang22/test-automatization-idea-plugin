package pluginCommunicationHandler;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.psi.*;
import database.DbController;
import ide.CodeChangeListener;
import ide.PsiHandler;
import logger.PluginLogger;
import opencloverController.CloverApiRunner;
import mavenRunner.CloverRunner;
import opencloverController.CloverParser;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;
import test.Test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class StartUpActivity implements StartupActivity {
    /**
     * instance of PlugginLogger
     */
    final PluginLogger logger = new PluginLogger(StartUpActivity.class);

    public StartUpActivity() {
    }

    /**
     * first method in plugin to be executed
     *
     * @param project project instance
     */
    @Override
    public void runActivity(@NotNull Project project) {
        //setup code change listener
        PsiManager psiManager = PsiManager.getInstance(project);
        //set project variables
        PluginSingleton.getInstance().setProject(project);
        PluginSingleton.getInstance().setProjectRootFolderPath(project.getBasePath() + "/");
        psiManager.addPsiTreeChangeListener(new CodeChangeListener());
        //test all methods in tests
        logger.log(PluginLogger.Level.INFO, "Plugin startup");

        PsiHandler psiHandler = new PsiHandler();

        DbController db_controller = new DbController();

        try {
            db_controller.checkIfDbExists();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        List<PsiMethod> methods = psiHandler.getAllTests(project);

        for (PsiMethod testMethod : methods) {
            // check if test is in db
            db_controller.addTestToDb(testMethod.getName());
            HashSet<PsiMethod> a = psiHandler.traverseBodyToFindAllMethodUsages(testMethod.getBody());
            Test test = new Test(testMethod, Objects.requireNonNull(testMethod.getContainingClass()), a);
            TestSingleton.getInstance().getTestClasses().put(test.getName(), test.getTest_class_name());
        }

//        // run mvn clover
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
        List<PsiClass> allClassesForProject = psiHandler.getAllClasses(project);
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
}
