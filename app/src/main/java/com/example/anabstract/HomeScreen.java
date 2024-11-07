package com.example.anabstract;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;


public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

    }
    public void openGameScreen(View view) {
        Intent intent = new Intent(HomeScreen.this, game_screen.class);
        startActivity(intent);
    }

    public void openFocusTimerScreen(View view) {
        Intent intent = new Intent(HomeScreen.this, focus_timer.class);
        startActivity(intent);
    }
    public void openBlockControlScreen(View view) {
        Intent intent = new Intent(HomeScreen.this, BlockControl.class);
        startActivity(intent);
    }

    public void openVigilanceTestScreen(View view) {
        Intent intent = new Intent(HomeScreen.this, vigilance_test.class);
        startActivity(intent);
    }
}