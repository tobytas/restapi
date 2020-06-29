package com.gmail.wondergab12.restapi.repo.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"unused", "NullableProblems"})
@Entity(tableName = "cats")
public class Cat {

    @PrimaryKey
    @NonNull

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("url")
    private String url;

    public Cat(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
