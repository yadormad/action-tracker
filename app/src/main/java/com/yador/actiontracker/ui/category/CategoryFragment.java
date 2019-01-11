package com.yador.actiontracker.ui.category;

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
import com.yador.actiontracker.repository.entity.Category;

public class CategoryFragment extends Fragment {
    private OnCategoriesFragmentInteractionListener mListener;


    public CategoryFragment() {
    }

    @SuppressWarnings("unused")
    public static CategoryFragment newInstance(int columnCount) {
        return new CategoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            final CategoryListViewModel viewModel =
                    ViewModelProviders.of(this).get(CategoryListViewModel.class);
            viewModel.getCategories().observe(this, categories -> recyclerView.setAdapter(new CategoriesRecyclerViewAdapter(categories, mListener, getContext(), CategoryFragment.this)));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCategoriesFragmentInteractionListener) {
            mListener = (OnCategoriesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCategoriesFragmentInteractionListener {
        void onCategoriesFragmentInteraction(Category item);
    }
}