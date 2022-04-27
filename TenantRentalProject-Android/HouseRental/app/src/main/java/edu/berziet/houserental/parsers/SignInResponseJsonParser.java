package edu.berziet.houserental.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import edu.berziet.houserental.models.SingInResponseModel;

public class SignInResponseJsonParser {
    public static SingInResponseModel getObjectFromJson(String json) {
        try {
            SingInResponseModel responseModel = new SingInResponseModel();
            JSONObject jsonObject = new JSONObject(json);
            responseModel.setSuccess(jsonObject.getBoolean("success"));
            responseModel.setRole(jsonObject.getString("role"));
            responseModel.setErrorMessage(jsonObject.getString("errorMessage"));
            responseModel.setCountryId(jsonObject.getInt("countryId"));
            responseModel.setCityId(jsonObject.getInt("cityId"));
            responseModel.setCountryName(jsonObject.getString("countryName"));
            responseModel.setCityName(jsonObject.getString("cityName"));
            responseModel.setName(jsonObject.getString("name"));
            return responseModel;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
