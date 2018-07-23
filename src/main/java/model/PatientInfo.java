package model;

import lombok.Data;

@Data
public class PatientInfo {

    String patientName;
    String appointDate;
    String appointTime;
    String appointId;

    public PatientInfo(String patientName, String appointDate, String appointTime, String appointId) {
        this.patientName = patientName;
        this.appointDate = appointDate;
        this.appointTime = appointTime;
        this.appointId = appointId;
    }
}
