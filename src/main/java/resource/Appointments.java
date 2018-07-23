package resource;

import com.google.gson.Gson;
import dao.PatientsDAO;
import model.PatientInfo;

import java.util.*;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class Appointments {

    public static void main(String[] args) {
        establish();
    }

    public static void establish() {
        PatientsDAO patientsDAO = new PatientsDAO();
        Map<String, List<PatientInfo>> map = patientsDAO.getMap();

        get("/appointments/*/*/free", (req, res) -> {
            List<String> dates = new ArrayList<>();

            for (Map.Entry<String, List<PatientInfo>> entry : map.entrySet()) {
                if (Objects.equals(entry.getKey(), req.splat()[0])) {
                    List<PatientInfo> patientInfoList = entry.getValue();
                    for (PatientInfo patientInfo : patientInfoList) {
                        if (patientInfo.getAppointDate().equals(req.splat()[1])) {
                            dates.add(patientInfo.getAppointTime());
                        }
                    }
                }
            }

            String joined = new Gson().toJson(dates);

            return String.format("{slots:%s}", joined);
        });

        post("/appointments/*/*/*/*", (req, res) -> {
            String appointId = UUID.randomUUID().toString();
            String patientName = req.splat()[3];
            String appointDate = req.splat()[1];
            String appointTime = req.splat()[2];

            String tokenId = req.splat()[0];
            if (map.containsKey(tokenId)) {
                List<PatientInfo> patientInfoList = map.get(tokenId);
                for (PatientInfo patientInfo : patientInfoList) {
                    if (!Objects.equals(patientInfo.getPatientName(), patientName) ||
                            Objects.equals(patientInfo.getAppointTime(), appointTime) &&
                            Objects.equals(patientInfo.getAppointDate(), appointDate)) {
                        return "{error: \"Unable to reserve the appointment\"}";
                    }
                }
            } else {
                map.put(tokenId, new ArrayList<>());
            }
            PatientInfo patientInfo = new PatientInfo(patientName, appointDate, appointTime, appointId);
            map.get(tokenId).add(patientInfo);

            return String.format("{appointmentId:%s}", appointId);
        });

        delete("/appointments/*/*", (req, res) -> {
            String tokendId = req.splat()[0];
            String appointId = req.splat()[1];

            if (!map.containsKey(tokendId)) {
                return "{error: \"Unable to cancel the appointment\"}";
            }
            List<PatientInfo> patientInfoList = map.get(tokendId);
            boolean isFound = false;
            PatientInfo toDelete = null;
            for (PatientInfo patientInfo : patientInfoList) {
                if (Objects.equals(patientInfo.getAppointId(), appointId)) {
                    isFound = true;
                    toDelete = patientInfo;
                    break;
                }
            }

            if (isFound) {
                patientInfoList.remove(toDelete);
                return "{\"success\"}";
            } else {
                return "{error: \"Unable to cancel the appointment\"}";
            }
        });
    }
}
