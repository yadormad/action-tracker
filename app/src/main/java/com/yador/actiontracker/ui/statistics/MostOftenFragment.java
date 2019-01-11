package com.yador.actiontracker.ui.statistics;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yador.actiontracker.R;
import com.yador.actiontracker.ui.record.RecordsRecyclerViewAdapter;
import com.yador.actiontracker.ui.record.RecordsFragment;
import com.yador.actiontracker.ui.record.RecordsListViewModel;

public class MostOftenFragment extends Fragment {
    private RecordsFragment.OnRecordsListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mostoften, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            final RecordsListViewModel viewModel =
                    ViewModelProviders.of(this).get(RecordsListViewModel.class);
            viewModel.getRecords().observe(this, records -> recyclerView.setAdapter(new RecordsRecyclerViewAdapter(records, mListener, getContext(), MostOftenFragment.this)));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecordsFragment.OnRecordsListFragmentInteractionListener) {
            mListener = (RecordsFragment.OnRecordsListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onRecordsListFragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
