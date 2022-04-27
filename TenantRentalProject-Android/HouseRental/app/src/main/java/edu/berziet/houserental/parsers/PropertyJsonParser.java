package edu.berziet.houserental.parsers;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.models.PropertyModel;

public class PropertyJsonParser {
    public static PropertyModel getObjectFromJson(String json) {
        try {
                JSONObject jsonObject = new JSONObject(json);
                PropertyModel property = new PropertyModel();
                property.setId(jsonObject.getInt("id"));
                property.setSurfaceArea(jsonObject.getInt("surfaceArea"));
                property.setBedroomsCount(jsonObject.getInt("bedroomsCount"));
                property.setRentalPrice(jsonObject.getInt("rentalPrice"));
                property.setStatus(jsonObject.getString("status"));
                property.setHasBalcony(jsonObject.getBoolean("hasBalcony"));
                property.setHasGarden(jsonObject.getBoolean("hasGarden"));
                property.setConstructionYear(jsonObject.getInt("constructionYear"));
                property.setAvailabilityDate(jsonObject.getString("availabilityDate"));
                property.setDescription(jsonObject.getString("description"));
                property.setRented(jsonObject.getBoolean("rented"));
                property.setAdvertiseDate(jsonObject.getString("advertiseDate"));
                property.setCityId(jsonObject.getInt("cityId"));
                property.setAgencyId(jsonObject.getString("agencyId"));
                JSONArray imagesArray = jsonObject.getJSONArray("imagesList");
                ArrayList<PropertyImage> imagesList = new ArrayList<>();
                for(int k=0;k<imagesArray.length();k++){
                    JSONObject jsonObject1 = imagesArray.getJSONObject(k);
                    PropertyImage image = PropertyImageJsonParser.getObjectFromJson(jsonObject1.toString());
                    imagesList.add(image);
                }
                property.setImagesList(imagesList);
            return property;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
