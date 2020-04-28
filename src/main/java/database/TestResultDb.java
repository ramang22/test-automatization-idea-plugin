package database;

public class TestResultDb {
    private int id;
    private int test_run;
    private int test_id;
    private String test_name;
    private int result;
    private String exec_time;

    public TestResultDb(int id, int test_id, int test_run,String test_name, int result, String exec_time){
        this.id = id;
        this.test_run = test_run;
        this.test_id = test_id;
        this.test_name = test_name;
        this.result = result;
        this.exec_time = exec_time;
    }
}
