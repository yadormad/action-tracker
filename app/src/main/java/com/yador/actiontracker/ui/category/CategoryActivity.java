package com.yador.actiontracker.ui.category;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yador.actiontracker.Constants;
import com.yador.actiontracker.R;
import com.yador.actiontracker.repository.AppDatabase;
import com.yador.actiontracker.repository.dao.PhotoDao;
import com.yador.actiontracker.repository.entity.Category;
import com.yador.actiontracker.repository.entity.Photo;
import com.yador.actiontracker.ui.image.ImageActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {
    private TextView title;
    private TextView description;
    private ImageView image;
    private PhotoDao photoDAO;
    private Category category;
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        title = findViewById(R.id.editTextTitle);
        description = findViewById(R.id.editTextDescription);
        image = findViewById(R.id.imageCatView);
        photoDAO = AppDatabase.getInstance(getApplicationContext()).getPhotoDAO();
        updateView();
        findViewById(R.id.button).setOnClickListener(v -> {
            category.setDescription(description.getText().toString());
            category.setTitle(title.getText().toString());
            if (photo != null) {
                int size = photoDAO.update(photo);
                if (size == 0) {
                    category.setPhotoId(photoDAO.insert(photo)[0]);
                }
            }
            AppDatabase.getInstance(getApplicationContext()).getCategoryDAO().update(category);
            finish();
        });
        image.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Constants.RESULT_LOAD_IMG);
        });
        image.setOnLongClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
            intent.putExtra(Constants.CURRENT_PHOTO, photo);
            startActivityForResult(intent, 1);
            return true;
        });
    }

    private void updateView() {
        category = (Category) getIntent().getSerializableExtra(Constants.CATEGORY_MODEL);
        if (category != null) {
            title.setText(category.getTitle());
            if (category.getDescription() != null) {
                description.setText(category.getDescription());
            }
            photo = photoDAO.findById(category.getPhotoId());
            updateImage();

        }
    }

    private void updateImage() {
        try {
            final Uri imageUri = Uri.parse(photo.getImageUri());
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            image.setImageBitmap(selectedImage);
        } catch (FileNotFoundException err) {
            Toast.makeText(getApplicationContext(), "Can't load image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Constants.RESULT_CANCEL) {
            if (resultCode == Constants.RESULT_PHOTO) {
                this.photo = (Photo) Objects.requireNonNull(data).getSerializableExtra(Constants.CURRENT_PHOTO);
                updateImage();
            } else {
                final Uri imageUri = Objects.requireNonNull(data).getData();
                photo.setImageUri(Objects.requireNonNull(imageUri).toString());
                updateImage();
            }
        }
    }


}
