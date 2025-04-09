package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class NotesAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final int resource;

    public NotesAdapter(Context context, int resource, ArrayList<String> notesList) {
        super(context, resource, notesList);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView noteText = convertView.findViewById(R.id.noteText);
        ImageView noteIcon = convertView.findViewById(R.id.noteIcon);

        noteText.setText(getItem(position));

        // Set different icons based on note type
        if (position % 3 == 0) {
            noteIcon.setImageResource(R.drawable.ic_note_personal);
        } else if (position % 3 == 1) {
            noteIcon.setImageResource(R.drawable.ic_note_work);
        } else {
            noteIcon.setImageResource(R.drawable.ic_note_idea);
        }

        return convertView;
    }
}