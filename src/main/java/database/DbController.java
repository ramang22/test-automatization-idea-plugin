package database;

import logger.PluginLogger;
import pluginCommunicationHandler.StartUpActivity;
import pluginResources.PluginSingleton;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbController {

    final PluginLogger logger = new PluginLogger(DbController.class);

    private Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            Class.forName("org.sqlite.JDBC");
            // TODO find how auto import this
            String path = PluginSingleton.getInstance().getProjectRootFolderPath() + "TestPlugin/database/test.db";
            String url = "jdbc:sqlite:" + path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(PluginLogger.Level.ERROR, e.getMessage());
        }
        return conn;
    }

    public void insert(String name) {
        String sql = "INSERT INTO test(name) VALUES(?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(PluginLogger.Level.ERROR, e.getMessage());
        }
    }

    public List<TestDb> getTestByTestName(String test_name) {
        String sql = "SELECT *"
                + "FROM Test WHERE name = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, test_name);
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            List<TestDb> rs_tests = new ArrayList<>();
            while (rs.next()) {
                rs_tests.add(new TestDb(rs.getString("name"), rs.getInt("id")));
            }
            return rs_tests;
        } catch (SQLException e) {
            logger.log(PluginLogger.Level.ERROR, e.getMessage());
        }
        return null;
    }

    public void update(int id, String name) {
        String sql = "UPDATE Test SET name = ? , "
                + "lastResult = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, name);
            pstmt.setInt(3, id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(PluginLogger.Level.ERROR, e.getMessage());
        }
    }

    public List<TestResultDb> getAllTestResultsById(int test_id) {
        String sql = "SELECT *"
                + "FROM testResult WHERE test_id = ?"
                + "ORDER BY test_run DESC";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setInt(1, test_id);
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            List<TestResultDb> rs_tests = new ArrayList<>();
            //(int id, int test_id, int test_run,String test_name, int result, String exec_time)
            while (rs.next()) {
                rs_tests.add(new TestResultDb(
                        rs.getInt("id"),
                        rs.getInt("test_id"),
                        rs.getInt("test_run"),
                        "",
                        rs.getInt("result"),
                        rs.getString("exec_time")
                ));
            }
            return rs_tests;
        } catch (SQLException e) {
            logger.log(PluginLogger.Level.ERROR, e.getMessage());
        }
        return null;
    }

    public void addTestToDb(String testName) {
        // check if is test in db
        List<TestDb> tests = getTestByTestName(testName);
        if (tests.isEmpty()) {
            insert(testName);
        }
    }


    public void insertTestResult(int test_id, int test_run, int result, String exec_time) {
        String sql = "INSERT INTO testResult(test_id, test_run, result, exec_time) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, test_id);
            pstmt.setInt(2, test_run);
            pstmt.setInt(3, result);
            pstmt.setString(4, exec_time);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(PluginLogger.Level.ERROR, e.getMessage());
        }
    }

    public void addTestResult(String test_name, int result, String time) {
        List<TestDb> tests = getTestByTestName(test_name);
        for (TestDb test : tests) {
            int test_id = test.getId();
            List<TestResultDb> testResults = getAllTestResultsById(test_id);
            int test_run = testResults.size();
            insertTestResult(test_id, test_run + 1, result, time);
        }
    }

    public List<TestResultDb> getAllTestResultsByName(String test_name) {
        List<TestDb> tests = getTestByTestName(test_name);
        List<TestResultDb> resultsForTest = new ArrayList<>();
        for (TestDb test : tests) {
            resultsForTest.addAll(getAllTestResultsById(test.getId()));
        }
        return resultsForTest;
    }

    public void checkIfDbExists() throws ClassNotFoundException, SQLException {
        String dbUrl = PluginSingleton.getInstance().getProjectRootFolderPath() + "TestPlugin/database/test.db";
        File f = new File(dbUrl);
        if (!f.exists()) {
            new File(PluginSingleton.getInstance().getProjectRootFolderPath() + "TestPlugin/database").mkdirs();
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl)) {
                if (conn != null) {
                    logger.log(PluginLogger.Level.INFO, "A new databse has been created");
                    Statement stmt = conn.createStatement();
                    String sql = "CREATE TABLE \"testResult\" ( \"id\" INTEGER PRIMARY KEY AUTOINCREMENT, \"test_id\" INTEGER, \"result\" INTEGER, \"exec_time\" TEXT, \"test_run\" INTEGER )";
                    stmt.executeUpdate(sql);
                    stmt = conn.createStatement();
                    sql = "CREATE TABLE \"Test\" ( \"id\" INTEGER PRIMARY KEY AUTOINCREMENT, \"Name\" TEXT )";
                    stmt.executeUpdate(sql);
                }
            } catch (SQLException e) {
                logger.log(PluginLogger.Level.ERROR, e.getMessage());
            }
        }
    }
}
