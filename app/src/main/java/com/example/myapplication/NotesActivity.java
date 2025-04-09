package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private ListView notesListView;
    private FloatingActionButton addNoteButton, addTaskButton;
    private TextView notesHeader;
    private ArrayList<String> notesList;
    private NotesAdapter notesAdapter;
    private boolean showingNotes = true;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize views
        notesListView = findViewById(R.id.notesListView);
        addNoteButton = findViewById(R.id.addNoteButton);
        addTaskButton = findViewById(R.id.addTaskButton);
        notesHeader = findViewById(R.id.notesHeader);

        // Initialize notes list with sample data
        notesList = new ArrayList<>();
        notesList.add("Welcome to your Notes!");
        notesList.add("Tap '+' to add a new note.");
        notesList.add("Swipe left to delete items");

        // Setup adapter with custom layout
        notesAdapter = new NotesAdapter(this, R.layout.note_item, notesList);
        notesListView.setAdapter(notesAdapter);

        // List item click listener
        notesListView.setOnItemClickListener((parent, view, position, id) -> {
            if (showingNotes) {
                Intent intent = new Intent(NotesActivity.this, NoteDetailActivity.class);
                intent.putExtra("note_content", notesList.get(position));
                startActivity(intent);
            } else {
                // Handle task click
            }
        });

        // Add Note Button
        addNoteButton.setOnClickListener(v ->
                startActivity(new Intent(NotesActivity.this, NoteCreationActivity.class)));

        // Add Task Button
        addTaskButton.setOnClickListener(v ->
                startActivity(new Intent(NotesActivity.this, TaskCreationActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle) {
            toggleView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleView() {
        showingNotes = !showingNotes;
        if (showingNotes) {
            notesHeader.setText("Notes");
            addTaskButton.hide();
            addNoteButton.show();
        } else {
            notesHeader.setText("To-Do List");
            addNoteButton.hide();
            addTaskButton.show();
        }
        updateListContent();
    }

    private void updateListContent() {
        notesList.clear();
        if (showingNotes) {
            notesList.add("Personal Notes");
            notesList.add("Work Notes");
            notesList.add("Ideas");
        } else {
            notesList.add("Complete project");
            notesList.add("Buy groceries");
            notesList.add("Call mom");
        }
        notesAdapter.notifyDataSetChanged();
    }
}