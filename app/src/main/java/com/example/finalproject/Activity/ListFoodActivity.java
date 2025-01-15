package com.example.finalproject.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Adapter.ListFoodAdapter;
import com.example.finalproject.Domain.Foods;
import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityListFoodBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodActivity extends BaseActivity {
    private ActivityListFoodBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityListFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getIntentExtra();
        initList();
    }

    private void getIntentExtra() {
        categoryId=getIntent().getIntExtra("CategoryId",0);
        categoryName=getIntent().getStringExtra("CategoryName");
        searchText=getIntent().getStringExtra("text");
        isSearch=getIntent().getBooleanExtra("isSearch",false);

        binding.titleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }


    private void initList() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBar.setVisibility(View.VISIBLE);
        final ArrayList<Foods>[] list = new ArrayList[]{new ArrayList<>()};
        Query query;

        if (isSearch) {
            query = myRef; // Fetch all data for filtering on the client side
        } else {
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Foods food = issue.getValue(Foods.class);
                        if (food != null) {
                            list[0].add(food);
                        }
                    }

                    // Filter the list based on the search text
                    if (isSearch) {
                        list[0] = filterListBySearchText(list[0], searchText);
                    }

                    if (!list[0].isEmpty()) {
                        binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodActivity.this, 2));
                        adapterListFood = new ListFoodAdapter(list[0]);
                        binding.foodListView.setAdapter(adapterListFood);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Filters the list of foods based on the search text.
     */
    private ArrayList<Foods> filterListBySearchText(ArrayList<Foods> list, String searchText) {
        ArrayList<Foods> filteredList = new ArrayList<>();
        for (Foods food : list) {
            if (food.getTitle() != null && food.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(food);
            }
        }
        return filteredList;
    }

}