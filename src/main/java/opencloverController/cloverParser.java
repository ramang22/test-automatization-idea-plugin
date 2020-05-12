package opencloverController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;

import logger.PluginLogger;
import org.json.*;
import pluginResources.PluginSingleton;
import pluginResources.TestSingleton;

public class cloverParser {
    /**
     * logger instance
     */
    static final PluginLogger logger = new PluginLogger(cloverParser.class);

    /**
     * Read whole file and put it into string
     *
     * @param filepath file path
     * @return String from file
     */
    private String readWholeFile(String filepath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filepath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            logger.log(PluginLogger.Level.ERROR, e.getMessage());
        }
        return contentBuilder.toString();
    }

    /**
     * Get json from OpenClover js files
     *
     * @param file_content file input
     * @return Map<TestId, TestName>
     */
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

    /**
     * get Substring by two strings
     *
     * @param start        first string
     * @param end          second string
     * @param searchString all string
     * @return return substring from searchstring
     */
    private String getSubstringByTwoStrings(String start, String end, String searchString) {
        int startIndexOfJSON = searchString.indexOf(start);
        int endIndexOfJSON = searchString.indexOf(end);
        int lenTestTargets = start.length();
        return searchString.substring(startIndexOfJSON + lenTestTargets, endIndexOfJSON);
    }

    /**
     * @param test_lines        test lines
     * @param test_id_test_name test id name map
     * @param test_id           test id
     * @param line_num          line number
     */
    private void checkIfCouldBeAdded(HashMap<Integer, HashSet<String>> test_lines, HashMap<Integer, String> test_id_test_name, String test_id, Integer line_num) {
        if (!test_id.equals("")) {
            if (test_lines.containsKey(line_num)) {
                test_lines.get(line_num).add(test_id_test_name.get(Integer.parseInt(test_id)));
            } else {
                HashSet<String> newList = new HashSet<String>();
                newList.add(test_id_test_name.get(Integer.parseInt(test_id)));
                test_lines.put(line_num, newList);
            }
        }
    }

    /**
     * @param test_id_test_name test id name map
     * @param file_content      file input to string
     * @return hashmap<line, list < testnames>
     */
    private HashMap<Integer, HashSet<String>> getSrcFileLines(HashMap<Integer, String> test_id_test_name, String file_content) {
        HashMap<Integer, HashSet<String>> test_lines = new HashMap<>();

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

    /**
     * get coverage from class
     *
     * @param classPath class path
     * @param className class name
     */
    private void getTestCoverageInClass(String classPath, String className) throws JSONException {
        String file_content_string = this.readWholeFile(classPath);
        HashMap<Integer, String> test_id_test_name = this.getTestTargets(file_content_string);
        if (test_id_test_name != null) {
            HashMap<Integer, HashSet<String>> testCoverageByLine = this.getSrcFileLines(test_id_test_name, file_content_string);
            TestSingleton.getInstance().getCoverageByClass().put(className, testCoverageByLine);
        } else {
            logger.log(PluginLogger.Level.INFO, "For " + className + " no coverage found.");
        }
    }

    /**
     * get coverage for package
     */
    public static void getTestCoverageWithinClasses() throws JSONException {
        cloverParser parser = new cloverParser();

        HashSet<String> coveredClasses = PluginSingleton.getInstance().getPackage_file_paths();
        String htmlReportPath = PluginSingleton.getInstance().getClover_html_report_path();

        for (String path : coveredClasses) {
            File tempFile = new File(htmlReportPath + path);
            if (tempFile.exists()) {
                logger.log(PluginLogger.Level.INFO, "Coverage for : " + path);
                parser.getTestCoverageInClass(htmlReportPath + path, path.split("/")[1].split("\\.")[0]);

            }
        }
    }

}
