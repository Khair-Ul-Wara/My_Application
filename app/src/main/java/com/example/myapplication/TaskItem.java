package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

public class TaskItem implements Parcelable {
    private String taskText;
    private String description;
    private boolean isDone;
    private Date dueDate;
    private boolean hasReminder;

    public TaskItem(String taskText, String description, boolean isDone, Date dueDate, boolean hasReminder) {
        this.taskText = taskText;
        this.description = description;
        this.isDone = isDone;
        this.dueDate = dueDate;
        this.hasReminder = hasReminder;
    }

    protected TaskItem(Parcel in) {
        taskText = in.readString();
        description = in.readString();
        isDone = in.readByte() != 0;
        long tmpDueDate = in.readLong();
        dueDate = tmpDueDate != -1 ? new Date(tmpDueDate) : null;
        hasReminder = in.readByte() != 0;
    }

    public static final Creator<TaskItem> CREATOR = new Creator<TaskItem>() {
        @Override
        public TaskItem createFromParcel(Parcel in) {
            return new TaskItem(in);
        }

        @Override
        public TaskItem[] newArray(int size) {
            return new TaskItem[size];
        }
    };

    // Getters and Setters
    public String getTaskText() { return taskText; }
    public String getDescription() { return description; }
    public boolean isDone() { return isDone; }
    public Date getDueDate() { return dueDate; }
    public boolean hasReminder() { return hasReminder; }

    public void setDone(boolean done) { isDone = done; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public void setHasReminder(boolean hasReminder) { this.hasReminder = hasReminder; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taskText);
        dest.writeString(description);
        dest.writeByte((byte) (isDone ? 1 : 0));
        dest.writeLong(dueDate != null ? dueDate.getTime() : -1);
        dest.writeByte((byte) (hasReminder ? 1 : 0));
    }
}