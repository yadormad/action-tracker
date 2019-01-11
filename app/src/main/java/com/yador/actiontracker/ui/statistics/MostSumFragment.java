package com.yador.actiontracker.ui.statistics;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yador.actiontracker.R;
import com.yador.actiontracker.ui.category.CategoryFragment;
import com.yador.actiontracker.ui.category.CategoryListViewModel;
import com.yador.actiontracker.ui.category.CategoriesRecyclerViewAdapter;
import com.yador.actiontracker.ui.date.DateTimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MostSumFragment extends Fragment {
    private CategoryFragment.OnCategoriesFragmentInteractionListener mListener;
    private DateTimePicker startDateTimePicker;
    private DateTimePicker endDateTimePicker;
    private Date start;
    private Date end;
    private SimpleDateFormat myDateFormat;
    private TextView startDateView;
    private TextView endDateView;
    private RecyclerView recyclerView;
    private CategoryListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.most_sum, container, false);
        myDateFormat = new SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault());
        startDateView = view.findViewById(R.id.startDateView);
        endDateView = view.findViewById(R.id.endDateView);
        configureDateTimeFragments();
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.most_sum_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewModel = ViewModelProviders.of(this).get(CategoryListViewModel.class);
        view.findViewById(R.id.search_button).setOnClickListener(v -> new Find().doInBackground(start, end));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryFragment.OnCategoriesFragmentInteractionListener) {
            mListener = (CategoryFragment.OnCategoriesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCategoryListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
            if (objects[0] != null && objects[1] != null) {
                recyclerView.setAdapter(new CategoriesRecyclerViewAdapter(viewModel.getMostSum(objects[0], objects[1]), mListener, getContext(), MostSumFragment.this));
            } else {
                recyclerView.setAdapter(new CategoriesRecyclerViewAdapter(viewModel.getMostSum(), mListener, getContext(), MostSumFragment.this));
            }
            return null;
        }
    }
}
