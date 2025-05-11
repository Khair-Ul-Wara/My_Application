package com.example.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import android.util.Log;


public class TaskAdapter extends ArrayAdapter<TaskItem> {
    private Context context;
    private List<TaskItem> taskList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault());
    private FirestoreHelper firestoreHelper;

    public TaskAdapter(Context context, List<TaskItem> taskList, FirestoreHelper firestoreHelper) {
        super(context, R.layout.item_task, taskList);
        this.context = context;
        this.taskList = taskList;
        this.firestoreHelper = firestoreHelper;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_task, parent, false);
        }

        TaskItem task = taskList.get(position);
        CheckBox taskCheckBox = convertView.findViewById(R.id.taskCheckBox);
        TextView taskText = convertView.findViewById(R.id.taskText);
        TextView taskDescription = convertView.findViewById(R.id.taskDescription);
        TextView taskDueDate = convertView.findViewById(R.id.taskDueDate);

        taskText.setText(task.getTaskText());
        taskDescription.setText(task.getDescription());
        taskCheckBox.setChecked(task.isDone());

        if (task.getDueDate() != null) {
            taskDueDate.setText(dateFormat.format(task.getDueDate()));
            taskDueDate.setVisibility(View.VISIBLE);
        } else {
            taskDueDate.setVisibility(View.GONE);
        }

        if (task.isDone()) {
            taskText.setPaintFlags(taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskText.setAlpha(0.5f);
            taskDescription.setAlpha(0.5f);
        } else {
            taskText.setPaintFlags(taskText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            taskText.setAlpha(1.0f);
            taskDescription.setAlpha(1.0f);
        }

        taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
            // Update in Firestore
            String docId = ((NotesActivity) context).getTaskDocId(position);
            if (docId != null) {
                firestoreHelper.updateTask(docId, task, new FirestoreHelper.FirestoreCallback() {
                    @Override
                    public void onSuccess(String documentId) {
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("TaskAdapter", "Error updating task status", e);
                        // Revert UI change if update fails
                        taskCheckBox.setChecked(!isChecked);
                    }
                });
            }
        });

        return convertView;
    }

    public void updateTasks(List<TaskItem> newTasks) {
        taskList.clear();
        taskList.addAll(newTasks);
        notifyDataSetChanged();
    }
}