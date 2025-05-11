package com.example.myapplication;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreHelper {
    private FirebaseFirestore db;
    private String userId;

    // Collections
    private static final String USERS_COLLECTION = "users";
    private static final String NOTES_SUBCOLLECTION = "notes";
    private static final String TASKS_SUBCOLLECTION = "tasks";

    // Callback interfaces
    public interface FirestoreCallback {
        void onSuccess(String documentId);
        void onFailure(Exception e);
    }

    public interface FirestoreListCallback {
        void onSuccess(List<DocumentSnapshot> documents);
        void onFailure(Exception e);
    }

    // Constructor
    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            throw new IllegalStateException("User not logged in!");
        }
        userId = user.getUid();
    }

    // ==================== NOTES CRUD ====================
    public void addNote(String content, FirestoreCallback callback) {
        Map<String, Object> note = new HashMap<>();
        note.put("content", content);
        note.put("userId", userId);
        note.put("timestamp", FieldValue.serverTimestamp());

        db.collection(USERS_COLLECTION)
                .document(userId)
                .collection(NOTES_SUBCOLLECTION)
                .add(note)
                .addOnSuccessListener(docRef -> callback.onSuccess(docRef.getId()))
                .addOnFailureListener(callback::onFailure);
    }

    public void getAllNotes(FirestoreListCallback callback) {
        db.collection(USERS_COLLECTION)
                .document(userId)
                .collection(NOTES_SUBCOLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(task.getResult().getDocuments());
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void updateNote(String docId, String content, FirestoreCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("content", content);
        updates.put("timestamp", FieldValue.serverTimestamp());

        db.collection(USERS_COLLECTION)
                .document(userId)
                .collection(NOTES_SUBCOLLECTION)
                .document(docId)
                .update(updates)
                .addOnSuccessListener(aVoid -> callback.onSuccess(docId))
                .addOnFailureListener(callback::onFailure);
    }

    public void deleteNote(String docId, FirestoreCallback callback) {
        db.collection(USERS_COLLECTION)
                .document(userId)
                .collection(NOTES_SUBCOLLECTION)
                .document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess(docId))
                .addOnFailureListener(callback::onFailure);
    }

    // ==================== TASKS CRUD ====================
    public void addTask(TaskItem task, FirestoreCallback callback) {
        Map<String, Object> taskData = new HashMap<>();
        taskData.put("taskText", task.getTaskText());
        taskData.put("description", task.getDescription());
        taskData.put("isDone", task.isDone());
        taskData.put("dueDate", task.getDueDate());
        taskData.put("hasReminder", task.hasReminder());
        taskData.put("userId", userId);
        taskData.put("timestamp", FieldValue.serverTimestamp());

        db.collection(USERS_COLLECTION)
                .document(userId)
                .collection(TASKS_SUBCOLLECTION)
                .add(taskData)
                .addOnSuccessListener(docRef -> callback.onSuccess(docRef.getId()))
                .addOnFailureListener(callback::onFailure);
    }

    public void getAllTasks(FirestoreListCallback callback) {
        db.collection(USERS_COLLECTION)
                .document(userId)
                .collection(TASKS_SUBCOLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(task.getResult().getDocuments());
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void updateTask(String docId, TaskItem task, FirestoreCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("taskText", task.getTaskText());
        updates.put("description", task.getDescription());
        updates.put("isDone", task.isDone());
        updates.put("dueDate", task.getDueDate());
        updates.put("hasReminder", task.hasReminder());
        updates.put("timestamp", FieldValue.serverTimestamp());

        db.collection(USERS_COLLECTION)
                .document(userId)
                .collection(TASKS_SUBCOLLECTION)
                .document(docId)
                .update(updates)
                .addOnSuccessListener(aVoid -> callback.onSuccess(docId))
                .addOnFailureListener(callback::onFailure);
    }

    public void deleteTask(String docId, FirestoreCallback callback) {
        db.collection(USERS_COLLECTION)
                .document(userId)
                .collection(TASKS_SUBCOLLECTION)
                .document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess(docId))
                .addOnFailureListener(callback::onFailure);
    }
}