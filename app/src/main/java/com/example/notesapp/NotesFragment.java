package com.example.notesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

        // Attach ItemTouchHelper to RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeHelperCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    // Handle delete action
                    Note note = noteList.get(position);
                    DatabaseHelper.getInstance(getActivity()).deleteNote(note);
                    noteList.remove(position);
                    adapter.notifyItemRemoved(position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Handle edit action
                    Note note = noteList.get(position);
                    EditNoteFragment editNoteFragment = EditNoteFragment.newInstance(note.getId());
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, editNoteFragment)
                            .addToBackStack(null)
                            .commit();
                    adapter.notifyItemChanged(position); // Revert the swipe action
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void loadNotes() {
        // Load notes from SQLite database
        noteList.clear();
        noteList.addAll(DatabaseHelper.getInstance(getActivity()).getAllNotes());
        adapter.notifyDataSetChanged();
    }
}