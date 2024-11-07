package com.example.anabstract;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {

    private EditText signupName, signupEmail, signupUsername, signupPassword;
    private TextView loginRedirectText;
    private Button signupButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    // Regular expression for validating a strong password
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +      // At least 1 digit
                    "(?=.*[A-Z])" +      // At least 1 uppercase letter
                    "(?=.*[a-z])" +      // At least 1 lowercase letter
                    "(?=.*[@#$%^&+=!])" + // At least 1 special character
                    "(?=\\S+$)" +        // No white spaces
                    ".{8,}" +            // At least 8 characters
                    "$"                  // End of string
    );

    // Regular expression for validating a name (only letters and spaces)
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]+$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize UI elements
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from the input fields
                String name = signupName.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String username = signupUsername.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();

                // Validate inputs
                if (!validateName(name) || !validateEmail(email) || !validateUsername(username) || !validatePassword(password)) {
                    return;
                }

                // Initialize Firebase and save data
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                HelperClass helperClass = new HelperClass(name, email, username, password);
                reference.child(username).setValue(helperClass);

                Toast.makeText(Registration.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, Login.class); // Assuming the Login activity is named Login
                startActivity(intent);
            }
        });
    }

    // Validate the name (only allows alphabets and spaces)
    private boolean validateName(String name) {
        if (name.isEmpty()) {
            signupName.setError("Name cannot be empty");
            return false;
        } else if (!NAME_PATTERN.matcher(name).matches()) {
            signupName.setError("Only alphabets are allowed in the name");
            return false;
        } else {
            signupName.setError(null);
            return true;
        }
    }

    // Validate the email (proper email format)
    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            signupEmail.setError("Email cannot be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("Please enter a valid email");
            return false;
        } else {
            signupEmail.setError(null);
            return true;
        }
    }

    // Validate the username (non-empty)
    private boolean validateUsername(String username) {
        if (username.isEmpty()) {
            signupUsername.setError("Username cannot be empty");
            return false;
        } else {
            signupUsername.setError(null);
            return true;
        }
    }

    // Validate the password (at least 8 characters, with uppercase, lowercase, and special char)
    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            signupPassword.setError("Password cannot be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            signupPassword.setError("Password must contain at least 8 characters, including uppercase, lowercase, digit, and special character");
            return false;
        } else {
            signupPassword.setError(null);
            return true;
        }
    }
}
