package com.example.finalproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MakeOfferActivity extends AppCompatActivity {
    private static final String DESIGNS_COLLECTION = "designs";
    private static final String OFFERS_COLLECTION = "offers";

    private TextView textViewDesignDescription;
    private Button buttonMakeOffer;
    private EditText editTextOffer;

    private Design design;
    private FirebaseFirestore firestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer);

        textViewDesignDescription = findViewById(R.id.text_view_design_description);
        buttonMakeOffer = findViewById(R.id.button_make_offer);
        editTextOffer = findViewById(R.id.edit_text_offer);

        design = (Design) getIntent().getSerializableExtra("design");
        textViewDesignDescription.setText(design.getDescription());

        firestore = FirebaseFirestore.getInstance();
        buttonMakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String offer = editTextOffer.getText().toString().trim();
                if (offer.isEmpty()) {
                    Toast.makeText(MakeOfferActivity.this, "Lütfen bir teklif girin.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> data = new HashMap<>();
                data.put("designId", design.getId());
                data.put("offer", offer);
                firestore.collection(OFFERS_COLLECTION).add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String offerId = documentReference.getId();
                                firestore.collection(DESIGNS_COLLECTION).document(design.getId())
                                        .update("offerIds", FieldValue.arrayUnion(offerId))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(MakeOfferActivity.this, "Teklifiniz gönderildi.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MakeOfferActivity.this, "Teklif gönderilemedi.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}

