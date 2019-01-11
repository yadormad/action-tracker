package com.yador.actiontracker.repository.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Category implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String description;
    @ForeignKey(entity = Photo.class,
            parentColumns = "id",
            childColumns = "photoId",
            onDelete = ForeignKey.CASCADE)
    private long photoId;
    private int sum;

    public Category(@NonNull String title, long phId) {
        this.title = title;
        this.photoId = phId;
    }

    public Category(int sum, long catId, long photoId, String title) {
        this.sum = sum;
        this.id = catId;
        this.photoId = photoId;
        this.title = title;
    }

    public Category() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }



    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
