package edu.berziet.houserental.parsers;

import org.json.JSONObject;
import edu.berziet.houserental.models.PropertyPostResponseModel;

public class PropertyPostResponseJsonParser {
    public static PropertyPostResponseModel getObjectFromJson(String json){
        try {
            JSONObject jsonObject1 = new JSONObject(json);
                PropertyPostResponseModel postResponse = new PropertyPostResponseModel();
                postResponse.setSuccess(jsonObject1.getBoolean("success"));
                postResponse.setErrorMessage(jsonObject1.getString("errorMessage"));
                postResponse.setPropertyId(jsonObject1.getInt("propertyId"));
                return postResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
