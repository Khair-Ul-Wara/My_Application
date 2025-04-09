package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvSignUpLink;
    private TextInputLayout passwordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);

        // Set click listener for the SignIn button
        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Email required");
                etEmail.requestFocus();
            } else if (TextUtils.isEmpty(password)) {
                passwordInputLayout.setError("Password required");
                etPassword.requestFocus();
            } else {
                showLoading(true);
                // Simulate network request
                new Handler().postDelayed(() -> {
                    showLoading(false);
                    Toast.makeText(SignInActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, NotesActivity.class));
                    finish();
                }, 1500);
            }
        });

        // Set click listener for the SignUp link
        tvSignUpLink.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignupActivity.class));
            finish();
        });
    }

    private void showLoading(boolean show) {
        btnSignIn.setEnabled(!show);
        btnSignIn.setText(show ? "Signing in..." : "Sign In");
    }
}