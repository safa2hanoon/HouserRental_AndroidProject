package edu.berziet.houserental.parsers;

import org.json.JSONObject;

import edu.berziet.houserental.models.SimpleSuccessResponseModel;

public class SimpleSuccessResponseJsonParser {
    public static SimpleSuccessResponseModel getObjectFromJson(String json){
        try {
            JSONObject jsonObject1 = new JSONObject(json);
            SimpleSuccessResponseModel postResponse = new SimpleSuccessResponseModel();
                postResponse.setSuccess(jsonObject1.getBoolean("success"));
                postResponse.setMessage(jsonObject1.getString("message"));
                return postResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
