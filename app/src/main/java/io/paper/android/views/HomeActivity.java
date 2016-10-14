package io.paper.android.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.paper.android.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // replacing content with fragment only on first start of activity,
        // in order not to lose state of NotesFragment afterwards
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_content, new NotesFragment())
                    .commit();
        }
    }
}
