//package com.example.myapplication;
//
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.material.textfield.TextInputLayout;
//
//public class SignupActivity extends AppCompatActivity {
//
//    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
//    private Button btnSignup;
//    private TextView tvSignInLink;
//    private TextInputLayout passwordInputLayout, confirmPasswordInputLayout;
//
//    private MediaPlayer mediaPlayer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        // Initialize MediaPlayer
//        mediaPlayer = MediaPlayer.create(this, R.raw.clickable);
//
//        // Initialize views
//        etFullName = findViewById(R.id.etFullName);
//        etEmail = findViewById(R.id.etEmail);
//        etPassword = findViewById(R.id.etPassword);
//        etConfirmPassword = findViewById(R.id.etConfirmPassword);
//        btnSignup = findViewById(R.id.button);
//        tvSignInLink = findViewById(R.id.tvSignInAction);
//        passwordInputLayout = findViewById(R.id.passwordInputLayout);
//        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
//
//        // Real-time password validation
//        etPassword.addTextChangedListener(new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//                playClickSound();
//                validatePassword();
//            }
//            @Override public void afterTextChanged(Editable s) {}
//        });
//
//        etConfirmPassword.addTextChangedListener(new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
//                playClickSound();
//                validatePasswordMatch();
//            }
//            @Override public void afterTextChanged(Editable s) {}
//        });
//
//        // SignUp button click
//        btnSignup.setOnClickListener(v -> {
//            playClickSound();
//            if (validateAllFields()) {
//                showLoading(true);
//                new Handler().postDelayed(() -> {
//                    showLoading(false);
//                    Toast.makeText(SignupActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(SignupActivity.this, NotesActivity.class));
//                    finish();
//                }, 1500);
//            }
//        });
//
//        // SignIn link click
//        tvSignInLink.setOnClickListener(v -> {
//            playClickSound();
//            startActivity(new Intent(SignupActivity.this, SignInActivity.class));
//            finish();
//        });
//    }
//
//    private boolean validateAllFields() {
//        boolean isValid = true;
//
//        if (etFullName.getText().toString().trim().isEmpty()) {
//            playClickSound();
//            etFullName.setError("Full name required");
//            isValid = false;
//        }
//
//        if (etEmail.getText().toString().trim().isEmpty() ||
//                !android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
//            playClickSound();
//            etEmail.setError("Valid email required");
//            isValid = false;
//        }
//
//        if (!validatePassword()) {
//            isValid = false;
//        }
//
//        if (!validatePasswordMatch()) {
//            isValid = false;
//        }
//
//        return isValid;
//    }
//
//    private boolean validatePassword() {
//        String password = etPassword.getText().toString();
//        if (password.length() < 6) {
//            passwordInputLayout.setError("Password must be at least 6 characters");
//            return false;
//        } else {
//            passwordInputLayout.setError(null);
//            return true;
//        }
//    }
//
//    private boolean validatePasswordMatch() {
//        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
//            confirmPasswordInputLayout.setError("Passwords don't match");
//            return false;
//        } else {
//            confirmPasswordInputLayout.setError(null);
//            return true;
//        }
//    }
//
//    private void showLoading(boolean show) {
//        btnSignup.setEnabled(!show);
//        btnSignup.setText(show ? "Creating account..." : "Sign Up");
//    }
//
//    private void playClickSound() {
//        if (mediaPlayer != null) {
//            mediaPlayer.start();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//        super.onDestroy();
//    }
//}
package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth mAuth;
    MediaPlayer mediaPlayer; // MediaPlayer for sound

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mediaPlayer = MediaPlayer.create(this, R.raw.clickable); // Load sound

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();

                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, SignInActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        binding.tvSignInAction.setOnClickListener(v -> {
            playClickSound();
            startActivity(new Intent(SignupActivity.this, SignInActivity.class));
            finish();
        });
    }

    private void playClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
