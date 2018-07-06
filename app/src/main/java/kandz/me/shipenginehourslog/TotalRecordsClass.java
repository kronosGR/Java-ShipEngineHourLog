package kandz.me.shipenginehourslog;

public class TotalRecordsClass  {

    private int tMid;
    private int tEid;
    private int tTotal;
    private int tTotalMinutes;

    public TotalRecordsClass() {

    }

    public TotalRecordsClass(int tMid, int tEid, int tTotal, int tTotalMinutes) {
        this.tMid = tMid;
        this.tEid = tEid;
        this.tTotal = tTotal;
        this.tTotalMinutes = tTotalMinutes;
    }

    public int gettMid() {
        return tMid;
    }

    public void settMid(int tMid) {
        this.tMid = tMid;
    }

    public int gettEid() {
        return tEid;
    }

    public void settEid(int tEid) {
        this.tEid = tEid;
    }

    public int gettTotal() {
        return tTotal;
    }

    public void settTotal(int tTotal) {
        this.tTotal = tTotal;
    }

    public int gettTotalMinutes() {
        return tTotalMinutes;
    }

    public void settTotalMinutes(int tTotalMinutes) {
        this.tTotalMinutes = tTotalMinutes;
    }
}
