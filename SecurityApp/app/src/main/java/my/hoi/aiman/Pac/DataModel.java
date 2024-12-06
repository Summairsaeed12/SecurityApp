package my.hoi.aiman.Pac;

import org.json.JSONObject;

public class DataModel {
    private  int id=0;
    private  String name ="";
    private  String mobileNo ="";
    private  String vehNo ="";
    private  String expDate ="";
    private JSONObject agent ;

    public JSONObject getAffcode() {
        return agent;
    }

    public void setAffcode(JSONObject affcode) {
        this.agent = affcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getVehNo() {
        return vehNo;
    }

    public void setVehNo(String vehNo) {
        this.vehNo = vehNo;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
}
