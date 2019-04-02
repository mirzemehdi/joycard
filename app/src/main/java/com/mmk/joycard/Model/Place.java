package com.mmk.joycard.Model;

public class Place {

    private String imgUrl;
    private String placeTitle;


    public Place() {
    }

    public Place(String imgUrl, String placeTitle) {

        this.imgUrl = imgUrl;
        this.placeTitle = placeTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public void setPlaceTitle(String placeTitle) {
        this.placeTitle = placeTitle;
    }


}
