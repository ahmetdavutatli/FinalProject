package com.example.finalproject.Activity;

import android.os.Bundle;
import android.util.Log;
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
import com.example.finalproject.Domain.Category;
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
        DatabaseReference categoryRef = database.getReference("Category");
        binding.progressBar.setVisibility(View.VISIBLE);

        if (isSearch) {
            // Step 1: Search for a matching category
            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Integer matchedCategoryId = null;
                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                            Category category = categorySnapshot.getValue(Category.class);
                            if (category != null) {
                                Log.d("CategorySearch", "Category: " + category.getName() + ", Id: " + category.getId());
                                if (category.getName().toLowerCase().contains(searchText.toLowerCase())) {
                                    matchedCategoryId = category.getId();
                                    Log.d("CategorySearch", "Matched Category: " + category.getName() + " (Id: " + matchedCategoryId + ")");
                                    break;
                                }
                            }
                        }

                        if (matchedCategoryId != null) {
                            // Fetch foods by matched category
                            fetchFoodsByCategory(matchedCategoryId);
                        } else {
                            // Fallback: Search foods by title
                            fetchFoodsByTitle(searchText);
                        }
                    } else {
                        // Fallback: No categories found, search foods by title
                        fetchFoodsByTitle(searchText);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBar.setVisibility(View.GONE);
                    Log.e("CategorySearch", "Error fetching categories: " + error.getMessage());
                }
            });
        } else {
            // If not searching, fetch foods directly by categoryId
            fetchFoodsByCategory(categoryId);
        }
    }

    private void fetchFoodsByCategory(int categoryId) {
        Log.d("FetchFoodsByCategory", "Fetching foods for CategoryId: " + categoryId);
        DatabaseReference foodRef = database.getReference("Foods");
        Query query = foodRef.orderByChild("CategoryId").equalTo(categoryId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Foods> foodList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                        Foods food = foodSnapshot.getValue(Foods.class);
                        if (food != null) {
                            foodList.add(food);
                        }
                    }
                    Log.d("FetchFoodsByCategory", "Foods fetched: " + foodList.size());
                } else {
                    Log.d("FetchFoodsByCategory", "No foods found for CategoryId: " + categoryId);
                }

                // Update the RecyclerView
                updateRecyclerView(foodList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e("FetchFoodsByCategory", "Error: " + error.getMessage());
            }
        });
    }

    private void fetchFoodsByTitle(String searchText) {
        Log.d("FetchFoodsByTitle", "Searching foods by title containing: " + searchText);
        DatabaseReference foodRef = database.getReference("Foods");
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Foods> foodList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                        Foods food = foodSnapshot.getValue(Foods.class);
                        if (food != null && food.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                            foodList.add(food);
                        }
                    }
                    Log.d("FetchFoodsByTitle", "Foods fetched by title: " + foodList.size());
                } else {
                    Log.d("FetchFoodsByTitle", "No foods found for title: " + searchText);
                }

                // Update the RecyclerView
                updateRecyclerView(foodList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e("FetchFoodsByTitle", "Error fetching foods: " + error.getMessage());
            }
        });
    }

    private void updateRecyclerView(ArrayList<Foods> foodList) {
        if (!foodList.isEmpty()) {
            binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodActivity.this, 2));
            adapterListFood = new ListFoodAdapter(foodList);
            binding.foodListView.setAdapter(adapterListFood);
        } else {
            // Optionally show a "No results found" message
            Log.d("RecyclerView", "No results to display.");
        }
        binding.progressBar.setVisibility(View.GONE);
    }




}