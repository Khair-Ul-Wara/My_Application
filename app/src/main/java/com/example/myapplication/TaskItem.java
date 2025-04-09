package com.example.myapplication;

public class TaskItem {
    private String taskText;
    private boolean isDone;

    public TaskItem(String taskText, boolean isDone) {
        this.taskText = taskText;
        this.isDone = isDone;
    }

    public String getTaskText() {
        return taskText;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
