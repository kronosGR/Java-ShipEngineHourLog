package kandz.me.shipenginehourslog;

public class MaintanceRoutinesClass {

    private int mid;
    private String mName;
    private String mDesc;
    private int mHours;

    public MaintanceRoutinesClass(int mid, String mName, String mDesc, int mHours) {
        this.mid = mid;
        this.mName = mName;
        this.mDesc = mDesc;
        this.mHours = mHours;
    }

    public MaintanceRoutinesClass() {
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public int getmHours() {
        return mHours;
    }

    public void setmHours(int mHours) {
        this.mHours = mHours;
    }
}
