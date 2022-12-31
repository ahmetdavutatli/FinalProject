package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DesignerProfileActivity extends AppCompatActivity {
    private static final String DESIGNS_COLLECTION = "designs";
    private static final String DESIGNERS_COLLECTION = "designers";

    private TextView textViewName;
    private RecyclerView recyclerViewDesigns;
    private Button buttonAddDesign;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private Designer designer;
    private List<Design> designs;
    private DesignListAdapter designListAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_profile);

        textViewName = findViewById(R.id.text_view_name);
        recyclerViewDesigns = findViewById(R.id.recycler_view_designs);
        buttonAddDesign = findViewById(R.id.button_add_design);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String designerId = firebaseAuth.getCurrentUser().getUid();
        firestore.collection(DESIGNERS_COLLECTION).document(designerId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Hata oluştu.
                            return;
                        }
                        designer = snapshot.toObject(Designer.class);
                        designer.setId(snapshot.getId());
                        textViewName.setText(designer.getName());
                    }
                });

        designs = new ArrayList<>();
        designListAdapter = new DesignListAdapter(designs);
        recyclerViewDesigns.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDesigns.setAdapter(designListAdapter);

        // Tasarımcının paylaştığı tasarımları getir
        firestore.collection(DESIGNS_COLLECTION)
                .whereEqualTo("designerId", designerId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        designs.clear();
                        for (QueryDocumentSnapshot documentSnapshot : snapshot) {
                            Design design = documentSnapshot.toObject(Design.class);
                            design.setId(documentSnapshot.getId());
                            designs.add(design);
                        }
                        designListAdapter.notifyDataSetChanged();
                    }
                });

        buttonAddDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DesignerProfileActivity.this, DesignAddActivity.class);
                intent.putExtra("designerId", designer.getId());
                startActivity(intent);
            }
        });

        }
    }

