package edu.berziet.houserental.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import edu.berziet.houserental.models.AgencyProfileResponseModel;
import edu.berziet.houserental.models.RentingAgencyModel;

public class RentingAgencyResponseJsonParser {
    public static AgencyProfileResponseModel getObjectFromJson(String json){
        try {
            AgencyProfileResponseModel responseModel = new AgencyProfileResponseModel();
            JSONObject jsonObject = new JSONObject(json);
            boolean exist = jsonObject.getBoolean("exist");
            responseModel.setExist(exist);
            if(exist){
                JSONObject agencyProfileJson = jsonObject.getJSONObject("agencyProfile");
                RentingAgencyModel agencyModel = new RentingAgencyModel();
                agencyModel.setEmailAddress(agencyProfileJson.getString("emailAddress"));
                agencyModel.setPhoneNumber(agencyProfileJson.getString("phoneNumber"));
                agencyModel.setCityId(agencyProfileJson.getInt("cityId"));
                agencyModel.setCountryId(agencyProfileJson.getInt("countryId"));
                agencyModel.setCountryName(agencyProfileJson.getString("countryName"));
                agencyModel.setCityName(agencyProfileJson.getString("cityName"));
                agencyModel.setName(agencyProfileJson.getString("name"));
                responseModel.setAgency(agencyModel);
            }
            return responseModel;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
