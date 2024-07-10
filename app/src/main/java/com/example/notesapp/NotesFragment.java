package com.example.notesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class NotesFragment extends Fragment {
    private NotesAdapter adapter;
    private final List<Note> noteList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NotesAdapter(noteList);
        recyclerView.setAdapter(adapter);

        // Load notes from database
        loadNotes();

        FloatingActionButton fab = view.findViewById(R.id.fab_add_note);
        fab.setOnClickListener(v -> {
            // Open AddNoteFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddNoteFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loadNotes() {
        // Load notes from SQLite database
        noteList.clear();
        noteList.addAll(DatabaseHelper.getInstance(getActivity()).getAllNotes());
        adapter.notifyDataSetChanged();
    }
}
