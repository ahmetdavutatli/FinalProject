package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Design;
import com.example.finalproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DesignAddActivity extends AppCompatActivity {

    private EditText mDescriptionEditText;
    private ImageView mImageView;
    private Button mSaveButton;

    private Design mDesign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_add);

        mDescriptionEditText = findViewById(R.id.edit_text_description);
        mImageView = findViewById(R.id.image_view_design);
        mSaveButton = findViewById(R.id.button_save);

        mDesign = new Design();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDesign();
            }
        });
    }

    private void saveDesign() {
        String description = mDescriptionEditText.getText().toString();
        Bitmap image = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();

        mDesign.setDescription(description);
        mDesign.setImageUrl(String.valueOf(image));

        // Burada tasarımın veritabanına kaydedilmesi gereken işlemler yapılabilir.
        // Örneğin:
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference designRef = database.getReference("designs").push();
        designRef.setValue(mDesign);
    }
}