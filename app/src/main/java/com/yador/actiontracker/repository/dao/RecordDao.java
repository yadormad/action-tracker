package com.yador.actiontracker.repository.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.yador.actiontracker.repository.entity.Record;

import java.util.List;

@Dao
public interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Record record);

    @Query("SELECT * FROM RECORD")
    LiveData<List<Record>> getAll();

    @Query("SELECT * FROM Record WHERE id=:recordId")
    LiveData<Record> findById(long recordId);

    @Query("SELECT * FROM Record ORDER BY categoryId, duration DESC")
    LiveData<List<Record>> getAllSortByCat();

    @Delete
    void delete(Record record);

    @Update
    void update(Record record);
}
