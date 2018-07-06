package kandz.me.shipenginehourslog;

public class HourClass {

    private int hId;
    private String hDate;
    private String hTime;
    private int hEId;
    private int hAmount;
    private int hAmountMinutes;

    public HourClass(int hId, String hDate, String hTime, int hEId, int hAmount , int hAmountMinutes) {
        this.hId = hId;
        this.hDate = hDate;
        this.hTime = hTime;
        this.hEId = hEId;
        this.hAmount = hAmount;
        this.hAmountMinutes = hAmountMinutes;
    }

    public HourClass() {
    }

    public int gethId() {
        return hId;
    }

    public void sethId(int hId) {
        this.hId = hId;
    }

    public String gethDate() {
        return hDate;
    }

    public void sethDate(String hDate) {
        this.hDate = hDate;
    }

    public String gethTime() {
        return hTime;
    }

    public void sethTime(String hTime) {
        this.hTime = hTime;
    }

    public int gethEId() {
        return hEId;
    }

    public void sethEId(int hEId) {
        this.hEId = hEId;
    }

    public int gethAmount() {
        return hAmount;
    }

    public void sethAmount(int hAmount) {
        this.hAmount = hAmount;
    }

    public int gethAmountMinutes() {
        return hAmountMinutes;
    }

    public void sethAmountMinutes(int hAmountMinutes) {
        this.hAmountMinutes = hAmountMinutes;
    }
}
