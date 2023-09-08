package com.demo.adnroid.surroundings;

import java.util.ArrayList;
import java.util.List;

public class weather {


    String locationName;
    String lat;
    String lon;
    String obsTime;
    String TEMP;
    String HUMD;


    public weather(String locationName, String lat, String lon, String obsTime, String TEMP, String HUMD) {
        this.locationName = locationName;
        this.lat = lat;
        this.lon = lon;
        this.obsTime = obsTime;
        this.TEMP = TEMP;
        this.HUMD = HUMD;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getObsTime() {
        return obsTime;
    }

    public void setObsTime(String obsTime) {
        this.obsTime = obsTime;
    }

    public String getTEMP() {
        return TEMP;
    }

    public void setTEMP(String TEMP) {
        this.TEMP = TEMP;
    }

    public String getHUMD() {
        return HUMD;
    }

    public void setHUMD(String HUMD) {
        this.HUMD = HUMD;
    }


}
