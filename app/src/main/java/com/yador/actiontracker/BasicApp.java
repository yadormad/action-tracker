package com.yador.actiontracker;

import android.app.Application;

import com.yador.actiontracker.repository.AppDatabase;
import com.yador.actiontracker.repository.DataRepository;

public class BasicApp extends Application {
    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
