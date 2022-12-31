package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DesignDetailActivity extends AppCompatActivity {
    private static final String DESIGNS_COLLECTION = "designs";
    private static final String DESIGNERS_COLLECTION = "designers";

    private ImageView imageViewDesign;
    private TextView textViewDescription;
    private TextView textViewDesignerName;
    private Button buttonMakeOffer;
    private FirebaseFirestore firestore;
    private Design design;
    private Designer designer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_detail);

        imageViewDesign = findViewById(R.id.image_view_design);
        textViewDescription = findViewById(R.id.text_view_description);
        textViewDesignerName = findViewById(R.id.text_view_designer_name);
        buttonMakeOffer = findViewById(R.id.button_make_offer);

        firestore = FirebaseFirestore.getInstance();
        String designId = getIntent().getStringExtra("designId");
        firestore.collection(DESIGNS_COLLECTION).document(designId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Hata oluştu.
                            return;
                        }
                        design = snapshot.toObject(Design.class);
                        design.setId(snapshot.getId());
                        textViewDescription.setText(design.getDescription());
                        // Tasarımcının bilgilerini getir
                        String designerId = design.getId();
                        firestore.collection(DESIGNERS_COLLECTION).document(designerId)
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        designer = snapshot.toObject(Designer.class);
                                        designer.setId(snapshot.getId());
                                        textViewDesignerName.setText(designer.getName());
                                    }
                                });
                        // Resmi göster
                        Glide.with(DesignDetailActivity.this).load(
                                design.getImageUrl()).into(imageViewDesign);
                    }
                });
        buttonMakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DesignDetailActivity.this, MakeOfferActivity.class);
                intent.putExtra("designId", design.getId());
                intent.putExtra("designerId", designer.getId());
                startActivity(intent);
            }
        });
    }
}

