package com.yador.actiontracker.ui.record;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.yador.actiontracker.BasicApp;
import com.yador.actiontracker.Constants;
import com.yador.actiontracker.R;
import com.yador.actiontracker.repository.AppDatabase;
import com.yador.actiontracker.repository.DataRepository;
import com.yador.actiontracker.repository.entity.Category;
import com.yador.actiontracker.repository.entity.Photo;
import com.yador.actiontracker.repository.entity.Record;
import com.yador.actiontracker.ui.category.CategoryListViewModel;
import com.yador.actiontracker.ui.date.DateTimePicker;
import com.yador.actiontracker.ui.image.ImageActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RecordActivity extends AppCompatActivity {
    private Record record;
    private Spinner spinner;
    private AppDatabase appDatabase;
    private DataRepository repository;
    private FlexboxLayout imageLayout;
    private DateTimePicker startDateTimePicker;
    private DateTimePicker endDateTimePicker;
    private TextView startDateView;
    private TextView endDateView;
    private Date start;
    private Date end;
    private Category chosenCategory;
    private List<Category> allCategories;
    private ArrayList<Photo> addedPhotos;
    private ArrayList<Photo> recentlyAddedPhotos = new ArrayList<>();

    private SimpleDateFormat myDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = AppDatabase.getInstance(this);
        repository = ((BasicApp) getApplication()).getRepository();
        setContentView(R.layout.activity_record);
        spinner = findViewById(R.id.spinner);
        final CategoryListViewModel viewModel =
                ViewModelProviders.of(this).get(CategoryListViewModel.class);
        allCategories = new ArrayList<>();
        viewModel.getCategories().observe(this, categories -> {
            if (categories == null) {
                allCategories = new ArrayList<>();
            } else {
                allCategories = categories;
                changeSpinnerItems();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenCategory = allCategories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageLayout = findViewById(R.id.imagesLayout);
        addedPhotos = new ArrayList<>();
        Button addImageButton = findViewById(R.id.addImage);
        addImageButton.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Constants.RESULT_LOAD_IMG);
        });

        myDateFormat = new SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault());
        startDateView = findViewById(R.id.startDateView);
        endDateView = findViewById(R.id.endDateView);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveRecord());

        configureDateTimePickers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIncomeData();
    }

    private void configureDateTimePickers() {
        startDateTimePicker = new DateTimePicker(RecordActivity.this, date -> {
            start = date;
            startDateView.setText(myDateFormat.format(date));
        });

        endDateTimePicker = new DateTimePicker(RecordActivity.this, date -> {
            end = date;
            endDateView.setText(myDateFormat.format(date));
        });

        startDateView.setOnClickListener(v -> startDateTimePicker.showDateDialog());

        endDateView.setOnClickListener(v -> endDateTimePicker.showDateDialog());
    }

    private void changeSpinnerItems() {
        List<String> titles = new ArrayList<>();
        for (Category category : allCategories) {
            titles.add(category.getTitle());
        }
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, titles);
        spinner.setAdapter(spinnerAdapter);
    }

    private void saveRecord() {
        record.setCategoryId(chosenCategory.getId());
        record.setEndTime(end);
        record.setStartTime(start);
        EditText title = findViewById(R.id.editTitle);
        record.setTitle(title.getText().toString());
        long recId = appDatabase.getRecordDAO().insert(record);
        Photo[] photosToUpdate = new Photo[recentlyAddedPhotos.size()];
        Photo[] photosToInsert = new Photo[addedPhotos.size() - recentlyAddedPhotos.size()];
        int indexU = 0;
        int indexI = 0;
        for (Photo photo : addedPhotos) {
            Photo temp = new Photo();
            temp.setImageUri(photo.getImageUri());
            temp.setRecordId(recId);
            temp.setDescription(photo.getDescription());
            if (recentlyAddedPhotos.contains(photo)) {
                photosToUpdate[indexU] = temp;
                indexU++;
            } else {
                photosToInsert[indexI] = temp;
                indexI++;
            }
        }

        appDatabase.getPhotoDAO().update(photosToUpdate);
        appDatabase.getPhotoDAO().insert(photosToInsert);
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Constants.RESULT_CANCEL) {
            if (resultCode == Constants.RESULT_PHOTO) {
                Photo intentPhoto = (Photo) Objects.requireNonNull(data).getSerializableExtra(Constants.CURRENT_PHOTO);
                for (Photo photo : addedPhotos) {
                    if (photo.getId() == intentPhoto.getId()) {
                        photo.setDescription(intentPhoto.getDescription());
                    }
                }
            } else {
                try {
                    final Uri imageUri = Objects.requireNonNull(data).getData();
                    final InputStream imageStream = getContentResolver().openInputStream(Objects.requireNonNull(imageUri));
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setImageBitmap(selectedImage);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
                    final Photo photo = new Photo(imageUri.toString());
                    imageView.setOnClickListener(v -> {
                        Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                        intent.putExtra(Constants.CURRENT_PHOTO, photo);
                        startActivityForResult(intent, 1);
                    });
                    imageLayout.addView(imageView);

                    addedPhotos.add(photo);
                } catch (FileNotFoundException | NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void getIncomeData() {
        if (getIntent().hasExtra(Constants.RECORD_MODEL)) {
            record = (Record) getIntent().getSerializableExtra(Constants.RECORD_MODEL);
            getIntent().removeExtra(Constants.RECORD_MODEL);

            updateView();
        } else {
            record = new Record();
        }
    }

    private void updateView() {
        if (record.getStartTime() != null) {
            startDateView.setText(myDateFormat.format(record.getStartTime()));
        }
        if (record.getEndTime() != null) {
            endDateView.setText(myDateFormat.format(record.getEndTime()));
        }
        ((EditText) findViewById(R.id.editTitle)).setText(record.getTitle());
        repository.getPhotosByRecord(record.getId()).observe(this, photos -> {
            if (photos != null) {
                try {
                    for (final Photo photo : photos) {
                        addedPhotos.add(photo);
                        recentlyAddedPhotos.add(photo);
                        final Uri imageUri = Uri.parse(photo.getImageUri());
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ImageView imageView = new ImageView(getApplicationContext());
                        imageView.setOnClickListener(v -> {
                            Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                            intent.putExtra(Constants.CURRENT_PHOTO, photo);
                            startActivityForResult(intent, 1);
                        });
                        imageView.setImageBitmap(selectedImage);
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
                        imageLayout.addView(imageView);
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
