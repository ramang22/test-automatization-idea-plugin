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
        if (exec_time.equals("") || exec_time.equals(" ") || exec_time.isEmpty()){
            this.exec_time = "0";
        }
        this.exec_time = exec_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTest_run() {
        return test_run;
    }

    public void setTest_run(int test_run) {
        this.test_run = test_run;
    }

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getExec_time() {
        return exec_time;
    }

    public void setExec_time(String exec_time) {
        this.exec_time = exec_time;
    }
}
