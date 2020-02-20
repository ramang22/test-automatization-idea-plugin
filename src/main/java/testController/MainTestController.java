package testController;

import ide.IDEA;
import ide.Tester;

import test.UnitTests;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

public class MainTestController {

    public void runAllTests(String path) throws InterruptedException {
        File root = new File(path);
        for (File file : Objects.requireNonNull(root.listFiles())) {
            IDEA methodGather = new IDEA();
            Tester tester = new Tester(methodGather.getAllMethodsByClass(file.getClass()));
            tester.runTests();
        }


    }
}
