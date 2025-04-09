package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

public class SignupActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignup;
    private TextView tvSignInLink;
    private TextInputLayout passwordInputLayout, confirmPasswordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.button);
        tvSignInLink = findViewById(R.id.tvSignInAction);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);

        // Real-time password validation
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePasswordMatch();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // SignUp button onClick listener
        btnSignup.setOnClickListener(v -> {
            if (validateAllFields()) {
                showLoading(true);
                // Simulate network request
                new Handler().postDelayed(() -> {
                    showLoading(false);
                    Toast.makeText(SignupActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, NotesActivity.class));
                    finish();
                }, 1500);
            }
        });

        // SignIn link click listener
        tvSignInLink.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, SignInActivity.class));
            finish();
        });
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        if (etFullName.getText().toString().trim().isEmpty()) {
            etFullName.setError("Full name required");
            isValid = false;
        }

        if (etEmail.getText().toString().trim().isEmpty() ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError("Valid email required");
            isValid = false;
        }

        if (!validatePassword()) {
            isValid = false;
        }

        if (!validatePasswordMatch()) {
            isValid = false;
        }

        return isValid;
    }

    private boolean validatePassword() {
        String password = etPassword.getText().toString();
        if (password.length() < 6) {
            passwordInputLayout.setError("Password must be at least 6 characters");
            return false;
        } else {
            passwordInputLayout.setError(null);
            return true;
        }
    }

    private boolean validatePasswordMatch() {
        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            confirmPasswordInputLayout.setError("Passwords don't match");
            return false;
        } else {
            confirmPasswordInputLayout.setError(null);
            return true;
        }
    }

    private void showLoading(boolean show) {
        btnSignup.setEnabled(!show);
        btnSignup.setText(show ? "Creating account..." : "Sign Up");
    }
}