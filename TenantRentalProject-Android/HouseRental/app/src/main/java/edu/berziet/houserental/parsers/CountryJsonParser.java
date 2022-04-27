package edu.berziet.houserental.parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.berziet.houserental.models.CountryModel;

public class CountryJsonParser {
    public static List<CountryModel> getObjectFromJson(String json) {
        List<CountryModel> countriesList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CountryModel country = new CountryModel();
                country.setCountryId(jsonObject.getInt("id"));
                country.setName(jsonObject.getString("name"));
                country.setZipCode(jsonObject.getString("zipCode"));
                countriesList.add(country);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countriesList;
    }
}
