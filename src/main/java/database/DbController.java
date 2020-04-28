package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbController {


    //        DbController.insert("Test_add", 1);
//        for (TestDb test : Objects.requireNonNull(DbController.getTestByTestName("Test_add"))){
//            System.out.println("Updating tests");
//            DbController.update(test.getId(), test.getName(), 0 );
//            test.printSelf();
//        }

    private Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:/Users/ramang/Documents/Developer/IntelliJ-IDEA-test-run-plugin/src/database/test.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(String name) {
        String sql = "INSERT INTO test(name) VALUES(?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("New test inserted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
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
            System.out.println("Test updated");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void addTestToDb(String testName){
        // check if is test in db
        List<TestDb> tests = getTestByTestName(testName);
        if (tests.isEmpty()){
            insert(testName);
        }else {
            System.out.println("test is already in db");
        }
        // yes , return
        // no, add

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
            System.out.println(e.getMessage());
        }
    }

    public void addTestResult(String test_name, int result, String time) {
        List<TestDb> tests = getTestByTestName(test_name);
        for (TestDb test : tests){
            int test_id = test.getId();
            List<TestResultDb> testResults = getAllTestResultsById(test_id);
            int test_run = testResults.size();
            System.out.println(test_run);
            insertTestResult(test_id, test_run+1, result, time);
        }
    }

    public List<TestResultDb> getAllTestResultsByName(String test_name){
        List<TestDb> tests = getTestByTestName(test_name);
        List<TestResultDb> resultsForTest = new ArrayList<>();
        for (TestDb test : tests){
            resultsForTest.addAll(getAllTestResultsById(test.getId()));
        }
        return resultsForTest;
    }
}
