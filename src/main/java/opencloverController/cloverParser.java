package opencloverController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.json.*;

public class cloverParser {

    private String readWholeFile(String filepath) {
        StringBuilder contentBuilder = new StringBuilder();
        filepath = "/Users/ramang/Documents/Developer/line_coverage_parser_for_openclover/calc.js";
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

    private String getSubstringByTwoStrings(String start, String end, String searchString) {
        int startIndexOfJSON = searchString.indexOf(start);
        int endIndexOfJSON = searchString.indexOf(end);
        int lenTestTargets = start.length();
        return searchString.substring(startIndexOfJSON + lenTestTargets, endIndexOfJSON);
    }

    private void checkIfCouldBeAdded(HashMap<String, List<Integer>> test_lines, HashMap<Integer, String> test_id_test_name, String test_id, Integer line_num) {
        if (!test_id.equals("")) {
            if (test_lines.containsKey(test_id_test_name.get(Integer.parseInt(test_id)))) {
                test_lines.get(test_id_test_name.get(Integer.parseInt(test_id))).add(line_num);
            } else {
                List<Integer> newList = new ArrayList<>();
                newList.add(line_num);
                test_lines.put(test_id_test_name.get(Integer.parseInt(test_id)), newList);
            }
        }
    }

    private HashMap<String, List<Integer>> getSrcFileLines(HashMap<Integer, String> test_id_test_name, String file_content) {
        HashMap<String, List<Integer>> test_lines = new HashMap<>();

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

    private void getTestCoverageInClass(String className) throws JSONException {
        // load file , TODO PATH
        String file_content_string = this.readWholeFile(className);
        // get 1. property clover.testTargets
        HashMap<Integer, String> test_id_test_name = this.getTestTargets(file_content_string);
        // get 2. property clover.srcFileLines
        HashMap<String, List<Integer>> testCoverageByLine = this.getSrcFileLines(test_id_test_name, file_content_string);
        // TODO write coverage into singleton
    }


    public static void getTestCoverageWithinClasses() throws JSONException {
        // TODO get all classes used in project
        // for each class harvest coverage
        // save into singleton
        cloverParser parser = new cloverParser();
        parser.getTestCoverageInClass("calc");
    }

}
