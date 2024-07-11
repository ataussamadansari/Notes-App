package com.example.notesapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditNoteFragment extends Fragment {
    private static final String ARG_NOTE_ID = "note_id";
    private int noteId;
    private EditText editTextTitle, editTextContent;

    public static EditNoteFragment newInstance(int noteId) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteId = getArguments().getInt(ARG_NOTE_ID);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        editTextTitle = view.findViewById(R.id.edit_text_title);
        editTextContent = view.findViewById(R.id.edit_text_content);

        loadNote();

        view.findViewById(R.id.button_save).setOnClickListener(v -> saveNote());

        return view;
    }

    private void loadNote() {
        Note note = DatabaseHelper.getInstance(getActivity()).getNoteById(noteId);
        if (note != null) {
            editTextTitle.setText(note.getTitle());
            editTextContent.setText(note.getContent());
        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(getActivity(), "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note();
        note.setId(noteId);
        note.setTitle(title);
        note.setContent(content);

        DatabaseHelper.getInstance(getActivity()).updateNote(note);
        getActivity().getSupportFragmentManager().popBackStack();
    }
}