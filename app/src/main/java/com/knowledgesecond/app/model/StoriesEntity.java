package com.knowledgesecond.app.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by zsg95 on 2016/11/15.
 */

public class StoriesEntity implements Serializable{

    /**
     * id : 7047795
     * title : 央视说要干预男男性行为，具体是怎么干预法？
     * ga_prefix : 081310
     * images : ["http://pic3.zhimg.com/fe27abc8f094510f2d3b4f3706108b56.jpg"]
     * type : 0
     */

    private int id;
    private String title;
    private List<String> images;
    private int type;

    public void setId(int id){
        this.id=id;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public void setImages(List<String> images){
        this.images=images;
    }

    public void setType(int type){
        this.type=type;
    }

    public int getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public List<String> getImages(){
        return images;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString(){
        return "StoriesEntity{" + "id=" + id + ", title='" + title + '\'' + ", images=" + images + ", type=" + type + '}';
    }

}
