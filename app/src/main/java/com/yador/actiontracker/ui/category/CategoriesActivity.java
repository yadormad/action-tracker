package com.yador.actiontracker.ui.category;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yador.actiontracker.R;
import com.yador.actiontracker.repository.entity.Category;

public class CategoriesActivity extends AppCompatActivity
        implements CategoryFragment.OnCategoriesFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
    }

    @Override
    public void onCategoriesFragmentInteraction(Category item) {

    }

}
