package cn.edu.buct.se.cs1808.utils;


import com.baidu.mapapi.model.LatLng;

public class Museum {
    private int id;
    private String name;
    private String pos;
    private LatLng latLng;
    private String introduce;
    private String imageSrc;

    public void load(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getName() {
        return name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getPos() {
        return pos;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public void setName(String name) {
        this.name = name;
    }
}
