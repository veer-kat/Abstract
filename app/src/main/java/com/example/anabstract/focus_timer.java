package com.example.anabstract;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class focus_timer extends AppCompatActivity {

    private TextView timerText;
    private EditText timeInput;
    private Button setTimeButton, startButton, pauseButton, resetButton;
    private CountDownTimer countDownTimer;
    private Handler attentionHandler;
    private boolean isRunning = false;
    private long timeLeftInMillis = 1500000; // Default to 25 minutes
    private static final int ATTENTION_INTERVAL_MIN = 20000; // 20 seconds
    private static final int ATTENTION_INTERVAL_MAX = 60000; // 60 seconds
    private static final int ATTENTION_TIMEOUT = 5000; // 5 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.focus_timer);
        timerText = findViewById(R.id.timerText);
        timeInput = findViewById(R.id.timeInput);
        setTimeButton = findViewById(R.id.setTimeButton);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);
        attentionHandler = new Handler();

        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                scheduleAttentionPopup();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        updateTimerText();
    }

    private void setTime() {
        String input = timeInput.getText().toString();
        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show();
            return;
        }

        int minutes = Integer.parseInt(input);
        if (minutes <= 0) {
            Toast.makeText(this, "Please enter a positive time", Toast.LENGTH_SHORT).show();
            return;
        }

        timeLeftInMillis = minutes * 60000; // Convert minutes to milliseconds
        updateTimerText();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                isRunning = false;
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                resetButton.setVisibility(View.VISIBLE);
            }
        }.start();

        isRunning = true;
        startButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isRunning = false;
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
    }

    private void resetTimer() {
        timeLeftInMillis = 1500000; // Reset to 25 minutes
        updateTimerText();
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);
        attentionHandler.removeCallbacksAndMessages(null);
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerText.setText(timeFormatted);
    }

    private void scheduleAttentionPopup() {
        Random random = new Random();
        int randomInterval = ATTENTION_INTERVAL_MIN + random.nextInt(ATTENTION_INTERVAL_MAX - ATTENTION_INTERVAL_MIN);

        attentionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showAttentionPopup();
                scheduleAttentionPopup(); // Schedule the next popup
            }
        }, randomInterval);
    }

    private void showAttentionPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.attention_pop, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button confirmButton = dialogView.findViewById(R.id.attentionConfirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // Dismiss the dialog if no response within ATTENTION_TIMEOUT
        attentionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    pauseTimer(); // Pause timer if user did not respond
                    Toast.makeText(focus_timer.this, "Timer paused due to inactivity.", Toast.LENGTH_SHORT).show();
                }
            }
        }, ATTENTION_TIMEOUT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        attentionHandler.removeCallbacksAndMessages(null); // Stop any pending popups
    }

}
