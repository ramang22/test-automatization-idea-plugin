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

    public void insert(String name, int last_result) {
        String sql = "INSERT INTO test(name,lastResult) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, last_result);
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
                rs_tests.add(new TestDb(rs.getString("name"), rs.getInt("id"), rs.getInt("lastResult")));
            }
            return rs_tests;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void update(int id, String name, Integer last_result) {
        String sql = "UPDATE Test SET name = ? , "
                + "lastResult = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, name);
            pstmt.setDouble(2, last_result);
            pstmt.setInt(3, id);
            // update
            pstmt.executeUpdate();
            System.out.println("Test updated");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
