package prioritization;

public class PrioritizationValuator {
    private String test_name;
    private double historyValue;
    private double timeValue;

    public PrioritizationValuator(String test_name, double historyValue, double timeValue){
        this.test_name = test_name;
        this.historyValue = historyValue;
        this.timeValue = timeValue;
    }

    public PrioritizationValuator(){

    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public double getHistoryValue() {
        return historyValue;
    }

    public void setHistoryValue(double historyValue) {
        this.historyValue = historyValue;
    }

    public double getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(double timeValue) {
        this.timeValue = timeValue;
    }
}
