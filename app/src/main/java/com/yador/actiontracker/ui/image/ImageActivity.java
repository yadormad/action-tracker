package com.yador.actiontracker.ui.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yador.actiontracker.Constants;
import com.yador.actiontracker.R;
import com.yador.actiontracker.repository.entity.Photo;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageActivity extends AppCompatActivity {
    private EditText text;
    private Photo photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView imageView = findViewById(R.id.imageView);
        text = findViewById(R.id.editText);
        photo = (Photo)getIntent().getSerializableExtra(Constants.CURRENT_PHOTO);
        try {
            final InputStream imageStream = getContentResolver().openInputStream(Uri.parse(photo.getImageUri()));
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(selectedImage);
        } catch (FileNotFoundException err) {
            Toast.makeText(getApplicationContext(), "Can't load image", Toast.LENGTH_LONG).show();
            finish();
        }
        if (photo.getDescription() != null) {
            text.setText(photo.getDescription());
        }
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            photo.setDescription(text.getText().toString());
            getIntent().putExtra(Constants.CURRENT_PHOTO, photo);
            setResult(Constants.RESULT_PHOTO, getIntent());
            finish();
        });
    }
}
