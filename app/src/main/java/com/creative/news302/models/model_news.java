package com.creative.news302.models;

import com.firebase.client.ServerValue;
import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class model_news {



    String title;



    String titleH;
    String brief,briefH;
    String Description,DescriptionH;
    String imgurl;
    String categories;
    long count;
    HashMap<String, Object> timestampCreated = new HashMap<>();

    public model_news(String title, String brief, String description, String imgurl, String category, long count,String titleH,String briefH,String DescriptionH) {
        this.title = title;
        this.brief = brief;
        Description = description;
        this.titleH = titleH;
        this.briefH = briefH;
        this.DescriptionH = DescriptionH;
        this.imgurl = imgurl;
        this.categories = category;
        this.count = count;

        timestampCreated.put("timestamp", ServerValue.TIMESTAMP);
    }

    public model_news()
    {

    }

    public String getTitleH() {
        return titleH;
    }

    public void setTitleH(String titleH) {
        this.titleH = titleH;
    }

    public String getBriefH() {
        return briefH;
    }

    public void setBriefH(String briefH) {
        this.briefH = briefH;
    }

    public String getDescriptionH() {
        return DescriptionH;
    }

    public void setDescriptionH(String descriptionH) {
        DescriptionH = descriptionH;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(HashMap<String, Object> timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    @Exclude
    public long getTimestampCreatedLong(){
        return (long)timestampCreated.get("timestamp");
    }
}
