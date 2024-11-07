package com.example.anabstract;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SonicSyncdown extends AppCompatActivity {

    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundMap;
    private float volume = 1.0f; // Initial volume

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private final int nextClueWaitTime = 1000; // How long to wait before starting clue playback
    private int guessCounter = 0;
    private double clueHoldTime = 1000.0; // How long to hold each clue
    private final double cluePauseTime = 333.0; // Pause time between clues
    private TextView scoreText; // Text to display current score
    private Button startButton; // Start button
    private Button stopButton; // Stop button
    private int[] pattern;
    private int patternLength = 8;
    private int progress = 0;
    private boolean gamePlaying = false;
    private int curScore = 0;
    private int highScore = 0;
    private Handler clueHandler;
    private ToneGenerator toneGenerator;
    private TextView p1;

    private ArrayList<String> dummyWords;
    private Handler popupHandler;
    private Runnable popupRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sonic_syncdown);

        // UI References
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        p1 = findViewById(R.id.p1);
        SeekBar volumeSlider = findViewById(R.id.volumeSlider);
        startButton = findViewById(R.id.startBtn);
        stopButton = findViewById(R.id.stopBtn);
        scoreText = findViewById(R.id.scoreText);

        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, (int) (ToneGenerator.MAX_VOLUME * volume));

        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress / 100.0f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        startButton.setOnClickListener(v -> startGame());
        stopButton.setOnClickListener(v -> stopGame());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6) // Number of simultaneous sounds
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        // Initialize soundMap to store sound resources
        soundMap = new HashMap<>();

        // Load sounds into soundMap (add your own sound files)
        soundMap.put(1, soundPool.load(this, R.raw.sound1, 1));
        soundMap.put(2, soundPool.load(this, R.raw.sound2, 1));
        soundMap.put(3, soundPool.load(this, R.raw.sound3, 1));
        soundMap.put(4, soundPool.load(this, R.raw.sound4, 1));

        // Add listeners to buttons
        button1.setOnClickListener(v -> guessButton1());
        button2.setOnClickListener(v -> guessButton2());
        button3.setOnClickListener(v -> guessButton3());
        button4.setOnClickListener(v -> guessButton4());

        // Initialize dummy words list
        dummyWords = new ArrayList<>();
        dummyWords.add("Going wrong");
        dummyWords.add("Congratulations!!!");
        dummyWords.add("Please like and share");
        dummyWords.add("Keep going my friend!");
        dummyWords.add("Thank you for playing");

        // Initialize handler for popup messages
        popupHandler = new Handler();
    }

    private void startGame() {
        gamePlaying = true;
        curScore = 0;
        progress = 0;
        generatePattern();
        playClueSequence();
        updateParagraph();
        startPopupMessages(); // Start displaying pop-up messages
    }

    private void generatePattern() {
        pattern = new int[patternLength];
        Random random = new Random();
        for (int i = 0; i < patternLength; i++) {
            pattern[i] = random.nextInt(4) + 1; // Random pattern between 1-4
        }
    }

    private void updateParagraph() {
        p1.setText("Press buttons in the same order as the original pattern to win.");
    }

    public void guessButton1() {
        processGuess(1);
    }

    public void guessButton2() {
        processGuess(2);
    }

    public void guessButton3() {
        processGuess(3);
    }

    public void guessButton4() {
        processGuess(4);
    }

    // Handles user guesses
    private void processGuess(int btn) {
        if (!gamePlaying) {
            return;
        }

        // Check if the guessed button matches the pattern
        if (btn == pattern[guessCounter]) {
            // Correct guess
            guessCounter++;
            if (guessCounter == progress + 1) {
                // User has correctly guessed the current sequence
                guessCounter = 0;
                progress++;
                curScore = progress;
                updateScoreText();

                if (progress == patternLength) {
                    // User guessed the full pattern correctly
                    winGame();
                    return;
                }

                // Play the next clue sequence
                playClueSequence();
            }
        } else {
            // Incorrect guess, stop the game immediately
            loseGame("You guessed wrong! Game Over.");
        }
    }

    private void loseGame(String message) {
        gamePlaying = false;
        stopGame();
        showAlert("Game Over", message);
    }

    private void winGame() {
        gamePlaying = false;
        stopGame();
        showAlert("Game Over", "You won!");
    }

    private void stopGame() {
        popupHandler.removeCallbacks(popupRunnable); // Stop pop-up messages
        Intent close= new Intent(SonicSyncdown.this,Login.class);
        startActivity(close);
        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.GONE);
    }

    private void startPopupMessages() {
        popupRunnable = new Runnable() {
            @Override
            public void run() {
                displayPopupMessage();
                popupHandler.postDelayed(this, 10000); // Schedule next pop-up in 15 seconds
            }
        };
        popupHandler.postDelayed(popupRunnable, 10000); // Initial delay
    }

    private void displayPopupMessage() {
        // Pick a random dummy word for the popup
        String message = dummyWords.get(new Random().nextInt(dummyWords.size()));

        // Show popup dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reminder");
        builder.setMessage(message);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Clue sequence playing logic
    private void playClueSequence() {
        guessCounter = 0;
        clueHoldTime *= 0.9;
        new Handler().postDelayed(() -> {
            for (int i = 0; i <= progress; i++) {
                final int btn = pattern[i];
                new Handler().postDelayed(() -> playSingleClue(btn), i * (int) (clueHoldTime + cluePauseTime));
            }
        }, nextClueWaitTime);
    }

    private void playSingleClue(int btn) {
        lightButton(btn);
        playTone(btn, clueHoldTime);
        new Handler().postDelayed(() -> clearButton(btn), (long) clueHoldTime);
    }

    private void lightButton(int btn) {
        switch (btn) {
            case 1:
                button1.setBackgroundColor(Color.parseColor("#00ffff"));
                break;
            case 2:
                button2.setBackgroundColor(Color.parseColor("#66ff99"));
                break;
            case 3:
                button3.setBackgroundColor(Color.parseColor("#ffff66"));
                break;
            case 4:
                button4.setBackgroundColor(Color.parseColor("#ff5050"));
                break;
        }
    }

    private void clearButton(int btn) {
        switch (btn) {
            case 1:
                button1.setBackgroundColor(Color.parseColor("#00B4FF"));
                break;
            case 2:
                button2.setBackgroundColor(Color.parseColor("#00AA09"));
                break;
            case 3:
                button3.setBackgroundColor(Color.parseColor("#FFE500"));
                break;
            case 4:
                button4.setBackgroundColor(Color.parseColor("#FF0000"));
                break;
        }
    }

    private void playTone(int btn, double duration) {
        int soundId = soundMap.get(btn);
        if (soundId != 0) {
            soundPool.play(soundId, volume, volume, 1, 0, 1.0f);
        }
    }

    private void updateScoreText() {
        scoreText.setText("Score: " + curScore);
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            // Reset game state after alert is dismissed
            resetGame();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void resetGame() {
        // Reset game variables to initial state
        curScore = 0;
        progress = 0;
        guessCounter = 0;
        updateScoreText();
        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.GONE);
    }
}
