package mavenRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public interface CustomRunner {

    public static String getStdInput(Process process) {
        BufferedReader stdInput =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        return stdInput.lines().collect(Collectors.joining("\n"));
    }



}
