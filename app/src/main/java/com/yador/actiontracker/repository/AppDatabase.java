package com.yador.actiontracker.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.yador.actiontracker.R;
import com.yador.actiontracker.repository.dao.CategoryDao;
import com.yador.actiontracker.repository.dao.PhotoDao;
import com.yador.actiontracker.repository.dao.RecordDao;
import com.yador.actiontracker.repository.entity.Category;
import com.yador.actiontracker.repository.entity.Photo;
import com.yador.actiontracker.repository.entity.Record;

import java.util.concurrent.Executors;

@Database(entities = {Photo.class, Category.class, Record.class},
        version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();


    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    public abstract CategoryDao getCategoryDAO();

    public abstract PhotoDao getPhotoDAO();

    public abstract RecordDao getRecordDAO();

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "my-database")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                long[] ids = getInstance(context).getPhotoDAO().insert(getCategoriesPhotos());
                                getInstance(context).getCategoryDAO().insert(getInitCategories(ids));
                                getInstance(context).setDatabaseCreated();
                            }

                            private Category[] getInitCategories(long[] phIds) {
                                return new Category[]{
                                        new Category("Cleaning", phIds[0]),
                                        new Category("Dinner", phIds[1]),
                                        new Category("Dream", phIds[2]),
                                        new Category("Rest", phIds[3]),
                                        new Category("Work", phIds[4]),
                                };
                            }

                            private Photo[] getCategoriesPhotos() {
                                return new Photo[]{
                                        new Photo("android.resource://com.yador.actiontracker/" + R.mipmap.home_solid),
                                        new Photo("android.resource://com.yador.actiontracker/" + R.mipmap.utensils_solid),
                                        new Photo("android.resource://com.yador.actiontracker/" + R.mipmap.bed_solid),
                                        new Photo("android.resource://com.yador.actiontracker/" + R.mipmap.umbrella_beach_solid),
                                        new Photo("android.resource://com.yador.actiontracker/" + R.mipmap.briefcase_solid)
                                };
                            }
                        });
                    }
                })
                .build();
    }

    LiveData<Boolean> getDatabaseCreated() {
        return isDatabaseCreated;
    }

    private void setDatabaseCreated() {
        isDatabaseCreated.postValue(true);
    }

}
