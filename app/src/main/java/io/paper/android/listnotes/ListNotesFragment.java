package io.paper.android.listnotes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.paper.android.PaperApp;
import io.paper.android.R;
import io.paper.android.commons.views.BaseFragment;
import io.paper.android.editnote.EditNoteActivity;
import io.paper.android.notes.Note;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public final class ListNotesFragment extends BaseFragment implements ListNotesView {

    @BindView(R.id.toolbar_notes)
    Toolbar toolbar;

    @BindView(R.id.fab_add_note)
    FloatingActionButton addNoteButton;

    @BindView(R.id.recyclerview_notes)
    RecyclerView recyclerView;

    @Inject
    ListNotesPresenter listNotesPresenter;

    ListNotesAdapter listNotesAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ((PaperApp) getActivity().getApplicationContext()).notesComponent()
                .plus(new ListNotesModule())
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bind(this, view);

        toolbar.setTitle(getString(R.string.notes));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listNotesAdapter = new ListNotesAdapter(LayoutInflater.from(getActivity()));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listNotesAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        listNotesPresenter.detachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        listNotesPresenter.attachView(this);
    }

    @NonNull
    @Override
    public Flowable<ListNotesAction> notesActions() {
        return listNotesAdapter.asFlowable();
    }

    @NonNull
    @Override
    public Observable<Object> createNoteButtonClicks() {
        return RxView.clicks(addNoteButton);
    }

    @NonNull
    @Override
    public Consumer<List<Note>> showNotes() {
        return notes -> listNotesAdapter.swap(notes);
    }

    @NonNull
    @Override
    public Consumer<Long> navigateToEditNoteView() {
        return this::editNote;
    }

    private void editNote(Long noteId) {
        startActivity(EditNoteActivity.newIntent(getActivity(), noteId));
    }
}
