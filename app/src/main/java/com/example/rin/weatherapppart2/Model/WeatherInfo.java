package com.example.rin.weatherapppart2.Model;

public class WeatherInfo {
    private String country;
    private String city;
    private String lastUpdate;
    private String description;
    private String humidity;
    private String celsius;
    private String cloud;
    private String wild;
    private String weatherIcon;
    private String id;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCelsius() {
        return celsius;
    }

    public void setCelsius(String celsius) {
        this.celsius = celsius;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getWild() {
        return wild;
    }

    public void setWild(String wild) {
        this.wild = wild;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public WeatherInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WeatherInfo(String country, String city, String lastUpdate, String description, String humidity, String celsius, String cloud, String wild, String weatherIcon, String id) {
        this.country = country;
        this.city = city;
        this.lastUpdate = lastUpdate;
        this.description = description;
        this.humidity = humidity;
        this.celsius = celsius;
        this.cloud = cloud;
        this.wild = wild;
        this.weatherIcon = weatherIcon;
        this.id = id;
    }
}
