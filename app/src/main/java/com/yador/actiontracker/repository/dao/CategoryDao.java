package com.yador.actiontracker.repository.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.yador.actiontracker.repository.entity.Category;

import java.util.Date;
import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category... category);

    @Query("SELECT * FROM Category")
    LiveData<List<Category>> getAll();

    @Query("SELECT * FROM Category WHERE id=:categoryId LIMIT 1")
    LiveData<Category> findById(long categoryId);

    @Query("Select sum(duration) as sum, category.id, photoId, category.title from category join record on categoryId = category.id group by category.id, photoId order by sum DESC")
    List<Category> getMostSum();

    @Query("Select sum(duration) as sum, category.id, photoId, category.title from category join record on categoryId = category.id and startTime >= :start and endTime <=:end group by category.id, photoId order by sum DESC")
    List<Category> getMostSum(Date start, Date end);

    @Query("select sum(duration) as sum, category.id, photoId, category.title from category join record on categoryId = category.id group by category.id, photoId")
    List<Category> getSum();

    @Query("select sum(duration) as sum, category.id, photoId, category.title from category join record on categoryId = category.id and startTime >= :start and endTime <=:end group by category.id, photoId")
    List<Category> getSum(Date start, Date end);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);
}
