package edu.berziet.houserental.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import edu.berziet.houserental.models.TenantModel;
import edu.berziet.houserental.models.TenantProfileResponseModel;

public class TenantProfileJsonParser {
    public static TenantProfileResponseModel getObjectFromJson(String json){
        try {
            JSONObject responseJsonObject = new JSONObject(json);
            TenantProfileResponseModel tenantProfileResponseModel = new TenantProfileResponseModel();
            boolean exist = responseJsonObject.getBoolean("exist");
            tenantProfileResponseModel.setExist(exist);
            if(exist) {
                TenantModel tenant = new TenantModel();
                JSONObject tenantJsonObject = responseJsonObject.getJSONObject("tenant");
                tenant.setEmailAddress(tenantJsonObject.getString("emailAddress"));
                tenant.setFirstName(tenantJsonObject.getString("firstName"));
                tenant.setLastName(tenantJsonObject.getString("lastName"));
                tenant.setGender(tenantJsonObject.getString("gender"));
                tenant.setMonthlySalary(tenantJsonObject.getInt("monthlySalary"));
                tenant.setOccupation(tenantJsonObject.getString("occupation"));
                tenant.setFamilySize(tenantJsonObject.getInt("familySize"));
                tenant.setPhoneNumber(tenantJsonObject.getString("phoneNumber"));
                tenant.setCityId(tenantJsonObject.getInt("cityId"));
                tenant.setNationalityId(tenantJsonObject.getInt("nationalityId"));
                tenant.setCountryId(tenantJsonObject.getInt("countryId"));
                tenant.setCountryName(tenantJsonObject.getString("countryName"));
                tenant.setCityName(tenantJsonObject.getString("cityName"));
                tenant.setTenantNationality(tenantJsonObject.getString("tenantNationality"));
                tenantProfileResponseModel.setTenant(tenant);
            }
            return tenantProfileResponseModel;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
