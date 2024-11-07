package com.example.anabstract;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class noisy_number extends AppCompatActivity {

    TextView tvNum1, tvNum2, tvAns, tvResult, tvOperation;
    String operation;  // To store the selected operation
    MediaPlayer mediaPlayer;  // MediaPlayer to handle background audio
    AudioManager audioManager; // AudioManager to control the volume
    int maxVolume;  // To store the maximum volume of the device
    int minAllowedVolume;  // Minimum allowed volume (40% of max volume)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noisy_number);

        tvNum1 = findViewById(R.id.tv_num_1);
        tvNum2 = findViewById(R.id.tv_num_2);
        tvAns = findViewById(R.id.tv_ans);
        tvResult = findViewById(R.id.tv_result);
        tvOperation = findViewById(R.id.tv_operation);  // TextView to display the operation

        // Initialize AudioManager to control volume
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        minAllowedVolume = (int) (0.5 * maxVolume);  // 40% of max volume

        // Set initial volume to 40% if it's lower
        if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < minAllowedVolume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, minAllowedVolume, 0);
        }

        // Initialize the MediaPlayer and set it to loop the audio continuously
        mediaPlayer = MediaPlayer.create(noisy_number.this, R.raw.noisy_number);
        mediaPlayer.setLooping(true);  // Loop the audio
        mediaPlayer.setVolume(1.0f, 1.0f);  // Set volume to full for both left and right channels
        mediaPlayer.start();  // Start playing the audio
        run_reset();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume playing the audio when the app comes back to the foreground
        if (mediaPlayer != null) {
            mediaPlayer.start();  // Start or resume the audio
        }
        enforceMinVolume();  // Ensure volume is not below 40%
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause the MediaPlayer when the app is paused or goes to the background
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();  // Pause the audio
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer resource when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            enforceMinVolume();  // Enforce minimum volume when the window regains focus
        }
    }

    // Method to ensure volume is not below 40% of the max volume
    private void enforceMinVolume() {
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume < minAllowedVolume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, minAllowedVolume, 0);  // Reset to 40%
        }
    }

    // Override to handle volume button presses
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            // If the current volume is at or below the minimum allowed volume, block the action
            if (currentVolume <= minAllowedVolume) {
                return true; // Do nothing, block the volume down action
            } else {
                // Decrease the volume but ensure it doesn't go below the minimum
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 1, 0);
                enforceMinVolume();  // Re-check volume to enforce minimum
                return true; // Indicate that the volume change has been handled
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            // Increase the volume if it is below the maximum
            if (currentVolume < maxVolume) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + 1, 0);
            }
            return true; // Indicate that the volume change has been handled
        }
        return super.onKeyDown(keyCode, event);
    }

    void run_reset() {
        Random myRandom = new Random();

        int num1 = myRandom.nextInt(101);  // Generates numbers between 0 and 100
        int num2 = myRandom.nextInt(101);  // Generates numbers between 0 and 100

        tvNum1.setText("" + num1);
        tvNum2.setText("" + num2);

        tvAns.setText("");
        tvResult.setText("");

        // Randomly select an operation (+, -, *, /)
        int op = myRandom.nextInt(4);
        switch (op) {
            case 0:
                operation = "ADD";
                tvOperation.setText("+");
                break;
            case 1:
                operation = "SUBTRACT";
                tvOperation.setText("-");
                break;
            case 2:
                operation = "MULTIPLY";
                tvOperation.setText("×");
                break;
            case 3:
                operation = "DIVIDE";
                tvOperation.setText("÷");
                break;
        }
    }

    void printAns(String a) {
        String text = tvAns.getText().toString();
        tvAns.setText(text + a);
    }

    public void one(View view) { printAns("1"); }
    public void two(View view) { printAns("2"); }
    public void three(View view) { printAns("3"); }
    public void four(View view) { printAns("4"); }
    public void five(View view) { printAns("5"); }
    public void six(View view) { printAns("6"); }
    public void seven(View view) { printAns("7"); }
    public void eight(View view) { printAns("8"); }
    public void nine(View view) { printAns("9"); }
    public void zero(View view) { printAns("0"); }

    public void clear(View view) { run_reset(); }

    public void submit(View view) {
        int num1 = Integer.parseInt(tvNum1.getText().toString());
        int num2 = Integer.parseInt(tvNum2.getText().toString());
        int ans = 0;

        try {
            int get_user_ans = Integer.parseInt(tvAns.getText().toString());

            // Perform the operation based on the randomly selected operation
            switch (operation) {
                case "ADD":
                    ans = num1 + num2;
                    break;
                case "SUBTRACT":
                    ans = num1 - num2;
                    break;
                case "MULTIPLY":
                    ans = num1 * num2;
                    break;
                case "DIVIDE":
                    if (num2 != 0) {
                        ans = num1 / num2;  // Integer division
                    } else {
                        tvResult.setText("Cannot divide by zero!");
                        return;  // Return early if division by zero occurs
                    }
                    break;
            }

            // Check if the user's answer is correct
            if (ans == get_user_ans) {
                tvResult.setText("CORRECT!!");
            } else {
                tvResult.setText("The correct Answer is\n " + ans);
            }
        } catch (NumberFormatException e) {
            tvResult.setText("Please enter a valid number!");
        }
    }
}
