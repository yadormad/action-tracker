package com.yador.actiontracker.ui.statistics;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yador.actiontracker.R;
import com.yador.actiontracker.repository.entity.Category;
import com.yador.actiontracker.ui.category.CategoryListViewModel;
import com.yador.actiontracker.ui.date.DateTimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class PieFragment extends Fragment {
    private DateTimePicker startDateTimePicker;
    private DateTimePicker endDateTimePicker;
    private Date start;
    private Date end;
    private SimpleDateFormat myDateFormat;
    private TextView startDateView;
    private TextView endDateView;
    private CategoryListViewModel viewModel;
    private PieChartView pieChartView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pie, container, false);
        myDateFormat = new SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault());
        startDateView = view.findViewById(R.id.startDateView);
        endDateView = view.findViewById(R.id.endDateView);
        configureDateTimeFragments();
        pieChartView = view.findViewById(R.id.chart);
        viewModel = ViewModelProviders.of(this).get(CategoryListViewModel.class);
        view.findViewById(R.id.search_button).setOnClickListener(v -> new Find().doInBackground(start, end));
        return view;
    }


    private void configureDateTimeFragments() {
        startDateTimePicker = new DateTimePicker(getContext(), date -> {
            start = date;
            startDateView.setText(myDateFormat.format(date));
        });

        endDateTimePicker = new DateTimePicker(getContext(), date -> {
            end = date;
            endDateView.setText(myDateFormat.format(date));
        });

        startDateView.setOnClickListener(v -> startDateTimePicker.showDateDialog());

        endDateView.setOnClickListener(v -> endDateTimePicker.showDateDialog());
    }

    private class Find extends AsyncTask<Date, Void, Void> {
        @Override
        protected Void doInBackground(Date... objects) {
            List<SliceValue> pieData = new ArrayList<>();
            List<Category> categories;
            if (objects[0] != null && objects[1] != null) {
                categories = viewModel.getSum(objects[0], objects[1]);
            } else {
                categories = viewModel.getSum();
            }
            for (Category category : categories) {
                SliceValue sliceValue = new SliceValue(category.getSum(), Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                sliceValue.setLabel(category.getTitle());
                pieData.add(sliceValue);
            }
            PieChartData pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true);
            pieChartView.setPieChartData(pieChartData);
            return null;
        }
    }
}
