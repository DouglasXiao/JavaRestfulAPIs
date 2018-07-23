package dao;

import model.PatientInfo;

import java.util.*;

public class PatientsDAO {

    private Map<String, List<PatientInfo>> map;   // key : value  -> tokenId : PatientInfo list

    public PatientsDAO() {
        map = new HashMap<>();

        PatientInfo p1 = new PatientInfo("MaDongMei", "2018-07-22", "10:00", UUID.randomUUID().toString());
        List<PatientInfo> list = new ArrayList<>();
        list.add(p1);
        map.put("123456", list);
    }

    public Map<String, List<PatientInfo>> getMap() {
        return map;
    }
}
