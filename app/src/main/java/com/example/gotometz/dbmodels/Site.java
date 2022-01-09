package com.example.gotometz.dbmodels;

import android.content.ContentValues;

/**
 * Model for sites/POI
 */
public class Site {

    private long id;
    private String label;
    private double latitude;
    private double longitude;
    private String postalAddress;
    private Category category;
    private String summary;

    public Site() {
    }

    public Site(long id, String label, double latitude, double longitude, String postalAddress, Category category, String summary) {
        this.setId(id);
        this.setLabel(label);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setPostalAddress(postalAddress);
        this.setCategory(category);
        this.setSummary(summary);
    }

    public Site(String label, double latitude, double longitude, String postalAddress, Category category, String summary) {
        this.setLabel(label);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setPostalAddress(postalAddress);
        this.setCategory(category);
        this.setSummary(summary);
    }

    public long getId() {
        return id;
    }
    public String getLabel() {
        return label;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getPostalAddress() {
        return postalAddress;
    }
    public Category getCategory() {
        return category;
    }
    public String getSummary() {
        return summary;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public static Site fromContentValues(ContentValues values) {
        final Site site = new Site();

        if (values.containsKey("id")) site.setId(values.getAsLong("id"));
        if (values.containsKey("label")) site.setLabel(values.getAsString("label"));
        if (values.containsKey("latitude")) site.setLatitude(values.getAsDouble("latitude"));
        if (values.containsKey("longitude")) site.setLongitude(values.getAsDouble("longitude"));
        if (values.containsKey("postalAddr")) site.setPostalAddress(values.getAsString("postalAddr"));
        if (values.containsKey("category")) site.setCategory((Category) values.get("category"));
        if (values.containsKey("summary")) site.setSummary(values.getAsString("summary"));

        return site;
    }
}
