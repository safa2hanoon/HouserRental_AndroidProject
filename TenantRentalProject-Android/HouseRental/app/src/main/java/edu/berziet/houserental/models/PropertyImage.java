package edu.berziet.houserental.models;

public class PropertyImage {
    private int imageId;
    private String fileName;

    public PropertyImage() {
    }

    public PropertyImage(int imageId, String fileName) {
        this.imageId = imageId;
        this.fileName = fileName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
