package com.yador.actiontracker.repository.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Record implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private Date startTime;
    private Date endTime;
    private long duration;
    @ForeignKey(entity = Category.class,
            parentColumns = "id",
            childColumns = "categoryId"
    )
    private long categoryId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
        if (startTime != null && endTime != null) {
            this.duration = (endTime.getTime() - startTime.getTime()) / 3600;
        }
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
        if (startTime != null && endTime != null) {
            this.duration = (endTime.getTime() - startTime.getTime()) / 3600;
        }
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
