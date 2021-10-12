package com.huawei.healthkitsampleapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.healthkitsampleapp.kotlin.HomePageActivity;

public class EntryChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_choice);

        findViewById(R.id.btn_java).setOnClickListener(view -> {
            Intent intent = new Intent(EntryChoiceActivity.this, com.huawei.healthkitsampleapp.java.HomePageActivity.class);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.btn_kotlin).setOnClickListener(view -> {
            Intent intent = new Intent(EntryChoiceActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        });
    }
}