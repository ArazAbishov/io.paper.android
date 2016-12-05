package io.paper.android.editnote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.paper.android.R;

public class EditNoteActivity extends AppCompatActivity {
    public static final String ARG_NOTE_ID = "arg:noteId";

    public static Intent newIntent(Activity activity, Long noteId) {
        Intent intent = new Intent(activity, EditNoteActivity.class);
        intent.putExtra(ARG_NOTE_ID, noteId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        // replacing content with fragment only on first start of activity,
        // in order not to lose state of NotesFragment afterwards
        if (savedInstanceState == null) {
            Long noteId = getIntent().getExtras().getLong(ARG_NOTE_ID);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_content, EditNoteFragment.newInstance(noteId))
                    .commit();
        }
    }
}
