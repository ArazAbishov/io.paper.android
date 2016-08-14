package io.paper.android.views;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.paper.android.PaperApp;
import io.paper.android.R;
import io.paper.android.models.DbSchemas;
import io.paper.android.models.Note;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public final class NotesFragment extends Fragment {

    @BindView(R.id.toolbar_notes)
    Toolbar toolbar;

    @BindView(R.id.recyclerview_notes)
    RecyclerView recyclerView;

    @Inject
    BriteDatabase paperDatabase;

    @Nullable
    Unbinder unbinder;

    NotesAdapter notesAdapter;
    Subscription subscriptions;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // injecting dependencies
        PaperApp.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        setupToolbar();
        setupRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // making a new database query
        subscriptions = paperDatabase.createQuery(DbSchemas.Notes.TABLE_NAME, DbSchemas.Notes.QUERY)
                .mapToList(new Func1<Cursor, Note>() {
                    @Override
                    public Note call(Cursor cursor) {
                        return Note.mapNote(cursor);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Note>>() {
                    @Override
                    public void call(List<Note> notes) {
                        notesAdapter.swapData(notes);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();

        // un-subscribing in order not to leak memory
        subscriptions.unsubscribe();
    }

    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.notes));
        toolbar.inflateMenu(R.menu.menu_notes);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showAddNoteDialog();
                return true;
            }
        });
    }

    private void setupRecyclerView() {
        notesAdapter = new NotesAdapter(LayoutInflater.from(getActivity()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notesAdapter);
    }

    private void showAddNoteDialog() {
        AddNoteFragment.newInstance()
                .show(getChildFragmentManager(), NotesFragment.class.getSimpleName());
    }
}
