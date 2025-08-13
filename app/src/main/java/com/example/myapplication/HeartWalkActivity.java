package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HeartWalkActivity extends AppCompatActivity {

    private int heartsFound = 0;
    private TextView heartCountTextView;
    private Button foundHeartButton;
    private Button resetButton;

    private Button backToDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartwalk);

        heartCountTextView = findViewById(R.id.heartCounterText);
        foundHeartButton = findViewById(R.id.incrementButton);
        resetButton = findViewById(R.id.resetButton);
        backToDashboard = findViewById(R.id.backToDashboard);
        foundHeartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartsFound++;
                heartCountTextView.setText("Hearts Found: " + heartsFound);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartsFound = 0;
                heartCountTextView.setText("Hearts Found: " + heartsFound);
            }
        });
        backToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}