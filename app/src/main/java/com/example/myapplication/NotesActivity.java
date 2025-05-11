//package com.example.myapplication;
//
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.firestore.DocumentSnapshot;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Date;
//
//
//public class NotesActivity extends AppCompatActivity {
//    private static final String TAG = "NotesActivity";
//
//    private ListView notesListView;
//    private FloatingActionButton addNoteButton, addTaskButton;
//    private TextView notesHeader;
//    private ArrayList<String> notesList = new ArrayList<>();
//    private ArrayList<String> noteDocIds = new ArrayList<>();
//    private ArrayList<TaskItem> taskList = new ArrayList<>();
//    private ArrayList<String> taskDocIds = new ArrayList<>();
//    private NotesAdapter notesAdapter;
//    private TaskAdapter taskAdapter;
//    private boolean showingNotes = true;
//    private FirestoreHelper firestoreHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notes);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//        }
//
//        firestoreHelper = new FirestoreHelper();
//        initializeViews();
//        setupAdapters();
//        loadNotesFromFirestore();
//        setupClickListeners();
//    }
//
//    private void initializeViews() {
//        notesListView = findViewById(R.id.notesListView);
//        addNoteButton = findViewById(R.id.addNoteButton);
//        addTaskButton = findViewById(R.id.addTaskButton);
//        notesHeader = findViewById(R.id.notesHeader);
//    }
//
//    private void setupAdapters() {
//        notesAdapter = new NotesAdapter(this, notesList);
//        taskAdapter = new TaskAdapter(this, taskList, firestoreHelper);
//        notesListView.setAdapter(notesAdapter);
//    }
//
//    private void loadNotesFromFirestore() {
//        firestoreHelper.getAllNotes(new FirestoreHelper.FirestoreListCallback() {
//            @Override
//            public void onSuccess(List<DocumentSnapshot> documents) {
//                notesList.clear();
//                noteDocIds.clear();
//                for (DocumentSnapshot doc : documents) {
//                    String content = doc.getString("content");
//                    if (content != null) {
//                        notesList.add(content);
//                        noteDocIds.add(doc.getId());
//                    } else {
//                        Log.w(TAG, "Found note with null content, docId: " + doc.getId());
//                    }
//                }
//                notesAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.e(TAG, "Error loading notes", e);
//                Toast.makeText(NotesActivity.this, "Error loading notes", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//
//
//    private void setupClickListeners() {
//        notesListView.setOnItemClickListener((parent, view, position, id) -> {
//            playSound();
//            if (showingNotes) {
//                if (position < notesList.size()) {
//                    openNoteDetail(notesList.get(position), noteDocIds.get(position), position);
//                }
//            } else {
//                if (position < taskList.size()) {
//                    openTaskDetail(taskList.get(position), position);
//                }
//            }
//        });
//
//        addNoteButton.setOnClickListener(v -> {
//            playSound();
//            startActivityForResult(new Intent(NotesActivity.this, NoteCreationActivity.class), 1);
//        });
//
//        addTaskButton.setOnClickListener(v -> {
//            playSound();
//            startActivityForResult(new Intent(NotesActivity.this, TaskCreationActivity.class), 3);
//        });
//    }
//
//    private void openNoteDetail(String noteContent, String docId, int position) {
//        Intent intent = new Intent(this, NoteDetailActivity.class);
//        intent.putExtra("note_content", noteContent != null ? noteContent : "");
//        intent.putExtra("note_id", docId);
//        intent.putExtra("note_position", position);
//        startActivityForResult(intent, 2);
//    }
//
//    private void openTaskDetail(TaskItem task, int position) {
//        Intent intent = new Intent(this, TaskDetailActivity.class);
//        intent.putExtra("task_item", task);
//        intent.putExtra("task_position", position);
//        intent.putExtra("task_id", taskDocIds.get(position));
//        startActivityForResult(intent, 4);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.notes_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_toggle) {
//            playSound();
//            toggleView();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void toggleView() {
//        showingNotes = !showingNotes;
//        notesHeader.setText(showingNotes ? "Notes" : "To-Do List");
//        addNoteButton.setVisibility(showingNotes ? FloatingActionButton.VISIBLE : FloatingActionButton.GONE);
//        addTaskButton.setVisibility(showingNotes ? FloatingActionButton.GONE : FloatingActionButton.VISIBLE);
//        notesListView.setAdapter(showingNotes ? notesAdapter : taskAdapter);
//
//        if (!showingNotes) {
//            loadTasksFromFirestore();
//        }
//    }
//
//    private void playSound() {
//        try {
//            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.clickable);
//            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
//            mediaPlayer.start();
//        } catch (Exception e) {
//            Log.e(TAG, "Error playing sound", e);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && data != null) {
//            if (requestCode == 1) { // From NoteCreationActivity
//                String newNote = data.getStringExtra("note_content");
//                if (newNote != null) {
//                    addNoteToFirestore(newNote);
//                }
//            } else if (requestCode == 2) { // From NoteDetailActivity
//                handleNoteDetailResult(data);
//            } else if (requestCode == 3) { // From TaskCreationActivity
//                TaskItem newTask = data.getParcelableExtra("task_item");
//                if (newTask != null) {
//                    addTaskToFirestore(newTask);
//                }
//            } else if (requestCode == 4) { // From TaskDetailActivity
//                handleTaskDetailResult(data);
//            }
//        }
//    }
//
//    private void handleNoteDetailResult(Intent data) {
//        int position = data.getIntExtra("note_position", -1);
//        String updatedNote = data.getStringExtra("note_content");
//        String docId = data.getStringExtra("note_id");
//
//        if (position >= 0 && position < notesList.size() && docId != null) {
//            if (updatedNote == null) {
//                deleteNoteFromFirestore(docId, position);
//            } else {
//                updateNoteInFirestore(docId, position, updatedNote);
//            }
//        }
//    }
//
//    private void handleTaskDetailResult(Intent data) {
//        int position = data.getIntExtra("task_position", -1);
//        TaskItem updatedTask = data.getParcelableExtra("task_item");
//        String docId = data.getStringExtra("task_id");
//
//        if (position >= 0 && position < taskList.size() && docId != null && updatedTask != null) {
//            updateTaskInFirestore(docId, position, updatedTask);
//        }
//    }
//
//    private void addNoteToFirestore(String noteContent) {
//        firestoreHelper.addNote(noteContent != null ? noteContent : "", new FirestoreHelper.FirestoreCallback() {
//            @Override
//            public void onSuccess(String documentId) {
//                notesList.add(noteContent != null ? noteContent : "");
//                noteDocIds.add(documentId);
//                notesAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.e(TAG, "Error adding note", e);
//                Toast.makeText(NotesActivity.this, "Error adding note", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void updateNoteInFirestore(String docId, int position, String updatedNote) {
//        firestoreHelper.updateNote(docId, updatedNote != null ? updatedNote : "", new FirestoreHelper.FirestoreCallback() {
//            @Override
//            public void onSuccess(String documentId) {
//                notesList.set(position, updatedNote != null ? updatedNote : "");
//                notesAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.e(TAG, "Error updating note", e);
//                Toast.makeText(NotesActivity.this, "Error updating note", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void deleteNoteFromFirestore(String docId, int position) {
//        firestoreHelper.deleteNote(docId, new FirestoreHelper.FirestoreCallback() {
//            @Override
//            public void onSuccess(String documentId) {
//                if (position >= 0 && position < notesList.size()) {
//                    notesList.remove(position);
//                    noteDocIds.remove(position);
//                    notesAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.e(TAG, "Error deleting note", e);
//                Toast.makeText(NotesActivity.this, "Error deleting note", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//
//
//    // Add this method to your NotesActivity class
//    public String getTaskDocId(int position) {
//        if (position >= 0 && position < taskDocIds.size()) {
//            return taskDocIds.get(position);
//        }
//        return null;
//    }
//
//    // Update loadTasksFromFirestore method
//    private void loadTasksFromFirestore() {
//        firestoreHelper.getAllTasks(new FirestoreHelper.FirestoreListCallback() {
//            @Override
//            public void onSuccess(List<DocumentSnapshot> documents) {
//                taskList.clear();
//                taskDocIds.clear();
//                for (DocumentSnapshot doc : documents) {
//                    String taskText = doc.getString("taskText");
//                    String description = doc.getString("description");
//                    Boolean isDone = doc.getBoolean("isDone");
//                    Date dueDate = doc.getDate("dueDate");
//                    Boolean hasReminder = doc.getBoolean("hasReminder");
//
//                    if (taskText != null && isDone != null) {
//                        taskList.add(new TaskItem(
//                                taskText,
//                                description != null ? description : "",
//                                isDone,
//                                dueDate,
//                                hasReminder != null ? hasReminder : false
//                        ));
//                        taskDocIds.add(doc.getId());
//                    }
//                }
//                taskAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.e(TAG, "Error loading tasks", e);
//                Toast.makeText(NotesActivity.this, "Error loading tasks", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // Update addTaskToFirestore method
//    private void addTaskToFirestore(TaskItem task) {
//        firestoreHelper.addTask(task, new FirestoreHelper.FirestoreCallback() {
//            @Override
//            public void onSuccess(String documentId) {
//                taskList.add(task);
//                taskDocIds.add(documentId);
//                taskAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.e(TAG, "Error adding task", e);
//                Toast.makeText(NotesActivity.this, "Error adding task", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // Update updateTaskInFirestore method
//    private void updateTaskInFirestore(String docId, int position, TaskItem updatedTask) {
//        firestoreHelper.updateTask(docId, updatedTask, new FirestoreHelper.FirestoreCallback() {
//            @Override
//            public void onSuccess(String documentId) {
//                taskList.set(position, updatedTask);
//                taskAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.e(TAG, "Error updating task", e);
//                Toast.makeText(NotesActivity.this, "Error updating task", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
    private static final String TAG = "NotesActivity";

    private ListView notesListView;
    private FloatingActionButton addNoteButton, addTaskButton;
    private TextView notesHeader;
    private ArrayList<String> notesList = new ArrayList<>();
    private ArrayList<String> noteDocIds = new ArrayList<>();
    private ArrayList<TaskItem> taskList = new ArrayList<>();
    private ArrayList<String> taskDocIds = new ArrayList<>();
    private NotesAdapter notesAdapter;
    private TaskAdapter taskAdapter;
    private boolean showingNotes = true;
    private boolean isEditMode = false;

    private FirestoreHelper firestoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        firestoreHelper = new FirestoreHelper();
        initializeViews();
        setupAdapters();
        loadNotesFromFirestore();
        setupClickListeners();
    }

    private void initializeViews() {
        notesListView = findViewById(R.id.notesListView);
        addNoteButton = findViewById(R.id.addNoteButton);
        addTaskButton = findViewById(R.id.addTaskButton);
        notesHeader = findViewById(R.id.notesHeader);
    }

    private void setupAdapters() {
        notesAdapter = new NotesAdapter(this, notesList);
        taskAdapter = new TaskAdapter(this, taskList, firestoreHelper);
        notesListView.setAdapter(notesAdapter);
    }

    private void setupClickListeners() {
        notesListView.setOnItemClickListener((parent, view, position, id) -> {
            playSound();
            isEditMode = true;
            if (showingNotes) {
                openNoteDetail(notesList.get(position), noteDocIds.get(position), position);
            } else {
                openTaskDetail(taskList.get(position), position);
            }
        });

        addNoteButton.setOnClickListener(v -> {
            playSound();
            isEditMode = false;
            startActivityForResult(new Intent(NotesActivity.this, NoteCreationActivity.class), 1);
        });

        addTaskButton.setOnClickListener(v -> {
            playSound();
            isEditMode = false;
            startActivityForResult(new Intent(NotesActivity.this, TaskCreationActivity.class), 3);
        });
    }

    private void openNoteDetail(String noteContent, String docId, int position) {
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra("note_content", noteContent != null ? noteContent : "");
        intent.putExtra("note_id", docId);
        intent.putExtra("note_position", position);
        startActivityForResult(intent, 2);
    }

    private void openTaskDetail(TaskItem task, int position) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra("task_item", task);
        intent.putExtra("task_position", position);
        intent.putExtra("task_id", taskDocIds.get(position));
        startActivityForResult(intent, 4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle) {
            playSound();
            toggleView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleView() {
        showingNotes = !showingNotes;
        notesHeader.setText(showingNotes ? "Notes" : "To-Do List");
        addNoteButton.setVisibility(showingNotes ? FloatingActionButton.VISIBLE : FloatingActionButton.GONE);
        addTaskButton.setVisibility(showingNotes ? FloatingActionButton.GONE : FloatingActionButton.VISIBLE);
        notesListView.setAdapter(showingNotes ? notesAdapter : taskAdapter);

        if (!showingNotes) {
            loadTasksFromFirestore();
        }
    }

    private void playSound() {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.clickable);
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "Error playing sound", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                String newNote = data.getStringExtra("note_content");
                if (newNote != null) {
                    addNoteToFirestore(newNote);
                }
            } else if (requestCode == 2) {
                handleNoteDetailResult(data);
            } else if (requestCode == 3) {
                TaskItem newTask = data.getParcelableExtra("task_item");
                String taskId = data.getStringExtra("task_id");
                if (newTask != null && taskId != null) {
                    if (isEditMode) {
                        int position = findTaskPositionById(taskId);
                        if (position != -1) {
                            updateTaskInFirestore(taskId, position, newTask);
                        }
                    } else {
                        addTaskToFirestore(newTask);
                    }
                }
            } else if (requestCode == 4) {
                handleTaskDetailResult(data);
            }
        }
    }

    private void handleNoteDetailResult(Intent data) {
        int position = data.getIntExtra("note_position", -1);
        String updatedNote = data.getStringExtra("note_content");
        String docId = data.getStringExtra("note_id");

        if (position >= 0 && position < notesList.size() && docId != null) {
            if (updatedNote == null) {
                deleteNoteFromFirestore(docId, position);
            } else {
                updateNoteInFirestore(docId, position, updatedNote);
            }
        }
    }

    private void handleTaskDetailResult(Intent data) {
        int position = data.getIntExtra("task_position", -1);
        TaskItem updatedTask = data.getParcelableExtra("task_item");
        String docId = data.getStringExtra("task_id");

        if (position >= 0 && position < taskList.size() && docId != null && updatedTask != null) {
            updateTaskInFirestore(docId, position, updatedTask);
        }
    }

    private void addNoteToFirestore(String noteContent) {
        firestoreHelper.addNote(noteContent != null ? noteContent : "", new FirestoreHelper.FirestoreCallback() {
            @Override
            public void onSuccess(String documentId) {
                notesList.add(noteContent);
                noteDocIds.add(documentId);
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error adding note", e);
                Toast.makeText(NotesActivity.this, "Error adding note", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNoteInFirestore(String docId, int position, String updatedNote) {
        firestoreHelper.updateNote(docId, updatedNote, new FirestoreHelper.FirestoreCallback() {
            @Override
            public void onSuccess(String documentId) {
                notesList.set(position, updatedNote);
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error updating note", e);
                Toast.makeText(NotesActivity.this, "Error updating note", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteNoteFromFirestore(String docId, int position) {
        firestoreHelper.deleteNote(docId, new FirestoreHelper.FirestoreCallback() {
            @Override
            public void onSuccess(String documentId) {
                notesList.remove(position);
                noteDocIds.remove(position);
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error deleting note", e);
                Toast.makeText(NotesActivity.this, "Error deleting note", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadNotesFromFirestore() {
        firestoreHelper.getAllNotes(new FirestoreHelper.FirestoreListCallback() {
            @Override
            public void onSuccess(List<DocumentSnapshot> documents) {
                notesList.clear();
                noteDocIds.clear();
                for (DocumentSnapshot doc : documents) {
                    String content = doc.getString("content"); // Changed from "noteContent" to "content"
                    if (content != null) {
                        notesList.add(content);
                        noteDocIds.add(doc.getId());
                    } else {
                        Log.w(TAG, "Found note with null content, docId: " + doc.getId());
                    }
                }
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error loading notes", e);
                Toast.makeText(NotesActivity.this, "Error loading notes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTasksFromFirestore() {
        firestoreHelper.getAllTasks(new FirestoreHelper.FirestoreListCallback() {
            @Override
            public void onSuccess(List<DocumentSnapshot> documents) {
                taskList.clear();
                taskDocIds.clear();
                for (DocumentSnapshot doc : documents) {
                    String taskText = doc.getString("taskText");
                    String description = doc.getString("description");
                    Boolean isDone = doc.getBoolean("isDone");
                    Date dueDate = doc.getDate("dueDate");
                    Boolean hasReminder = doc.getBoolean("hasReminder");

                    if (taskText != null && isDone != null) {
                        taskList.add(new TaskItem(
                                taskText,
                                description != null ? description : "",
                                isDone,
                                dueDate,
                                hasReminder != null ? hasReminder : false
                        ));
                        taskDocIds.add(doc.getId());
                    }
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error loading tasks", e);
                Toast.makeText(NotesActivity.this, "Error loading tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTaskToFirestore(TaskItem task) {
        firestoreHelper.addTask(task, new FirestoreHelper.FirestoreCallback() {
            @Override
            public void onSuccess(String documentId) {
                taskList.add(task);
                taskDocIds.add(documentId);
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error adding task", e);
                Toast.makeText(NotesActivity.this, "Error adding task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTaskInFirestore(String docId, int position, TaskItem updatedTask) {
        firestoreHelper.updateTask(docId, updatedTask, new FirestoreHelper.FirestoreCallback() {
            @Override
            public void onSuccess(String documentId) {
                taskList.set(position, updatedTask);
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error updating task", e);
                Toast.makeText(NotesActivity.this, "Error updating task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int findTaskPositionById(String taskId) {
        for (int i = 0; i < taskDocIds.size(); i++) {
            if (taskDocIds.get(i).equals(taskId)) {
                return i;
            }
        }
        return -1;
    }

    public String getTaskDocId(int position) {
        if (position >= 0 && position < taskDocIds.size()) {
            return taskDocIds.get(position);
        }
        return null;
    }
}
