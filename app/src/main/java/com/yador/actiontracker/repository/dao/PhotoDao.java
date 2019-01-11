package com.yador.actiontracker.repository.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.yador.actiontracker.repository.entity.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Photo... photos);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    int update(Photo... photos);

    @Query("SELECT * FROM Photo WHERE id=:photoId LIMIT 1")
    Photo findById(long photoId);

    @Query("SELECT * FROM Photo WHERE recordId=:recordId")
    LiveData<List<Photo>> findByRecordId(long recordId);

    @Delete
    void delete(Photo photo);
}
