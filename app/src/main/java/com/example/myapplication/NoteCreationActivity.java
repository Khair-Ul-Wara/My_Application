//package com.example.myapplication;
//
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import com.google.android.material.textfield.TextInputLayout;
//
//public class NoteCreationActivity extends AppCompatActivity {
//    private EditText noteEditText;
//    private TextInputLayout noteInputLayout;
//    private boolean isEditing = false;
//    private String originalNoteContent = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_note_creation);
//
//        // Setup Toolbar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        // Remove the default title to avoid duplicate with the TextView inside Toolbar
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        noteInputLayout = findViewById(R.id.noteInputLayout);
//        noteEditText = findViewById(R.id.noteEditText);
//
//        // Check if we're editing an existing note
//        if (getIntent() != null && getIntent().hasExtra("is_editing")
//                && getIntent().hasExtra("note_content")) {
//            isEditing = true;
//            originalNoteContent = getIntent().getStringExtra("note_content");
//            noteEditText.setText(originalNoteContent);
//        }
//
//        // Character counter and validation
//        noteEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                noteInputLayout.setCounterEnabled(true);
//                noteInputLayout.setCounterMaxLength(1000);
//                noteInputLayout.setError(null); // Clear error when typing
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        // Set cursor to end of text when editing
//        if (isEditing) {
//            noteEditText.setSelection(noteEditText.getText().length());
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.note_creation_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        playSound(); // Play sound on any menu interaction
//
//        if (item.getItemId() == R.id.action_save) {
//            saveNote();
//            return true;
//        } else if (item.getItemId() == android.R.id.home) {
//            handleBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void saveNote() {
//        String noteContent = noteEditText.getText().toString().trim();
//        if (noteContent.isEmpty()) {
//            noteInputLayout.setError("Note cannot be empty");
//            return;
//        }
//
//        // Check if note was actually modified when editing
//        if (isEditing && noteContent.equals(originalNoteContent)) {
//            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // Return the new/edited note to calling activity
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("note_content", noteContent);
//        resultIntent.putExtra("is_editing", isEditing);
//
//        if (isEditing) {
//            resultIntent.putExtra("original_content", originalNoteContent);
//        }
//
//        setResult(RESULT_OK, resultIntent);
//        finish();
//    }
//
//    private void handleBackPressed() {
//        String currentContent = noteEditText.getText().toString().trim();
//        if (!currentContent.isEmpty() && !currentContent.equals(originalNoteContent)) {
//            // Show confirmation dialog if there are unsaved changes
//            new android.app.AlertDialog.Builder(this)
//                    .setTitle("Discard changes?")
//                    .setMessage("You have unsaved changes. Are you sure you want to discard them?")
//                    .setPositiveButton("Discard", (dialog, which) -> finish())
//                    .setNegativeButton("Cancel", null)
//                    .show();
//        } else {
//            finish();
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        handleBackPressed();
//    }
//
//    private void playSound() {
//        // Play a sound on button/menu interactions
//        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.clickable);
//        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
//        mediaPlayer.start();
//    }
//}
//code change#02:
//package com.example.myapplication;
//
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import com.google.android.material.textfield.TextInputLayout;
//
//public class NoteCreationActivity extends AppCompatActivity {
//    private EditText noteEditText;
//    private TextInputLayout noteInputLayout;
//    private boolean isEditing = false;
//    private String originalNoteContent = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_note_creation);
//
//        // Setup Toolbar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        noteInputLayout = findViewById(R.id.noteInputLayout);
//        noteEditText = findViewById(R.id.noteEditText);
//
//        // Check if we're editing an existing note
//        if (getIntent() != null && getIntent().hasExtra("is_editing")
//                && getIntent().hasExtra("note_content")) {
//            isEditing = true;
//            originalNoteContent = getIntent().getStringExtra("note_content");
//            noteEditText.setText(originalNoteContent);
//        }
//
//        // Character counter and validation
//        noteEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                noteInputLayout.setCounterEnabled(true);
//                noteInputLayout.setCounterMaxLength(1000);
//                noteInputLayout.setError(null); // Clear error when typing
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        // Set cursor to end of text when editing
//        if (isEditing) {
//            noteEditText.setSelection(noteEditText.getText().length());
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.note_creation_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        playSound(); // Play sound on any menu interaction
//
//        if (item.getItemId() == R.id.action_save) {
//            saveNote();
//            return true;
//        } else if (item.getItemId() == android.R.id.home) {
//            handleBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void saveNote() {
//        String noteContent = noteEditText.getText().toString().trim();
//        if (noteContent.isEmpty()) {
//            noteInputLayout.setError("Note cannot be empty");
//            return;
//        }
//
//        // Check if note was actually modified when editing
//        if (isEditing && noteContent.equals(originalNoteContent)) {
//            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // Return the new/edited note to calling activity
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("note_content", noteContent);
//        resultIntent.putExtra("is_editing", isEditing);
//
//        if (isEditing) {
//            resultIntent.putExtra("original_content", originalNoteContent);
//        }
//
//        setResult(RESULT_OK, resultIntent);
//        finish();
//    }
//
//    private void handleBackPressed() {
//        String currentContent = noteEditText.getText().toString().trim();
//        if (!currentContent.isEmpty() && !currentContent.equals(originalNoteContent)) {
//            // Show confirmation dialog if there are unsaved changes
//            new android.app.AlertDialog.Builder(this)
//                    .setTitle("Discard changes?")
//                    .setMessage("You have unsaved changes. Are you sure you want to discard them?")
//                    .setPositiveButton("Discard", (dialog, which) -> finish())
//                    .setNegativeButton("Cancel", null)
//                    .show();
//        } else {
//            finish();
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        handleBackPressed();
//    }
//
//    private void playSound() {
//        // Play a sound on button/menu interactions
//        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.clickable);
//        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
//        mediaPlayer.start();
//    }
//}
//new code:
package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputLayout;

public class NoteCreationActivity extends AppCompatActivity {
    private EditText noteEditText;
    private TextInputLayout noteInputLayout;
    private boolean isEditing = false;
    private String originalNoteContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_creation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        noteInputLayout = findViewById(R.id.noteInputLayout);
        noteEditText = findViewById(R.id.noteEditText);

        if (getIntent() != null && getIntent().hasExtra("is_editing")
                && getIntent().hasExtra("note_content")) {
            isEditing = true;
            originalNoteContent = getIntent().getStringExtra("note_content");
            noteEditText.setText(originalNoteContent);
            noteEditText.setSelection(noteEditText.getText().length());
        }

        noteEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                noteInputLayout.setCounterEnabled(true);
                noteInputLayout.setCounterMaxLength(1000);
                noteInputLayout.setError(null);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_creation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        playSound();
        if (item.getItemId() == R.id.action_save) {
            saveNote();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            handleBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String noteContent = noteEditText.getText().toString().trim();
        if (noteContent.isEmpty()) {
            noteInputLayout.setError("Note cannot be empty");
            return;
        }

        if (isEditing && noteContent.equals(originalNoteContent)) {
            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("note_content", noteContent);
        resultIntent.putExtra("is_editing", isEditing);
        if (isEditing) resultIntent.putExtra("original_content", originalNoteContent);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void handleBackPressed() {
        String currentContent = noteEditText.getText().toString().trim();
        if (!currentContent.isEmpty() && !currentContent.equals(originalNoteContent)) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Discard changes?")
                    .setMessage("You have unsaved changes. Are you sure you want to discard them?")
                    .setPositiveButton("Discard", (dialog, which) -> finish())
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        handleBackPressed();
    }

    private void playSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.clickable);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        mediaPlayer.start();
    }
}
