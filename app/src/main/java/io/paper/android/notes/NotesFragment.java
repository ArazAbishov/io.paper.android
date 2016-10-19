package io.paper.android.notes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.paper.android.PaperApp;
import io.paper.android.R;
import io.paper.android.editnote.EditNoteActivity;
import io.paper.android.notes.NotesAdapter.OnNoteClickListener;

public final class NotesFragment extends Fragment implements NotesView {

    @BindView(R.id.toolbar_notes)
    Toolbar toolbar;

    @BindView(R.id.recyclerview_notes)
    RecyclerView recyclerView;

    @Inject
    NotesPresenter notesPresenter;

    @Nullable
    Unbinder unbinder;

    NotesAdapter notesAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // injecting dependencies
        PaperApp.getAppComponent(context).inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        // toolbar
        toolbar.setTitle(getString(R.string.notes));

        // recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        notesAdapter = new NotesAdapter(LayoutInflater.from(getActivity()), new OnNoteClickListener() {
            @Override public void onNoteClick(Note note) {
                onEditNote(note);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notesAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // notify presenter that
        // view is detached now
        notesPresenter.detachView();
    }

    @Override
    public void onResume() {
        super.onResume();

        // attach view back
        notesPresenter.attachView(this);
    }

    @OnClick(R.id.fab_add_note)
    @SuppressWarnings("unused")
    public void onAddNote() {
        notesPresenter.createNote();
    }

    @Override
    public void renderNotes(List<Note> notes) {
        notesAdapter.swap(notes);
    }

    @Override
    public void navigateToEditNoteView(Long noteId) {
        startActivity(EditNoteActivity.newIntent(getActivity(), noteId));
    }

    private void onEditNote(Note note) {
        startActivity(EditNoteActivity.newIntent(getActivity(), note.id()));
    }
}
