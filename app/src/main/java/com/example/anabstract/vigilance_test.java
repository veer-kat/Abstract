package com.example.anabstract;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anabstract.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Random;

public class vigilance_test extends AppCompatActivity {
    private TextView stimulusTextView;
    private long responseTime; // Dec
    private Button correctButton;
    private Button incorrectButton;
    private TextView scoreTextView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig; // Add Firebase Remote Config
    private int score = 0;
    private int totalStimuli = 20; // Total stimuli to present
    private int currentStimulus = 0;
    private Handler handler = new Handler();
    private Random random = new Random();
    private boolean isCurrentTarget;
    private long displayTime; // Declare displayTime

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vigilance_test);

        stimulusTextView = findViewById(R.id.stimulusTextView);
        correctButton = findViewById(R.id.correctButton);
        incorrectButton = findViewById(R.id.incorrectButton);
        scoreTextView = findViewById(R.id.scoreTextView);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Initialize Firebase Remote Config
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600) // Set cache expiration
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        // Fetch remote config values
        fetchRemoteConfig();

        correctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleResponse(true);
            }
        });

        incorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleResponse(false);
            }
        });
    }

    private void fetchRemoteConfig() {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Get the display time from Remote Config
                        displayTime = mFirebaseRemoteConfig.getLong("stimulus_display_time");
                    } else {
                        // Fallback to a default display time if fetching fails
                        displayTime = 1000; // Default to 5 second
                    }
                    startCPT(); // Start the CPT after fetching config
                });
    }

    private void startCPT() {
        correctButton.setEnabled(true);
        incorrectButton.setEnabled(true);
        currentStimulus = 0;
        score = 0;
        scoreTextView.setText("Score: " + score);
        presentNextStimulus();
    }

    private void presentNextStimulus() {
        if (currentStimulus < totalStimuli) {
            // Randomly choose to display a target or non-target
            isCurrentTarget = random.nextBoolean(); // Update the variable here
            stimulusTextView.setText(isCurrentTarget ? "X" : "O"); // "X" is target, "O" is non-target

            // Log stimulus presentation
            Bundle bundle = new Bundle();
            bundle.putString("stimulus_type", isCurrentTarget ? "target" : "non_target");
            bundle.putInt("stimulus_number", currentStimulus + 1);
            mFirebaseAnalytics.logEvent("stimulus_presented", bundle);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stimulusTextView.setText(""); // Clear the stimulus after some time
                    currentStimulus++;
                    presentNextStimulus();
                }
            }, displayTime + 300); // Show each stimulus for the configured display time
        } else {
            endCPT();
        }
    }


    private void handleResponse(boolean isCorrect) {
        if (isCorrect) {
            // Check if the response was correct
            if (isCurrentTarget) {
                score++; // Increment score for correct target response
            } else {
                score--; // Decrement score for incorrect target response
            }
        } else {
            // Check if the response was incorrect
            if (!isCurrentTarget) {
                score++; // Increment score for correct non-target response
            } else {
                score--; // Decrement score for incorrect non-target response
            }
        }
        Bundle responseBundle = new Bundle();
        responseBundle.putString("response_type", isCorrect ? "correct" : "incorrect");
        responseBundle.putLong("response_time", responseTime);
        mFirebaseAnalytics.logEvent("response_recorded", responseBundle);

        scoreTextView.setText("Score: " + score);
    }

    private void endCPT() {
        correctButton.setEnabled(false);
        incorrectButton.setEnabled(false);
        stimulusTextView.setText("Done! Final Score: " + score);
    }
}
