package com.yador.actiontracker.repository.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Photo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String description;
    private String imageUri;
    @ForeignKey(entity = Record.class,
            parentColumns = "id",
            childColumns = "recordId")
    private long recordId;

    public Photo(String imageUri) {
        this.imageUri = imageUri;
    }

    public Photo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }
}
