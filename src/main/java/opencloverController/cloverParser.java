package opencloverController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import org.json.*;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;

public class cloverParser {

    private String readWholeFile(String filepath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filepath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    private HashMap<Integer, String> getTestTargets(String file_content) throws JSONException {
        HashMap<Integer, String> id_name_map = new HashMap<>();
        String json_string = this.getSubstringByTwoStrings("clover.testTargets = ", "// JSON: { lines ", file_content);
        JSONObject json = new JSONObject(json_string);
        if (json.length() > 0) {
            for (int i = 0; i < json.names().length(); i++) {
                //get test id from "test_1"
                String test_id = json.names().getString(i).split("_")[1];
                //get test name form json field name
                JSONObject test = (JSONObject) json.get(json.names().getString(i));
                String test_name = test.get("name").toString();
                id_name_map.put(Integer.parseInt(test_id), test_name);
            }
            return id_name_map;
        }
        return null;
    }

    private String getSubstringByTwoStrings(String start, String end, String searchString) {
        int startIndexOfJSON = searchString.indexOf(start);
        int endIndexOfJSON = searchString.indexOf(end);
        int lenTestTargets = start.length();
        return searchString.substring(startIndexOfJSON + lenTestTargets, endIndexOfJSON);
    }

    private void checkIfCouldBeAdded(HashMap<Integer, List<String>> test_lines, HashMap<Integer, String> test_id_test_name, String test_id, Integer line_num) {
        if (!test_id.equals("")) {
            if (test_lines.containsKey(line_num)) {
                test_lines.get(line_num).add(test_id_test_name.get(Integer.parseInt(test_id)));
            } else {
                List<String> newList = new ArrayList<>();
                newList.add(test_id_test_name.get(Integer.parseInt(test_id)));
                test_lines.put(line_num,newList);
            }
        }
    }

    private HashMap<Integer, List<String>> getSrcFileLines(HashMap<Integer, String> test_id_test_name, String file_content) {
        HashMap<Integer, List<String>> test_lines = new HashMap<>();

        int startIndexOfJSON = file_content.indexOf("clover.srcFileLines = ");
        int lenSrcFileLines = "clover.srcFileLines = ".length();
        String lines = file_content.substring(startIndexOfJSON + lenSrcFileLines);
        int line_num = -1;
        StringBuilder test_id = new StringBuilder();
        for (int i = 0; i < lines.length(); i++) {
            char c = lines.charAt(i);
            if (c == '[') {
                line_num += 1;
            } else if (c == ']') {
                this.checkIfCouldBeAdded(test_lines, test_id_test_name, test_id.toString(), line_num);
                test_id = new StringBuilder();
                i += 2;
            } else if (c == ',') {
                this.checkIfCouldBeAdded(test_lines, test_id_test_name, test_id.toString(), line_num);
                test_id = new StringBuilder();
            } else if (c == ' ') {
                continue;
            } else {
                test_id.append(c);
            }
        }
        return test_lines;
    }

    private void getTestCoverageInClass(String classPath, String className) throws JSONException {
        // load file
        String file_content_string = this.readWholeFile(classPath);
        // get 1. property clover.testTargets
        HashMap<Integer, String> test_id_test_name = this.getTestTargets(file_content_string);
        // get 2. property clover.srcFileLines
        if (test_id_test_name != null){
            HashMap<Integer, List<String>> testCoverageByLine = this.getSrcFileLines(test_id_test_name, file_content_string);
            TestSingleton.getInstance().getCoverageByClass().put(className,testCoverageByLine);
        }else{
            System.out.println("V "+className+" som nenasiel ziadny coverage.");
        }

    }


    public static void getTestCoverageWithinClasses() throws JSONException {
        cloverParser parser = new cloverParser();

        // file paths
        HashSet<String> coveredClasses = PluginSingleton.getInstance().getPackage_file_paths();
        // html report path
        String htmlReportPath = PluginSingleton.getInstance().getClover_html_report_path();

        for (String path : coveredClasses) {
            //check if file exists in clover report
            File tempFile = new File(htmlReportPath + path);
            if (tempFile.exists()) {
                System.out.println(path);
                parser.getTestCoverageInClass(htmlReportPath + path,path.split("/")[1].split("\\.")[0]);

            }
        }
    }

}
