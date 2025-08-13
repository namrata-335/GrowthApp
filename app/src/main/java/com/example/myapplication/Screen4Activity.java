package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Screen4Activity extends AppCompatActivity {

    private static final String PREFS = "app_prefs";
    private static final String KEY_GOALS = "selectedGoals";
    private static final String KEY_REMINDERS = "reminders";

    private LinearLayout habitContainer;
    private ArrayList<String> selectedGoals;
    private ArrayList<String> reminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen4);

        habitContainer = findViewById(R.id.habitContainer);

        // Get from intent if present (when coming back from a Week activity)
        Intent intent = getIntent();
        ArrayList<String> intentGoals = intent.getStringArrayListExtra("selectedGoals");
        ArrayList<String> intentReminders = intent.getStringArrayListExtra("reminders");

        // Load from prefs as fallback
        selectedGoals = (intentGoals != null) ? intentGoals : loadList(KEY_GOALS);
        reminders = (intentReminders != null) ? intentReminders : loadList(KEY_REMINDERS);

        // Save incoming values so they persist for future launches
        if (intentGoals != null) saveList(KEY_GOALS, selectedGoals);
        if (intentReminders != null) saveList(KEY_REMINDERS, reminders);

        // Populate dashboard (clear first to avoid duplicates)
        habitContainer.removeAllViews();
        if (selectedGoals != null) {
            for (String goal : selectedGoals) {
                TextView goalView = new TextView(this);
                goalView.setText("❤️ " + goal);
                goalView.setTextColor(Color.parseColor("#5D4037"));
                goalView.setTextSize(18);
                goalView.setPadding(0, 16, 0, 16);
                habitContainer.addView(goalView);
            }
        }

        if (reminders != null) {
            for (String reminder : reminders) {
                if (reminder != null && !reminder.trim().isEmpty()) {
                    TextView reminderView = new TextView(this);
                    reminderView.setText("⏰ " + reminder);
                    reminderView.setTextColor(Color.parseColor("#6D4C41"));
                    reminderView.setTextSize(16);
                    reminderView.setPadding(0, 12, 0, 12);
                    habitContainer.addView(reminderView);
                }
            }
        }

        // Buttons (example navigations)
        Button heartWalkButton = findViewById(R.id.heartWalkButton);
        Button reflectionButton = findViewById(R.id.reflectionButton);
        Button continueButton = findViewById(R.id.continueButton);

        heartWalkButton.setOnClickListener(v -> {
            startActivity(new Intent(Screen4Activity.this, HeartWalkActivity.class));
        });

        reflectionButton.setOnClickListener(v -> {
            startActivity(new Intent(Screen4Activity.this, DailyReflectionActivity.class));
        });

        continueButton.setOnClickListener(v -> {
            // Pass current lists forward so GoalTime has them
            Intent goalTimeIntent = new Intent(Screen4Activity.this, GoalTime.class);
            goalTimeIntent.putStringArrayListExtra("selectedGoals", selectedGoals);
            goalTimeIntent.putStringArrayListExtra("reminders", reminders);
            startActivity(goalTimeIntent);
        });
    }

    // Save  ArrayList<String> as a JSON string
    private void saveList(String key, ArrayList<String> list) {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        JSONArray array = new JSONArray();
        if (list != null) {
            for (String s : list) {
                array.put(s);
            }
        }
        ed.putString(key, array.toString());
        ed.apply();
    }

    // Load an ArrayList<String> from JSON stored in prefs
    private ArrayList<String> loadList(String key) {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String json = prefs.getString(key, null);
        ArrayList<String> list = new ArrayList<>();
        if (json == null) return list;
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                list.add(array.optString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}