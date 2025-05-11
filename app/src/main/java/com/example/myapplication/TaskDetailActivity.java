package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {
    private TaskItem task;
    private String taskId;
    private FirestoreHelper firestoreHelper;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        firestoreHelper = new FirestoreHelper();
        task = getIntent().getParcelableExtra("task_item");
        taskId = getIntent().getStringExtra("task_id");

        if (task == null || taskId == null) {
            finish();
            return;
        }

        setupToolbar();
        populateTaskData();
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void populateTaskData() {
        TextView taskTitle = findViewById(R.id.taskTitle);
        TextView taskDescription = findViewById(R.id.taskDescription);
        TextView taskDueDate = findViewById(R.id.taskDueDate);
        CheckBox taskStatus = findViewById(R.id.taskStatus);
        CheckBox taskReminder = findViewById(R.id.taskReminder);

        taskTitle.setText(task.getTaskText());
        taskDescription.setText(task.getDescription());
        taskStatus.setChecked(task.isDone());
        taskReminder.setChecked(task.hasReminder());

        if (task.getDueDate() != null) {
            taskDueDate.setText(dateFormat.format(task.getDueDate()));
        } else {
            taskDueDate.setText("No due date set");
        }

        taskStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
            updateTaskInFirestore();
        });

        taskReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setHasReminder(isChecked);
            updateTaskInFirestore();
        });
    }

    private void updateTaskInFirestore() {
        firestoreHelper.updateTask(taskId, task, new FirestoreHelper.FirestoreCallback() {
            @Override
            public void onSuccess(String documentId) {
                // Update successful
            }

            @Override
            public void onFailure(Exception e) {
                // Handle error
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_task) {
            editTask();
            return true;
        } else if (item.getItemId() == R.id.action_delete_task) {
            deleteTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editTask() {
        Intent intent = new Intent(this, TaskCreationActivity.class);
        intent.putExtra("edit_task", task);
        intent.putExtra("task_id", taskId);
        startActivityForResult(intent, 1);
    }

    private void deleteTask() {
        firestoreHelper.deleteTask(taskId, new FirestoreHelper.FirestoreCallback() {
            @Override
            public void onSuccess(String documentId) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                // Handle error
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            TaskItem updatedTask = data.getParcelableExtra("task_item");
            if (updatedTask != null) {
                this.task = updatedTask;
                populateTaskData();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("task_item", updatedTask);
                resultIntent.putExtra("task_id", taskId);
                resultIntent.putExtra("task_position", getIntent().getIntExtra("task_position", -1));
                setResult(RESULT_OK, resultIntent);
            }
        }
    }
}