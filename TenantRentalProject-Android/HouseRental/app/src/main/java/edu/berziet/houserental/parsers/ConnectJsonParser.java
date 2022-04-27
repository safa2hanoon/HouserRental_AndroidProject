package edu.berziet.houserental.parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.ConnectModel;
import edu.berziet.houserental.models.CountryModel;
import edu.berziet.houserental.models.NationalityModel;
import edu.berziet.houserental.models.PropertyModel;

public class ConnectJsonParser {

    public static ConnectModel getObjectFromJson(String json) {
        ConnectModel connectModel = new ConnectModel();
        try {
            JSONObject connectJsonObject = new JSONObject(json);

            JSONArray jsonArray = connectJsonObject.getJSONArray("propertiesList");
            List<PropertyModel> propertiesList = PropertiesListJsonParser.getListFromJsonArray(jsonArray.toString());
            connectModel.setPropertiesList(propertiesList);

            JSONArray countriesJsonArray = connectJsonObject.getJSONArray("countriesList");
            List<CountryModel> countriesList = CountryJsonParser
                    .getObjectFromJson(countriesJsonArray.toString());
            connectModel.setCountriesList(countriesList);

            JSONArray nationalitiesJsonArray = connectJsonObject.getJSONArray("nationalitiesList");
            List<NationalityModel> nationalitiesList = NationalityJsonParser
                    .getObjectFromJson(nationalitiesJsonArray.toString());
            connectModel.setNationalitiesList(nationalitiesList);

            JSONArray citiesJsonArray = connectJsonObject.getJSONArray("citiesList");
            List<CityModel> citiesList = CityJsonParser
                    .getObjectFromJson(citiesJsonArray.toString());
            connectModel.setCitiesList(citiesList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectModel;
    }
}
