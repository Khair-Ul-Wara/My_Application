package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class NoteDetailActivity extends AppCompatActivity {
    private TextInputEditText noteContentEditText;
    private FloatingActionButton saveButton, deleteButton;
    private String noteId;
    private int notePosition;
    private FirestoreHelper firestoreHelper;
    private String originalContent;
    private boolean hasChanges = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        firestoreHelper = new FirestoreHelper();
        initializeViews();
        setupToolbar();
        setupTextWatcher();

        if (!getIntentData()) {
            showErrorAndFinish("Invalid note data");
            return;
        }

        setupButtonListeners();
    }

    private void initializeViews() {
        noteContentEditText = findViewById(R.id.noteContentEditText);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> handleBackPressed());
    }

    private void setupTextWatcher() {
        noteContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasChanges = !s.toString().equals(originalContent);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private boolean getIntentData() {
        Intent intent = getIntent();
        if (intent == null) return false;

        noteId = intent.getStringExtra("note_id");
        notePosition = intent.getIntExtra("note_position", -1);
        String noteContent = intent.getStringExtra("note_content");

        if (noteId == null || notePosition == -1) return false;

        originalContent = noteContent != null ? noteContent : "";
        noteContentEditText.setText(originalContent);
        return true;
    }

    private void setupButtonListeners() {
        saveButton.setOnClickListener(v -> saveNote());
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void saveNote() {
        String updatedContent = noteContentEditText.getText().toString().trim();
        if (updatedContent.isEmpty()) {
            showError("Note cannot be empty");
            return;
        }

        if (!hasChanges) {
            showInfo("No changes made");
            finish();
            return;
        }

        firestoreHelper.updateNote(noteId, updatedContent, new FirestoreHelper.FirestoreCallback() {
            @Override
            public void onSuccess(String documentId) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("note_position", notePosition);
                resultIntent.putExtra("note_content", updatedContent);
                resultIntent.putExtra("note_id", noteId);
                setResult(RESULT_OK, resultIntent);
                showSuccess("Note saved successfully");
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                showError("Error saving note");
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> deleteNote())
                .setNegativeButton("Cancel", null)
                .setBackground(getResources().getDrawable(R.drawable.dialog_background))
                .show();
    }

    private void deleteNote() {
        firestoreHelper.deleteNote(noteId, new FirestoreHelper.FirestoreCallback() {
            @Override
            public void onSuccess(String documentId) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("note_position", notePosition);
                resultIntent.putExtra("note_content", (String) null);
                resultIntent.putExtra("note_id", noteId);
                setResult(RESULT_OK, resultIntent);
                showSuccess("Note deleted");
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                showError("Error deleting note");
            }
        });
    }

    private void handleBackPressed() {
        if (hasChanges) {
            showUnsavedChangesDialog();
        } else {
            finish();
        }
    }

    private void showUnsavedChangesDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Save before exiting?")
                .setPositiveButton("Save", (dialog, which) -> saveNote())
                .setNegativeButton("Discard", (dialog, which) -> finish())
                .setNeutralButton("Cancel", null)
                .setBackground(getResources().getDrawable(R.drawable.dialog_background))
                .show();
    }

    @Override
    public void onBackPressed() {
        handleBackPressed();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showInfo(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorAndFinish(String message) {
        showError(message);
        finish();
    }
}