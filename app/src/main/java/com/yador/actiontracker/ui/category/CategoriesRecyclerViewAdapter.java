package com.yador.actiontracker.ui.category;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yador.actiontracker.Constants;
import com.yador.actiontracker.R;
import com.yador.actiontracker.repository.AppDatabase;
import com.yador.actiontracker.repository.dao.PhotoDao;
import com.yador.actiontracker.repository.entity.Category;
import com.yador.actiontracker.repository.entity.Photo;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.ViewHolder> {

    private List<Category> mValues;
    private final CategoryFragment.OnCategoriesFragmentInteractionListener mListener;
    private final PhotoDao photoDAO;
    private Fragment owner;

    public CategoriesRecyclerViewAdapter(List<Category> categories, CategoryFragment.OnCategoriesFragmentInteractionListener listener, Context context, Fragment owner) {
        this.owner = owner;
        if (categories == null) {
            mValues = new ArrayList<>();
        } else {
            mValues = categories;
        }
        mListener = listener;
        photoDAO = AppDatabase.getInstance(context).getPhotoDAO();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCategoryTitle.setText(mValues.get(position).getTitle());
        try {
            Photo photo = photoDAO.findById(mValues.get(position).getPhotoId());
            final Uri imageUri = Uri.parse(photo.getImageUri());
            final InputStream imageStream = Objects.requireNonNull(owner.getContext()).getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            holder.mCategoryIcon.setImageBitmap(selectedImage);
            holder.mView.setOnClickListener(v -> {
                if (null != mListener) {
                    Intent intent = new Intent(owner.getContext(), CategoryActivity.class);
                    intent.putExtra(Constants.CATEGORY_MODEL, mValues.get(holder.getAdapterPosition()));
                    owner.startActivity(intent);
                    mListener.onCategoriesFragmentInteraction(holder.mItem);
                }
            });
        } catch (FileNotFoundException err) {
            Toast.makeText(owner.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mCategoryIcon;
        final TextView mCategoryTitle;
        Category mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mCategoryIcon = view.findViewById(R.id.category_icon);
            mCategoryTitle = view.findViewById(R.id.category_title);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mCategoryTitle.getText() + "'";
        }
    }
}
