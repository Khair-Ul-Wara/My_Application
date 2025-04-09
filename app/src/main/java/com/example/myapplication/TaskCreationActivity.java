package com.example.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class TaskCreationActivity extends AppCompatActivity {
    private EditText taskInput;
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<TaskItem> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Task List");

        taskInput = findViewById(R.id.taskInput);
        taskRecyclerView = findViewById(R.id.taskRecyclerView);

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);

        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        // Add swipe to delete functionality
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                TaskItem deletedTask = taskList.get(position);
                taskList.remove(position);
                taskAdapter.notifyItemRemoved(position);

                Snackbar.make(taskRecyclerView, "Task deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", v -> {
                            taskList.add(position, deletedTask);
                            taskAdapter.notifyItemInserted(position);
                        }).show();
            }
        }).attachToRecyclerView(taskRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_creation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_task) {
            addTask();
            return true;
        } else if (item.getItemId() == R.id.action_save_tasks) {
            saveTasks();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addTask() {
        String taskText = taskInput.getText().toString().trim();
        if (!taskText.isEmpty()) {
            taskList.add(new TaskItem(taskText, false));
            taskAdapter.notifyItemInserted(taskList.size() - 1);
            taskInput.setText("");
        }
    }

    private void saveTasks() {
        if (taskList.isEmpty()) {
            Snackbar.make(taskRecyclerView, "Add at least one task", Snackbar.LENGTH_SHORT).show();
        } else {
            // Save logic here
            Toast.makeText(this, "Tasks saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}