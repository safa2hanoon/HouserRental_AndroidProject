package edu.berziet.houserental.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import edu.berziet.houserental.models.PropertyImage;

public class PropertyImageJsonParser {
    public static PropertyImage getObjectFromJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            PropertyImage image = new PropertyImage();
            image.setImageId(jsonObject.getInt("id"));
            image.setFileName(jsonObject.getString("fileName"));
            return image;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
