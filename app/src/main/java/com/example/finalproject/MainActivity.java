package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String DESIGNS_COLLECTION = "designs";
    private static final String DESIGNERS_COLLECTION = "designers";

    private RecyclerView recyclerViewDesigns;
    private DesignListAdapter designListAdapter;
    private List<Design> designs;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewDesigns = findViewById(R.id.recycler_view_designs);

        designs = new ArrayList<>();
        designListAdapter = new DesignListAdapter(designs);
        recyclerViewDesigns.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDesigns.setAdapter(designListAdapter);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection(DESIGNS_COLLECTION)
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
                            // Tasarımcının bilgilerini getir
                            String designerId = design.getId();
                            firestore.collection(DESIGNERS_COLLECTION).document(designerId)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot snapshot) {
                                            Designer designer = snapshot.toObject(Designer.class);
                                            designer.setId(snapshot.getId());
                                            design.setDesigner(designer);
                                            designs.add(design);
                                            designListAdapter.notifyDataSetChanged();
                                        }
                                    });
                        }
                    }
                });

        designListAdapter.setOnItemClickListener(new DesignListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Design design) {
                Intent intent = new Intent(MainActivity.this, MakeOfferActivity.class);
                intent.putExtra("design", (Parcelable) design);
                startActivity(intent);
            }
        });
    }
}
