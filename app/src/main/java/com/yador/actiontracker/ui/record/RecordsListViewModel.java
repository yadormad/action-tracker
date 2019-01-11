package com.yador.actiontracker.ui.record;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.yador.actiontracker.BasicApp;
import com.yador.actiontracker.repository.DataRepository;
import com.yador.actiontracker.repository.entity.Record;

import java.util.List;

public class RecordsListViewModel extends AndroidViewModel {
    private final MediatorLiveData<List<Record>> observableRecords;

    public RecordsListViewModel(Application application) {
        super(application);

        observableRecords = new MediatorLiveData<>();
        observableRecords.setValue(null);
        DataRepository repository = ((BasicApp) application).getRepository();
        LiveData<List<Record>> records = repository.getRecords();
        observableRecords.addSource(records, observableRecords::setValue);
    }

    public LiveData<List<Record>> getRecords() {
        return observableRecords;
    }
}