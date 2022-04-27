package edu.berziet.houserental.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.berziet.houserental.models.PropertyModel;
import edu.berziet.houserental.models.RentalHistoryModel;

public class RentalHistoryModelJsonParser {
    public static RentalHistoryModel getObjectFromJson(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            RentalHistoryModel rentalHistoryModel = new RentalHistoryModel();
            rentalHistoryModel.setId(jsonObject.getInt("id"));
            rentalHistoryModel.setTenantId(jsonObject.getString("tenantId"));
            rentalHistoryModel.setTenantName(jsonObject.getString("tenantName"));
            PropertyModel property = PropertyJsonParser.getObjectFromJson(
                    jsonObject.getJSONObject("property").toString()
            );
            rentalHistoryModel.setProperty(property);
            return rentalHistoryModel;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<RentalHistoryModel> getArrayFromJson(String json){
        ArrayList<RentalHistoryModel> historyList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0 ; i<jsonArray.length();i++){
                RentalHistoryModel historyModel =
                        getObjectFromJson(jsonArray.getJSONObject(i).toString());
                historyList.add(historyModel);
            }
            return historyList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
