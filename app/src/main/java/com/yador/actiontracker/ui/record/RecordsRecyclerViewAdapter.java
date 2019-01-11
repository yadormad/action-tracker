package com.yador.actiontracker.ui.record;

import android.app.AlertDialog;
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
import com.yador.actiontracker.repository.dao.CategoryDao;
import com.yador.actiontracker.repository.dao.PhotoDao;
import com.yador.actiontracker.repository.dao.RecordDao;
import com.yador.actiontracker.repository.entity.Category;
import com.yador.actiontracker.repository.entity.Photo;
import com.yador.actiontracker.repository.entity.Record;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecordsRecyclerViewAdapter extends RecyclerView.Adapter<RecordsRecyclerViewAdapter.ViewHolder> {

    private List<Record> mValues;
    private final RecordsFragment.OnRecordsListFragmentInteractionListener mListener;
    private final CategoryDao categoryDAO;
    private final PhotoDao photoDAO;
    private final RecordDao recordDAO;
    private Fragment owner;
    private Category recordCategory;

    public RecordsRecyclerViewAdapter(List<Record> records, RecordsFragment.OnRecordsListFragmentInteractionListener listener, Context context, Fragment owner) {
        this.owner = owner;
        if (records == null) {
            mValues = new ArrayList<>();
        } else {
            mValues = records;
        }
        mListener = listener;
        categoryDAO = AppDatabase.getInstance(context).getCategoryDAO();
        photoDAO = AppDatabase.getInstance(context).getPhotoDAO();
        recordDAO = AppDatabase.getInstance(context).getRecordDAO();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_records, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        categoryDAO.findById(mValues.get(position).getCategoryId()).observe(owner, category -> {
            if (category == null) {
                recordCategory = new Category();
            } else {
                recordCategory = category;
                long phId = recordCategory.getId();
                try {
                    Photo photo = photoDAO.findById(phId);
                    final Uri imageUri = Uri.parse(photo.getImageUri());
                    final InputStream imageStream = Objects.requireNonNull(owner.getContext()).getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    holder.mCategoryIcon.setImageBitmap(selectedImage);
                } catch (FileNotFoundException err) {
                    Toast.makeText(owner.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                }

            }
        });
        holder.mRecordTitle.setText(mValues.get(position).getTitle());
        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                Intent intent = new Intent(owner.getContext(), RecordActivity.class);
                intent.putExtra(Constants.RECORD_MODEL, mValues.get(position));
                owner.startActivity(intent);
                mListener.onRecordsListFragmentInteraction(holder.mItem);
            }
        });
        if (owner instanceof RecordsFragment) {
            holder.mView.setOnLongClickListener(v -> {
                new AlertDialog.Builder(owner.getContext())
                        .setTitle("Confirm")
                        .setMessage("Do you really want to delete this record?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> recordDAO.delete(mValues.get(position)))
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mCategoryIcon;
        final TextView mRecordTitle;
        Record mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mCategoryIcon = view.findViewById(R.id.category_icon);
            mRecordTitle = view.findViewById(R.id.record_id);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mRecordTitle.getText() + "'";
        }
    }
}
