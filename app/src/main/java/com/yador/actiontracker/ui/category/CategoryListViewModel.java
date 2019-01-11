package com.yador.actiontracker.ui.category;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.yador.actiontracker.BasicApp;
import com.yador.actiontracker.repository.DataRepository;
import com.yador.actiontracker.repository.entity.Category;

import java.util.Date;
import java.util.List;

public class CategoryListViewModel extends AndroidViewModel {
    private final DataRepository repository;
    private final MediatorLiveData<List<Category>> observableCategories;

    public CategoryListViewModel(Application application) {
        super(application);

        observableCategories = new MediatorLiveData<>();
        observableCategories.setValue(null);
        repository = ((BasicApp) application).getRepository();
        LiveData<List<Category>> records = repository.getCategories();
        observableCategories.addSource(records, observableCategories::setValue);
    }

    public LiveData<List<Category>> getCategories() {
        return observableCategories;
    }

    public List<Category> getMostSum() {
        return repository.getMostSum();
    }

    public List<Category> getMostSum(Date start, Date end) {
        return repository.getMostSum(start, end);
    }

    public List<Category> getSum() {
        return repository.getSum();
    }

    public List<Category> getSum(Date start, Date end) {
        return repository.getSum(start, end);
    }
}
