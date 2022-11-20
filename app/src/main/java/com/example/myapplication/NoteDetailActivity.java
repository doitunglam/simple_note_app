package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;


public class NoteDetailActivity extends AppCompatActivity {

    TextInputEditText noteDetailTitle;
    TextInputEditText noteDetailCreateDate;
    EditText noteDetailContent;
    int flag;
    Note note = new Note();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        noteDetailTitle = findViewById(R.id.noteDetailTitle);
        noteDetailCreateDate = findViewById(R.id.noteDetailCreateDate);
        noteDetailContent = findViewById(R.id.noteDetailContent);

        Intent it = getIntent();

        flag = it.getIntExtra("flag", 0);
        if (flag == 1) {
            note = it.getParcelableExtra("note", Note.class);
            noteDetailTitle.setText(note.getTitle());
            noteDetailCreateDate.setText(note.getCreateDate());
            noteDetailContent.setText(note.getContent());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent it = new Intent();
        it.putExtra("flag", -1);
        setResult(Activity.RESULT_OK, it);
        finish();
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_detail_mnu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.editSave) {
            String title = (noteDetailTitle.getText()).toString();
            String createDate = (noteDetailCreateDate.getText()).toString();
            String content = (noteDetailContent.getText()).toString();
            note.setTitle(title);
            note.setContent(content);
            note.setCreateDate(createDate);

            Intent it = new Intent();
            it.putExtra("note", note);
            it.putExtra("flag", flag);

            setResult(Activity.RESULT_OK, it);
            finish();
        }
        if (item.getItemId() == R.id.editDelete){
            Intent it = new Intent();
            it.putExtra("flag", -2);

            setResult(Activity.RESULT_OK, it);
            finish();
        }
            return super.onOptionsItemSelected(item);
    }
}
