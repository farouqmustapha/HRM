package farouqmustapha.heartratemonitor;

/**
 * Created by Farouq Mustapha on 5/5/2017.
 */

public class AmbulanceRequest {
    private String date;
    private String patientKey;
    private String requestStatus;
    private String patientLatitude;
    private String patientLongitude;

    public AmbulanceRequest(){

    }

    public AmbulanceRequest(String date, String patientKey, String requestStatus, String patientLatitude, String patientLongitude){
        this.date = date;
        this.patientKey = patientKey;
        this.requestStatus = requestStatus;
        this.patientLatitude = patientLatitude;
        this.patientLongitude = patientLongitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getPatientLatitude() {
        return patientLatitude;
    }

    public void setPatientLatitude(String patientLatitude) {
        this.patientLatitude = patientLatitude;
    }

    public String getPatientLongitude() {
        return patientLongitude;
    }

    public void setPatientLongitude(String patientLongitude) {
        this.patientLongitude = patientLongitude;
    }

    public String getPatientKey() {
        return patientKey;
    }

    public void setPatientKey(String patientKey) {
        this.patientKey = patientKey;
    }
}
