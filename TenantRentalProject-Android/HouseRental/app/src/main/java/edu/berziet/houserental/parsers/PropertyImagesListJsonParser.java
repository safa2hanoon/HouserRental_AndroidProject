package edu.berziet.houserental.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.berziet.houserental.models.PropertyImage;

public class PropertyImagesListJsonParser {
    public static ArrayList<PropertyImage> getObjectFromJson(String json) {
        ArrayList<PropertyImage> imagesList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PropertyImage image = new PropertyImage();
                image.setImageId(jsonObject.getInt("id"));
                image.setFileName(jsonObject.getString("fileName"));
                imagesList.add(image);
            }
            return imagesList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
