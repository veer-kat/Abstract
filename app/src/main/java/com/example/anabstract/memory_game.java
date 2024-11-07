package com.example.anabstract;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
public class memory_game extends AppCompatActivity {

    // Variables
    ImageView flashCard;

    // Handler and Runnable for showing flashcard at random intervals
    Handler flashCardHandler = new Handler();
    Runnable flashCardRunnable;

    TextView tv_p1;
    ImageView iv_11, iv_12, iv_13, iv_14, iv_21, iv_22, iv_23, iv_24, iv_31, iv_32, iv_33, iv_34;
    Integer[] cardArray = {101, 102, 103, 104, 105, 106, 201, 202, 203, 204, 205, 206};

    int image101, image102, image103, image104, image105, image106, image201, image202, image203, image204, image205, image206;

    int firstCard, secondCard;
    int clickedFirst, clickedSecond;
    int cardNumber = 1;
    int playerPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_mayhem);

        flashCard = findViewById(R.id.flash_card);

        // Start the flashcard distraction at random intervals
        startFlashCardDistraction();
        // Initialize TextViews and ImageViews
        tv_p1 = findViewById(R.id.tv_p1);

        iv_11 = findViewById(R.id.iv_11);
        iv_12 = findViewById(R.id.iv_12);
        iv_13 = findViewById(R.id.iv_13);
        iv_14 = findViewById(R.id.iv_14);
        iv_21 = findViewById(R.id.iv_21);
        iv_22 = findViewById(R.id.iv_22);
        iv_23 = findViewById(R.id.iv_23);
        iv_24 = findViewById(R.id.iv_24);
        iv_31 = findViewById(R.id.iv_31);
        iv_32 = findViewById(R.id.iv_32);
        iv_33 = findViewById(R.id.iv_33);
        iv_34 = findViewById(R.id.iv_34);

        // Assign tags to ImageViews based on their indices
        iv_11.setTag(0);
        iv_12.setTag(1);
        iv_13.setTag(2);
        iv_14.setTag(3);
        iv_21.setTag(4);
        iv_22.setTag(5);
        iv_23.setTag(6);
        iv_24.setTag(7);
        iv_31.setTag(8);
        iv_32.setTag(9);
        iv_33.setTag(10);
        iv_34.setTag(11);

        // Load the front of cards
        frontOfCardResources();

        // Shuffle the card array
        Collections.shuffle(Arrays.asList(cardArray));

        // Set up listeners for each ImageView
        iv_11.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_11, theCard);
        });
        iv_12.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_12, theCard);
        });
        iv_13.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_13, theCard);
        });
        iv_14.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_14, theCard);
        });
        iv_21.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_21, theCard);
        });
        iv_22.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_22, theCard);
        });
        iv_23.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_23, theCard);
        });
        iv_24.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_24, theCard);
        });
        iv_31.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_31, theCard);
        });
        iv_32.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_32, theCard);
        });
        iv_33.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_33, theCard);
        });
        iv_34.setOnClickListener(v -> {
            int theCard = (int) v.getTag();
            doStuff(iv_34, theCard);
        });
    }

    // Load card images
    private void frontOfCardResources() {
        image101 = R.drawable.emoji1;
        image102 = R.drawable.emoji2;
        image103 = R.drawable.emoji3;
        image104 = R.drawable.emoji4;
        image105 = R.drawable.emoji5;
        image106 = R.drawable.emoji6;
        image201 = R.drawable.emoji1copy;
        image202 = R.drawable.emoji2copy;
        image203 = R.drawable.emoji3copy;
        image204 = R.drawable.emoji4copy;
        image205 = R.drawable.emoji5copy;
        image206 = R.drawable.emoji6copy;
    }

    private void doStuff(ImageView iv, int card) {
        // Set the correct image for the selected card
        if (cardArray[card] == 101) {
            iv.setImageResource(image101);
        } else if (cardArray[card] == 102) {
            iv.setImageResource(image102);
        } else if (cardArray[card] == 103) {
            iv.setImageResource(image103);
        } else if (cardArray[card] == 104) {
            iv.setImageResource(image104);
        } else if (cardArray[card] == 105) {
            iv.setImageResource(image105);
        } else if (cardArray[card] == 106) {
            iv.setImageResource(image106);
        } else if (cardArray[card] == 201) {
            iv.setImageResource(image201);
        } else if (cardArray[card] == 202) {
            iv.setImageResource(image202);
        } else if (cardArray[card] == 203) {
            iv.setImageResource(image203);
        } else if (cardArray[card] == 204) {
            iv.setImageResource(image204);
        } else if (cardArray[card] == 205) {
            iv.setImageResource(image205);
        } else if (cardArray[card] == 206) {
            iv.setImageResource(image206);
        }

        // Process first and second card selection
        if (cardNumber == 1) {
            firstCard = cardArray[card];
            if (firstCard > 200) {
                firstCard = firstCard - 100; // Normalize card value for matching
            }
            cardNumber = 2;
            clickedFirst = card;
            iv.setEnabled(false); // Disable the first card clicked
        } else if (cardNumber == 2) {
            secondCard = cardArray[card];
            if (secondCard > 200) {
                secondCard = secondCard - 100; // Normalize card value for matching
            }
            clickedSecond = card;

            // Disable all cards temporarily
            iv_11.setEnabled(false);
            iv_12.setEnabled(false);
            iv_13.setEnabled(false);
            iv_14.setEnabled(false);
            iv_21.setEnabled(false);
            iv_22.setEnabled(false);
            iv_23.setEnabled(false);
            iv_24.setEnabled(false);
            iv_31.setEnabled(false);
            iv_32.setEnabled(false);
            iv_33.setEnabled(false);
            iv_34.setEnabled(false);

            // Delay before checking the cards
            Handler handler = new Handler();
            handler.postDelayed(this::calculate, 1000); // Delay for 1 second
        }
    }

    private void checkEnd() {
        if (iv_11.getVisibility() == View.INVISIBLE &&
                iv_12.getVisibility() == View.INVISIBLE &&
                iv_13.getVisibility() == View.INVISIBLE &&
                iv_14.getVisibility() == View.INVISIBLE &&
                iv_21.getVisibility() == View.INVISIBLE &&
                iv_22.getVisibility() == View.INVISIBLE &&
                iv_23.getVisibility() == View.INVISIBLE &&
                iv_24.getVisibility() == View.INVISIBLE &&
                iv_31.getVisibility() == View.INVISIBLE &&
                iv_32.getVisibility() == View.INVISIBLE &&
                iv_33.getVisibility() == View.INVISIBLE &&
                iv_34.getVisibility() == View.INVISIBLE) {

            // All cards matched, display the congratulations dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(memory_game.this);
            builder.setTitle("Congratulations!");
            builder.setMessage("You won the game!");

            builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Reset the game
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.show();
        }
    }

    private void calculate() {
        // Check if the selected cards match
        if (firstCard == secondCard) {
            // Cards match, set them as invisible
            if (clickedFirst == 0) {
                iv_11.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 1) {
                iv_12.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 2) {
                iv_13.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 3) {
                iv_14.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 4) {
                iv_21.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 5) {
                iv_22.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 6) {
                iv_23.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 7) {
                iv_24.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 8) {
                iv_31.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 9) {
                iv_32.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 10) {
                iv_33.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 11) {
                iv_34.setVisibility(View.INVISIBLE);
            }

            if (clickedSecond == 0) {
                iv_11.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 1) {
                iv_12.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 2) {
                iv_13.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 3) {
                iv_14.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 4) {
                iv_21.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 5) {
                iv_22.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 6) {
                iv_23.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 7) {
                iv_24.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 8) {
                iv_31.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 9) {
                iv_32.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 10) {
                iv_33.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 11) {
                iv_34.setVisibility(View.INVISIBLE);
            }

            // Increment points and update the TextView
            playerPoints++;
            tv_p1.setText("Points: " + playerPoints);
        } else {
            // If cards don't match, reset them to the background image
            iv_11.setImageResource(R.drawable.question);
            iv_12.setImageResource(R.drawable.question);
            iv_13.setImageResource(R.drawable.question);
            iv_14.setImageResource(R.drawable.question);
            iv_21.setImageResource(R.drawable.question);
            iv_22.setImageResource(R.drawable.question);
            iv_23.setImageResource(R.drawable.question);
            iv_24.setImageResource(R.drawable.question);
            iv_31.setImageResource(R.drawable.question);
            iv_32.setImageResource(R.drawable.question);
            iv_33.setImageResource(R.drawable.question);
            iv_34.setImageResource(R.drawable.question);
        }

        // Enable all ImageViews again
        iv_11.setEnabled(true);
        iv_12.setEnabled(true);
        iv_13.setEnabled(true);
        iv_14.setEnabled(true);
        iv_21.setEnabled(true);
        iv_22.setEnabled(true);
        iv_23.setEnabled(true);
        iv_24.setEnabled(true);
        iv_31.setEnabled(true);
        iv_32.setEnabled(true);
        iv_33.setEnabled(true);
        iv_34.setEnabled(true);

        // Reset card selection
        cardNumber = 1;

        // Check if the game has ended
        checkEnd();
    }

    private void startFlashCardDistraction() {
        flashCardRunnable = new Runnable() {
            @Override
            public void run() {
                showFlashCard();
                // Schedule the next flashcard display at a random time between 3 to 6 seconds
                flashCardHandler.postDelayed(this, (long) (3000 + Math.random() * 3000));
            }
        };
        // First display after a random initial delay
        flashCardHandler.postDelayed(flashCardRunnable, (long) (3000 + Math.random() * 3000));
    }

    // Method to show flashcard and hide it after a short delay
    private void showFlashCard() {
        flashCard.setVisibility(View.VISIBLE);  // Show flashcard
        flashCard.setOnClickListener(v -> flashCard.setVisibility(View.GONE));  // Allow user to hide it by clicking

        // Auto-hide flashcard after 1 second
        new Handler().postDelayed(() -> flashCard.setVisibility(View.GONE), 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to prevent memory leaks
        flashCardHandler.removeCallbacks(flashCardRunnable);
    }

}
