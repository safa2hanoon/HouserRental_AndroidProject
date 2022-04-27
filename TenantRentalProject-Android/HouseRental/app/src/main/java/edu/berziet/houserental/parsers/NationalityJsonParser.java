package edu.berziet.houserental.parsers;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import edu.berziet.houserental.models.NationalityModel;

public class NationalityJsonParser {
    public static List<NationalityModel> getObjectFromJson(String json) {
        List<NationalityModel> nationalitiesList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                NationalityModel nationality = new NationalityModel();
                nationality.setNationalityId(jsonObject.getInt("id"));
                nationality.setNationality(jsonObject.getString("nationality"));
                nationalitiesList.add(nationality);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nationalitiesList;
    }
}
