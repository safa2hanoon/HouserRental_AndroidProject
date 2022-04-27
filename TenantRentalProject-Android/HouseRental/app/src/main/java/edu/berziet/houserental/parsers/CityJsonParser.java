package edu.berziet.houserental.parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.berziet.houserental.models.CityModel;

public class CityJsonParser {
    public static List<CityModel> getObjectFromJson(String json) {
        List<CityModel> citiesList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                CityModel cityModel = new CityModel();
                cityModel.setId(jsonObject.getInt("id"));
                cityModel.setCountryId(jsonObject.getInt("countryId"));
                cityModel.setName(jsonObject.getString("name"));
                citiesList.add(cityModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return citiesList;
    }
}
