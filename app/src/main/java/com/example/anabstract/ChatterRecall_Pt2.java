package com.example.anabstract;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ChatterRecall_Pt2 extends AppCompatActivity {

    private TextView questionText, scoreCounter;
    private Button option1, option2, option3, option4;
    private ImageButton homeButton;
    private int score = 0;
    private int questionIndex = 0;
    private Random random;
    private List<Question> questionsList;
    private List<Integer> usedQuestionIndexes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatter_recall_pt2);

        // Initialize views
        questionText = findViewById(R.id.questionText);
        scoreCounter = findViewById(R.id.scoreCounter);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        homeButton = findViewById(R.id.homeButton);

        // Initialize question list and random selector
        random = new Random();
        questionsList = loadQuestions();

        // Set up option buttons
        setupOptionButtons();

        // Load the first question
        loadNewQuestion();
    }

    private void setupOptionButtons() {
        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button selectedButton = (Button) view;
                checkAnswer(selectedButton.getText().toString(), selectedButton);
            }
        };

        option1.setOnClickListener(optionClickListener);
        option2.setOnClickListener(optionClickListener);
        option3.setOnClickListener(optionClickListener);
        option4.setOnClickListener(optionClickListener);
    }

    private List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Why did Aswathama kill Abhimanyu's child?", "1", "2", "3", "4", "1"));
        questions.add(new Question("Who is the son of Pandu?", "Yudhishthira", "Bhima", "Arjuna", "All", "All"));
        questions.add(new Question("Who was the teacher of Kauravas and Pandavas?", "Kripacharya", "Drona", "Vidura", "Bhishma", "Drona"));
        questions.add(new Question("Who broke the Chakravyuha?", "Arjuna", "Bhima", "Abhimanyu", "Krishna", "Abhimanyu"));
        questions.add(new Question("Who gave the Gita's knowledge?", "Krishna", "Arjuna", "Bhishma", "Draupadi", "Krishna"));
        return questions;
    }

    private void loadNewQuestion() {
        if (questionIndex < questionsList.size()) {
            // Pick a random question that hasn't been used before
            int randomIndex;
            do {
                randomIndex = random.nextInt(questionsList.size());
            } while (usedQuestionIndexes.contains(randomIndex));
            usedQuestionIndexes.add(randomIndex);

            Question currentQuestion = questionsList.get(randomIndex);

            // Set question and options
            questionText.setText(currentQuestion.getQuestionText());
            option1.setText(currentQuestion.getOption1());
            option2.setText(currentQuestion.getOption2());
            option3.setText(currentQuestion.getOption3());
            option4.setText(currentQuestion.getOption4());

            questionIndex++;
        } else {
            showGameOverPopup();
        }
    }

    private void showGameOverPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("Your final score is: " + score);

        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetQuiz();
            }
        });

        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // Exit the activity
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetQuiz() {
        score = 0;
        questionIndex = 0;
        usedQuestionIndexes.clear();
        scoreCounter.setText(String.valueOf(score));
        loadNewQuestion();
    }

    private void checkAnswer(String selectedAnswer, Button selectedButton) {
        Question currentQuestion = questionsList.get(usedQuestionIndexes.get(usedQuestionIndexes.size() - 1));

        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            score++;
            scoreCounter.setText(String.valueOf(score));
//            selectedButton.setBackgroundResource(R.color.green); // Set the button to green
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
//            selectedButton.setBackgroundResource(R.color.red); // Set the button to red
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
        }

        // Disable the other options
        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);

        // Load the next question after a short delay
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        loadNewQuestion();
                        // Re-enable the options for the next question
                        option1.setEnabled(true);
                        option2.setEnabled(true);
                        option3.setEnabled(true);
                        option4.setEnabled(true);
                        // Reset the button colors
//                        option1.setBackgroundResource(R.color.skyblue);
//                        option2.setBackgroundResource(R.color.skyblue);
//                        option3.setBackgroundResource(R.color.skyblue);
//                        option4.setBackgroundResource(R.color.skyblue);
                    }
                },
                1000 // 1 second delay
        );
    }
}