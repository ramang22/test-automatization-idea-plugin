package database;

public class TestDb {
    /**
     * Test name
     */
    private String name;
    /**
     * Test id
     */
    private Integer id;

    public TestDb(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public void printSelf() {
        System.out.println("Test : " + this.name);
        System.out.println("Test id : " + this.id);

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
