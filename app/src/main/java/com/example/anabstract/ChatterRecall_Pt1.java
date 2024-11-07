package com.example.anabstract;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ChatterRecall_Pt1 extends AppCompatActivity {

    private ImageButton playPauseButton;
    private Button next;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private MediaPlayer distractionPlayer;
    private Handler handler = new Handler();
    private Handler distractionHandler = new Handler();
    private boolean isPlaying = false;

    private int[] distractionNoises = {R.raw.distraction1, R.raw.distraction2, R.raw.distraction3, R.raw.distraction4};

    // TextView references
    private TextView currentTimeTextView;
    private TextView totalTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatter_recall);

        playPauseButton = findViewById(R.id.playPauseButton);
        seekBar = findViewById(R.id.seekBar);
        next= findViewById(R.id.next);

        // Initialize the TextViews
        currentTimeTextView = findViewById(R.id.currentTimeTextView);
        totalTimeTextView = findViewById(R.id.totalTimeTextView);

        // Initialize the media player with an audio file (replace with your audio file path)
        mediaPlayer = MediaPlayer.create(this, R.raw.theplanthousehold);

        // Set the total time in the totalTimeTextView
        totalTimeTextView.setText(formatTime(mediaPlayer.getDuration()));

        seekBar.setMax(mediaPlayer.getDuration());

        // Play/Pause button click listener
        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                pauseAudio();
            } else {
                playAudio();
            }
        });

        // Update SeekBar position
        mediaPlayer.setOnCompletionListener(mp -> resetPlayer());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatterRecall_Pt1.this, ChatterRecall_Pt2.class);
                startActivity(intent);
            }
        });
    }

    private void playAudio() {
        mediaPlayer.start();
        playPauseButton.setImageResource(R.drawable.ic_pause);
        isPlaying = true;
        updateSeekBar();

        // Start random distraction noises
        playDistractionNoisesRandomly();
    }

    private void pauseAudio() {
        mediaPlayer.pause();
        playPauseButton.setImageResource(R.drawable.ic_play);
        isPlaying = false;

        // Stop distraction noises
        stopDistractionNoises();
    }

    private void resetPlayer() {
        isPlaying = false;
        playPauseButton.setImageResource(R.drawable.ic_play);
        seekBar.setProgress(0);
        mediaPlayer.seekTo(0);

        // Stop distraction noises when audio finishes
        stopDistractionNoises();
    }

    private void updateSeekBar() {
        if (isPlaying) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(currentPosition);
            currentTimeTextView.setText(formatTime(currentPosition)); // Update current time TextView
            handler.postDelayed(this::updateSeekBar, 1000);
        }
    }

    // Method to play distraction noises at random intervals
    private void playDistractionNoisesRandomly() {
        distractionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isPlaying) {
                    Random random = new Random();
                    int randomNoise = distractionNoises[random.nextInt(distractionNoises.length)];
                    distractionPlayer = MediaPlayer.create(ChatterRecall_Pt1.this, randomNoise);
                    distractionPlayer.start();

                    distractionPlayer.setOnCompletionListener(mp -> {
                        distractionPlayer.release();
                        distractionPlayer = null;
                    });

                    // Schedule the next distraction noise after 20 seconds (or any other interval)
                    distractionHandler.postDelayed(this, 5000);
                }
            }
        }, 5000); // First distraction after 5 seconds
    }

    private void stopDistractionNoises() {
        distractionHandler.removeCallbacksAndMessages(null);
        if (distractionPlayer != null) {
            distractionPlayer.stop();
            distractionPlayer.release();
            distractionPlayer = null;
        }
    }

    // Format milliseconds to mm:ss
    private String formatTime(int millis) {
        int minutes = (millis / 1000) / 60;
        int seconds = (millis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (distractionPlayer != null) {
            distractionPlayer.release();
            distractionPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
        distractionHandler.removeCallbacksAndMessages(null);
    }
}
