package com.demo.adnroid.surroundings;

public class esp_data{
    int id;
    String sensor;
    String location;
    String value1;
    String value2;
    String value3;
    String pm25;
    String reading_time;
    int data_num;

    public esp_data(int id, String sensor, String location, String value1, String value2, String value3, String reading_time, int data_num, String pm25) {

        this.id = id;

        this.sensor = sensor;

        this.location = location;

        this.value1 = value1;

        this.value2 = value2;

        this.value3 = value3;

        this.pm25 = pm25;

        this.reading_time = reading_time;

        this.data_num = data_num;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getReading_time() {
        return reading_time;
    }

    public void setReading_time(String reading_time) {
        this.reading_time = reading_time;
    }

    public int getData_num() {
        return data_num;
    }

    public void setData_num(int data_num) {
        this.data_num = data_num;
    }
}
