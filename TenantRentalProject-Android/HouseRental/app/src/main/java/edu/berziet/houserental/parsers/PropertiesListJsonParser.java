package edu.berziet.houserental.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.berziet.houserental.models.PropertyModel;

public class PropertiesListJsonParser {
    public static ArrayList<PropertyModel> getListFromJsonArray(String jsonArrayString){
        ArrayList<PropertyModel> propertiesList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonArrayString);
            for(int l=0;l<jsonArray.length();l++){
                JSONObject jsonObject = jsonArray.getJSONObject(l);
                PropertyModel propertyModel = PropertyJsonParser.getObjectFromJson(jsonObject.toString());
                if(propertyModel!=null){
                    propertiesList.add(propertyModel);
                }else{
                    return null;
                }
            }
            return propertiesList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
