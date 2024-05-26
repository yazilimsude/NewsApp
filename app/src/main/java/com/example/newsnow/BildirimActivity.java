package com.example.newsnow;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class BildirimActivity extends AppCompatActivity {
    CheckBox general, business, sports, technology, health, entertainment, science;
    Button kaydet;
    Toolbar toolbar;
    FirebaseDatabase database;
    DatabaseReference reference, categoryref;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildirim);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        general = findViewById(R.id.general);
        business = findViewById(R.id.business);
        sports = findViewById(R.id.sports);
        technology = findViewById(R.id.technology);
        health = findViewById(R.id.health);
        entertainment = findViewById(R.id.entertainment);
        science = findViewById(R.id.science);

        kaydet = findViewById(R.id.kaydet);

        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance("https://buyukveri-7d8ff-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = database.getReference("Users").child(auth.getUid()).child("SelectedCategories");
        categoryref = database.getReference("Categories");

        loadSelectedCategories();
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedCategories();
            }
        });

    }
    private void saveSelectedCategories(){
        Map<String, Boolean> selectedCategories = new HashMap<>();
        selectedCategories.put("general", general.isChecked());
        selectedCategories.put("business", business.isChecked());
        selectedCategories.put("sports", sports.isChecked());
        selectedCategories.put("technology", technology.isChecked());
        selectedCategories.put("health", health.isChecked());
        selectedCategories.put("entertainment", entertainment.isChecked());
        selectedCategories.put("science", science.isChecked());

        reference.setValue(selectedCategories, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(BildirimActivity.this, "Kategoriler başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BildirimActivity.this, "Failed to save categories: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        for (Map.Entry<String, Boolean> entry : selectedCategories.entrySet()) {
            if (entry.getValue()) {
                categoryref.child(entry.getKey()).child(auth.getCurrentUser().getUid()).setValue("subscribed", new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null) {
                            Log.e("FirebaseError", "Failed to update category: " + entry.getKey() + " Error: " + error.getMessage());
                        }
                    }
                });
            } else {
                Log.d("girdik mi","girdik");
                categoryref.child(entry.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Log.d("snapshot",""+snapshot);
                            Log.d("snapshotvalue",""+snapshot.getValue());
                            Log.d("snapshotchildren",""+snapshot.hasChildren()+" "+snapshot.getChildrenCount());
                            if(snapshot.hasChild(auth.getCurrentUser().getUid())){
                                Log.d("deneme","griiş");
                                categoryref.child(entry.getKey()).child(auth.getCurrentUser().getUid()).removeValue();
                            }

                            //Log.d("snapshot",snapshot.child(entry.getKey()).child(auth.getCurrentUser().getUid()).toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }
    private void loadSelectedCategories() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Boolean> selectedCategories = (Map<String, Boolean>) snapshot.getValue();
                    if (selectedCategories != null) {
                        general.setChecked(selectedCategories.getOrDefault("general", false));
                        business.setChecked(selectedCategories.getOrDefault("business", false));
                        sports.setChecked(selectedCategories.getOrDefault("sports", false));
                        technology.setChecked(selectedCategories.getOrDefault("technology", false));
                        health.setChecked(selectedCategories.getOrDefault("health", false));
                        entertainment.setChecked(selectedCategories.getOrDefault("entertainment", false));
                        science.setChecked(selectedCategories.getOrDefault("science", false));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BildirimActivity.this, "Veri yükleme başarısız: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
