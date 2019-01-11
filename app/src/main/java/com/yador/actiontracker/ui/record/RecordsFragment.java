package com.yador.actiontracker.ui.record;

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
import com.yador.actiontracker.repository.entity.Record;

public class RecordsFragment extends Fragment {
    private OnRecordsListFragmentInteractionListener mListener;


    public RecordsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            final RecordsListViewModel viewModel =
                    ViewModelProviders.of(this).get(RecordsListViewModel.class);
            viewModel.getRecords().observe(this, records -> recyclerView.setAdapter(new RecordsRecyclerViewAdapter(records, mListener, getContext(), RecordsFragment.this)));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecordsListFragmentInteractionListener) {
            mListener = (OnRecordsListFragmentInteractionListener) context;
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

    public interface OnRecordsListFragmentInteractionListener {
        void onRecordsListFragmentInteraction(Record item);
    }

}

