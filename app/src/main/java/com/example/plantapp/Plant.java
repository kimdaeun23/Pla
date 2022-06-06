package com.example.plantapp;

public class Plant {
    String name;
    String water_cycle;
    String imageUrl;
    String birthday;
    String dday;
    String water_lastday;
    int flower_water;
    int flower_light;


    public Plant(String name,String image, String water_cycle, String imageUrl, String birthday, String dday, String water_lastday, int flower_water, int flower_light){
        this.name=name;
        this.water_cycle=water_cycle;
        this.imageUrl=imageUrl;
        this.birthday=birthday;
        this.water_lastday=water_lastday;
        this.flower_water=flower_water;
        this.flower_light=flower_light;
    }

    public Plant(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWater_cycle() {
        return water_cycle;
    }

    public void setWater_cycle(String water_cycle) {
        this.water_cycle = water_cycle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDday() {
        return dday;
    }

    public void setDday(String dday) {
        this.dday = dday;
    }

    public String getWater_lastday() {
        return water_lastday;
    }

    public void setWater_lastday(String water_lastday) {
        this.water_lastday = water_lastday;
    }

    public int getFlower_water() {
        return flower_water;
    }

    public void setFlower_water(int flower_water) {
        this.flower_water = flower_water;
    }

    public int getFlower_light() {
        return flower_light;
    }

    public void setFlower_light(int flower_light) {
        this.flower_light = flower_light;
    }
}
