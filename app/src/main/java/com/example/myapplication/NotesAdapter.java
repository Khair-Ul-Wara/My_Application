//package com.example.myapplication;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import java.util.ArrayList;
//
//public class NotesAdapter extends ArrayAdapter<String> {
//    private Context context;
//    private ArrayList<String> notesList;
//
//    public NotesAdapter(Context context, ArrayList<String> notesList) {
//        super(context, R.layout.note_item, notesList);
//        this.context = context;
//        this.notesList = notesList;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context)
//                    .inflate(R.layout.note_item, parent, false);
//        }
//
//        TextView noteText = convertView.findViewById(R.id.noteText);
//        noteText.setText(notesList.get(position));
//
//        return convertView;
//    }
//
//    // Add this method to update data if needed
//    public void updateNotes(ArrayList<String> newNotes) {
//        notesList.clear();
//        notesList.addAll(newNotes);
//        notifyDataSetChanged();
//    }
//}
package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class NotesAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> notesList;

    public NotesAdapter(Context context, ArrayList<String> notesList) {
        super(context, R.layout.note_item, notesList);
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.note_item, parent, false);
        }

        TextView noteText = convertView.findViewById(R.id.noteText);
        noteText.setText(notesList.get(position));

        return convertView;
    }

    // Add this method to update data if needed
    public void updateNotes(ArrayList<String> newNotes) {
        notesList.clear();
        notesList.addAll(newNotes);
        notifyDataSetChanged();
    }
}
