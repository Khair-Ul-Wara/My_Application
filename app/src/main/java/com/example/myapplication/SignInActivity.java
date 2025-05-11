//package com.example.myapplication;
//
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.material.textfield.TextInputLayout;
//import java.util.regex.Pattern;
//
//public class SignInActivity extends AppCompatActivity {
//
//    private EditText etEmail, etPassword;
//    private Button btnSignIn;
//    private TextView tvSignUpLink;
//    private TextInputLayout passwordInputLayout;
//    private static final Pattern EMAIL_PATTERN = Pattern.compile(
//            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
//            Pattern.CASE_INSENSITIVE);
//
//    private MediaPlayer mediaPlayer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signin);
//
//        // Initialize MediaPlayer
//        mediaPlayer = MediaPlayer.create(this, R.raw.clickable);
//
//        // Initialize views
//        etEmail = findViewById(R.id.etEmail);
//        etPassword = findViewById(R.id.etPassword);
//        btnSignIn = findViewById(R.id.btnSignIn);
//        tvSignUpLink = findViewById(R.id.tvSignUpLink);
//        passwordInputLayout = findViewById(R.id.passwordInputLayout);
//
//        // Set click listener for the SignIn button
//        btnSignIn.setOnClickListener(v -> {
//            playClickSound();
//
//            String email = etEmail.getText().toString().trim();
//            String password = etPassword.getText().toString().trim();
//
//            // Reset errors
//            etEmail.setError(null);
//            passwordInputLayout.setError(null);
//
//            if (TextUtils.isEmpty(email)) {
//                playClickSound();
//                etEmail.setError("Email required");
//                etEmail.requestFocus();
//            } else if (!isValidEmail(email)) {
//                playClickSound();
//                etEmail.setError("Please enter a valid email");
//                etEmail.requestFocus();
//            } else if (TextUtils.isEmpty(password)) {
//                playClickSound();
//                passwordInputLayout.setError("Password required");
//                etPassword.requestFocus();
//            } else {
//                showLoading(true);
//                // Simulate network request
//                new Handler().postDelayed(() -> {
//                    showLoading(false);
//                    Toast.makeText(SignInActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(SignInActivity.this, NotesActivity.class));
//                    finish();
//                }, 1500);
//            }
//        });
//
//        // Set click listener for the SignUp link
//        tvSignUpLink.setOnClickListener(v -> {
//            playClickSound();
//            startActivity(new Intent(SignInActivity.this, SignupActivity.class));
//            finish();
//        });
//    }
//
//    private boolean isValidEmail(String email) {
//        return EMAIL_PATTERN.matcher(email).matches();
//    }
//
//    private void showLoading(boolean show) {
//        btnSignIn.setEnabled(!show);
//        btnSignIn.setText(show ? "Signing in..." : "Sign In");
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

import com.example.myapplication.databinding.ActivitySigninBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    ActivitySigninBinding binding;
    FirebaseAuth mAuth;
    MediaPlayer mediaPlayer; // MediaPlayer object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mediaPlayer = MediaPlayer.create(this, R.raw.clickable); // Initialize MediaPlayer

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();

                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this, NotesActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        binding.tvSignUpLink.setOnClickListener(v -> {
            playClickSound();
            startActivity(new Intent(SignInActivity.this, SignupActivity.class));
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
