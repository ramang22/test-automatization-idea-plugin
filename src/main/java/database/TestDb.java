package database;

public class TestDb {

    private String name;
    private Integer id;
    private Integer lastResult;

    public TestDb(String name, Integer id, Integer lastResult) {
        this.name = name;
        this.id = id;
        this.lastResult = lastResult;
    }

    public void printSelf() {
        System.out.println("Test : " + this.name);
        System.out.println("Test id : " + this.id);
        System.out.println("Test last result : " + this.lastResult);
    }

    public Integer getLastResult() {
        return lastResult;
    }

    public void setLastResult(Integer lastResult) {
        this.lastResult = lastResult;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
