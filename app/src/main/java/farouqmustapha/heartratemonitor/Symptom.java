package farouqmustapha.heartratemonitor;

/**
 * Created by Farouq Mustapha on 4/29/2017.
 */

public class Symptom {

    private String activity;
    private String date;
    private String time;
    private String remarks;
    private String painArea;
    private String key;

    public Symptom(){
        //needed for firebase
    }

    public Symptom(String activity, String date, String time, String remarks, String painArea){
        this.activity = activity;
        this.date = date;
        this.time = time;
        this.remarks = remarks;
        this.painArea = painArea;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPainArea() {
        return painArea;
    }

    public void setPainArea(String painArea) {
        this.painArea = painArea;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
