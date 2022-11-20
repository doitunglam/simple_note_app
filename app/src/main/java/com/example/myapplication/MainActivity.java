package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteAdapter.Listener {

    RecyclerView rvNote;
    NoteAdapter noteAdapter;
    ArrayList<Note> notes = new ArrayList<>();
    Menu currentMenu;
    int pos;

    ActivityResultLauncher<Intent> startNoteDetailForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Note note;
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the Intent
                        Intent intent = result.getData();
                        int flag = intent.getIntExtra("flag", -1);
                        System.out.println(flag);
                        switch (flag) {
                            case 1:
                                //System.out.println(pos);
                                note = intent.getParcelableExtra("note",Note.class);
                                noteAdapter.editNote(note,pos);
                                break;
                            case 0:
                                note = intent.getParcelableExtra("note",Note.class);
                                noteAdapter.addNote(note);
                                break;
                            case -1:
                                break;
                            case -2:
                                noteAdapter.deleteNote(pos);
                                break;
                        }

                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Note newNote = new Note();
        newNote.setTitle("New title");
        newNote.setContent("Content");
        newNote.setCreateDate("1/1/1970");

        Note newNote2 = new Note();
        newNote2.setTitle("New title2");
        newNote2.setContent("Content2");
        newNote2.setCreateDate("1/1/1970");

        DBHandler dbHandler = new DBHandler(this);
//        dbHandler.addNote(newNote);
//        dbHandler.addNote(newNote2);

        notes = dbHandler.getAllNotes();


        setContentView(R.layout.activity_main);
        rvNote = findViewById(R.id.rvContacts);
        noteAdapter = new NoteAdapter(MainActivity.this, notes,this);
        rvNote.setAdapter(noteAdapter);
        rvNote.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        rvNote.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_mnu, menu);
        this.currentMenu = menu;
        SearchView searchView = (SearchView) menu.findItem(R.id.mnuSearch).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                noteAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void OnItemListener(int pos, Note note) {
        Intent it = new Intent(MainActivity.this, NoteDetailActivity.class);
        it.putExtra("note", notes.get(pos));
        it.putExtra("flag", 1);
        startNoteDetailForResult.launch(it);
        this.pos = pos;
        //startActivity(it);
    }

    public void onAddButtonClick(View view) {
        Intent it = new Intent(MainActivity.this, NoteDetailActivity.class);
        it.putExtra("flag", 0);
        startNoteDetailForResult.launch(it);

        //startActivity(it);
    }
}