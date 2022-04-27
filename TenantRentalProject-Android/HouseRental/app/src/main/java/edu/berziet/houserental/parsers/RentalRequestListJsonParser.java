package edu.berziet.houserental.parsers;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import edu.berziet.houserental.models.RentalRequestModel;

public class RentalRequestListJsonParser {
    public static ArrayList<RentalRequestModel> getArrayFromJson(String json){
        ArrayList<RentalRequestModel> requestsList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i<jsonArray.length();i++){
                RentalRequestModel request =
                        RentalRequestModelJsonParser.getObjectFromJson(
                                jsonArray.getJSONObject(i).toString()
                        );
                requestsList.add(request);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestsList;
    }
}
