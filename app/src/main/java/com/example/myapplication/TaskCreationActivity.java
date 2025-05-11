
//code#02:
package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;
import java.util.Date;

public class TaskCreationActivity extends AppCompatActivity {
    private EditText taskTitleInput, taskDescriptionInput;
    private TextInputLayout dueDateLayout;
    private CheckBox reminderCheckbox;
    private Date dueDate;
    private boolean isEditMode = false;
    private TaskItem editingTask;
    private String editingTaskId;
    private FirestoreHelper firestoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        // Initialize ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        firestoreHelper = new FirestoreHelper();
        initializeViews();

        // Check if we're editing an existing task
        if (getIntent().hasExtra("edit_task")) {
            isEditMode = true;
            editingTask = getIntent().getParcelableExtra("edit_task");
            editingTaskId = getIntent().getStringExtra("task_id");
            populateTaskData();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Task");
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Create Task");
            }
        }
    }

    private void initializeViews() {
        taskTitleInput = findViewById(R.id.taskTitleInput);
        taskDescriptionInput = findViewById(R.id.taskDescriptionInput);
        dueDateLayout = findViewById(R.id.dueDateLayout);
        reminderCheckbox = findViewById(R.id.reminderCheckbox);

        dueDateLayout.setEndIconOnClickListener(v -> showDateTimePicker());
    }

    private void populateTaskData() {
        if (editingTask != null) {
            taskTitleInput.setText(editingTask.getTaskText());
            taskDescriptionInput.setText(editingTask.getDescription());
            dueDate = editingTask.getDueDate();
            updateDueDateText();
            reminderCheckbox.setChecked(editingTask.hasReminder());
        }
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        if (dueDate != null) {
            calendar.setTime(dueDate);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            (view1, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                dueDate = calendar.getTime();
                                updateDueDateText();
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                    );
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDueDateText() {
        if (dueDate != null) {
            dueDateLayout.getEditText().setText(android.text.format.DateFormat
                    .getDateFormat(this).format(dueDate) + " " +
                    android.text.format.DateFormat.getTimeFormat(this).format(dueDate));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_creation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_tasks) {
            saveTask();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            // Handle back button press
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTask() {
        String title = taskTitleInput.getText().toString().trim();
        String description = taskDescriptionInput.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Task title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        TaskItem task = new TaskItem(
                title,
                description,
                false, // Default to not done
                dueDate,
                reminderCheckbox.isChecked()
        );

        if (isEditMode) {
            firestoreHelper.updateTask(editingTaskId, task, new FirestoreHelper.FirestoreCallback() {
                @Override
                public void onSuccess(String documentId) {
                    // Schedule notification if reminder is enabled
                    if (task.hasReminder() && task.getDueDate() != null) {
                        NotificationHelper.scheduleTaskNotification(
                                TaskCreationActivity.this,
                                editingTaskId,
                                task.getTaskText(),
                                task.getDueDate()
                        );
                    }
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(TaskCreationActivity.this, "Error updating task", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            firestoreHelper.addTask(task, new FirestoreHelper.FirestoreCallback() {
                @Override
                public void onSuccess(String documentId) {
                    // Schedule notification if reminder is enabled
                    if (task.hasReminder() && task.getDueDate() != null) {
                        NotificationHelper.scheduleTaskNotification(
                                TaskCreationActivity.this,
                                documentId,
                                task.getTaskText(),
                                task.getDueDate()
                        );
                    }
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(TaskCreationActivity.this, "Error creating task", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
////new:
//package com.example.myapplication;
//
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.material.textfield.TextInputLayout;
//import java.util.Calendar;
//import java.util.Date;
//import android.content.Intent;
//
//
//public class TaskCreationActivity extends AppCompatActivity {
//    private EditText taskTitleInput, taskDescriptionInput;
//    private TextInputLayout dueDateLayout;
//    private CheckBox reminderCheckbox;
//    private Date dueDate;
//    private boolean isEditMode = false;
//    private TaskItem editingTask;
//    private String editingTaskId;
//    private FirestoreHelper firestoreHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task_creation);
//
//        // Initialize ActionBar
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
//
//        firestoreHelper = new FirestoreHelper();
//        initializeViews();
//
//        // Check if we're editing an existing task
//        if (getIntent().hasExtra("edit_task")) {
//            isEditMode = true;
//            editingTask = getIntent().getParcelableExtra("edit_task");
//            editingTaskId = getIntent().getStringExtra("task_id");
//            populateTaskData();
//            if (getSupportActionBar() != null) {
//                getSupportActionBar().setTitle("Edit Task");
//            }
//        } else {
//            if (getSupportActionBar() != null) {
//                getSupportActionBar().setTitle("Create Task");
//            }
//        }
//    }
//
//
//    private void initializeViews() {
//        taskTitleInput = findViewById(R.id.taskTitleInput);
//        taskDescriptionInput = findViewById(R.id.taskDescriptionInput);
//        dueDateLayout = findViewById(R.id.dueDateLayout);
//        reminderCheckbox = findViewById(R.id.reminderCheckbox);
//
//        dueDateLayout.setEndIconOnClickListener(v -> showDateTimePicker());
//    }
//
//    private void populateTaskData() {
//        if (editingTask != null) {
//            taskTitleInput.setText(editingTask.getTaskText());
//            taskDescriptionInput.setText(editingTask.getDescription());
//            dueDate = editingTask.getDueDate();
//            updateDueDateText();
//            reminderCheckbox.setChecked(editingTask.hasReminder());
//        }
//    }
//
//    private void showDateTimePicker() {
//        final Calendar calendar = Calendar.getInstance();
//        if (dueDate != null) {
//            calendar.setTime(dueDate);
//        }
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//                this,
//                (view, year, month, dayOfMonth) -> {
//                    calendar.set(year, month, dayOfMonth);
//                    TimePickerDialog timePickerDialog = new TimePickerDialog(
//                            this,
//                            (view1, hourOfDay, minute) -> {
//                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                                calendar.set(Calendar.MINUTE, minute);
//                                dueDate = calendar.getTime();
//                                updateDueDateText();
//                            },
//                            calendar.get(Calendar.HOUR_OF_DAY),
//                            calendar.get(Calendar.MINUTE),
//                            false
//                    );
//                    timePickerDialog.show();
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//        );
//        datePickerDialog.show();
//    }
//
//    private void updateDueDateText() {
//        if (dueDate != null) {
//            dueDateLayout.getEditText().setText(android.text.format.DateFormat
//                    .getDateFormat(this).format(dueDate) + " " +
//                    android.text.format.DateFormat.getTimeFormat(this).format(dueDate));
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.task_creation_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_save_tasks) {
//            saveTask();
//            return true;
//        } else if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void saveTask() {
//        String title = taskTitleInput.getText().toString().trim();
//        String description = taskDescriptionInput.getText().toString().trim();
//
//        if (title.isEmpty()) {
//            Toast.makeText(this, "Task title cannot be empty", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        TaskItem task = new TaskItem(
//                title,
//                description,
//                false,
//                dueDate,
//                reminderCheckbox.isChecked()
//        );
//
//        if (isEditMode) {
//            firestoreHelper.updateTask(editingTaskId, task, new FirestoreHelper.FirestoreCallback() {
//                @Override
//                public void onSuccess(String documentId) {
//                    if (task.hasReminder() && task.getDueDate() != null) {
//                        NotificationHelper.scheduleTaskNotification(
//                                TaskCreationActivity.this,
//                                editingTaskId,
//                                task.getTaskText(),
//                                task.getDueDate()
//                        );
//                    }
//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra("task_item", task);
//                    resultIntent.putExtra("task_id", editingTaskId);
//                    setResult(RESULT_OK, resultIntent);
//                    finish();
//                }
//
//                @Override
//                public void onFailure(Exception e) {
//                    Toast.makeText(TaskCreationActivity.this, "Error updating task", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            firestoreHelper.addTask(task, new FirestoreHelper.FirestoreCallback() {
//                @Override
//                public void onSuccess(String documentId) {
//                    if (task.hasReminder() && task.getDueDate() != null) {
//                        NotificationHelper.scheduleTaskNotification(
//                                TaskCreationActivity.this,
//                                documentId,
//                                task.getTaskText(),
//                                task.getDueDate()
//                        );
//                    }
//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra("task_item", task);
//                    resultIntent.putExtra("task_id", documentId);
//                    setResult(RESULT_OK, resultIntent);
//                    finish();
//                }
//
//                @Override
//                public void onFailure(Exception e) {
//                    Toast.makeText(TaskCreationActivity.this, "Error creating task", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//}