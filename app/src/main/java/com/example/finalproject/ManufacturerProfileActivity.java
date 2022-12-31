package com.example.finalproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ManufacturerProfileActivity extends AppCompatActivity {
    private static final String MANUFACTURERS_COLLECTION = "manufacturers";

    private TextView textViewName;
    private TextView textViewAbout;
    private TextView textViewContact;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private Manufacturer manufacturer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer_profile);

        textViewName = findViewById(R.id.text_view_name);
        textViewAbout = findViewById(R.id.text_view_about);
        textViewContact = findViewById(R.id.text_view_contact);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String manufacturerId = firebaseAuth.getCurrentUser().getUid();
        firestore.collection(MANUFACTURERS_COLLECTION).document(manufacturerId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Hata oluştu.
                            return;
                        }
                        manufacturer = snapshot.toObject(Manufacturer.class);
                        manufacturer.setId(snapshot.getId());
                        textViewName.setText(manufacturer.getName());
                        textViewAbout.setText(manufacturer.getAbout());
                        textViewContact.setText(manufacturer.getContactInfo());
                    }
                });
    }
}
