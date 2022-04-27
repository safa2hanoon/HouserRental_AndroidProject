package edu.berziet.houserental.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import edu.berziet.houserental.models.PropertyModel;
import edu.berziet.houserental.models.RentalRequestModel;

public class RentalRequestModelJsonParser {
    public static RentalRequestModel getObjectFromJson(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            RentalRequestModel model = new RentalRequestModel();
            model.setRequestId(jsonObject.getInt("requestId"));
            model.setPropertyId(jsonObject.getInt("propertyId"));
            model.setResult(jsonObject.getString("result"));
            model.setResultId(jsonObject.getInt("resultId"));
            model.setTenantId(jsonObject.getString("tenantId"));
            model.setTenantName(jsonObject.getString("tenantName"));
            PropertyModel property = PropertyJsonParser.getObjectFromJson(
                    jsonObject.getJSONObject("property").toString()
            );
            model.setProperty(property);
            return model;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
