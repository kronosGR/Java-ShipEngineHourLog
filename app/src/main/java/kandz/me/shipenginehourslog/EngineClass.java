package kandz.me.shipenginehourslog;

public class EngineClass
{
    private int eId;
    private String eName;
    private String eDesc;
    private int eLastMaintanceId;

    public EngineClass(int eId, String eName, String eDesc, int eLastMaintanceId) {
        this.eId = eId;
        this.eName = eName;
        this.eDesc = eDesc;
        this.eLastMaintanceId = eLastMaintanceId;
    }

    public EngineClass() {
    }

    public int geteId() {
        return eId;
    }

    public void seteId(int eId) {
        this.eId = eId;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eDame) {
        this.eName = eDame;
    }

    public String geteDesc() {
        return eDesc;
    }

    public void seteDesc(String eDesc) {
        this.eDesc = eDesc;
    }

    public int geteLastMaintanceId() {
        return eLastMaintanceId;
    }

    public void seteLastMaintanceId(int eLastMaintanceId) {
        this.eLastMaintanceId = eLastMaintanceId;
    }
}
