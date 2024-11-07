package com.example.anabstract;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
public class game_screen extends AppCompatActivity {

    LinearLayout btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);

        findViewById(R.id.sonic_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(game_screen.this, SonicSyncdown.class);
                startActivity(intent);
            }
        });

        // Chatter Recall Button
        findViewById(R.id.chatter_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(game_screen.this, ChatterRecall_Pt1.class);
                startActivity(intent);
            }
        });

        // Memory Mayhem Button
        findViewById(R.id.noisy_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(game_screen.this, noisy_number.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.memory_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(game_screen.this, memory_game.class);
                startActivity(intent);
            }
        });
    }
}
