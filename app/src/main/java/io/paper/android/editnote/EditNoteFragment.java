package io.paper.android.editnote;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paper.android.PaperApp;
import io.paper.android.R;
import io.paper.android.commons.views.BaseFragment;
import io.paper.android.notes.Note;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class EditNoteFragment extends BaseFragment implements EditNoteView {
    private static final String ARG_NOTE_ID = "arg:noteId";

    @BindView(R.id.toolbar_add_note)
    Toolbar toolbar;

    @BindView(R.id.edittext_note_title)
    EditText editTextTitle;

    @BindView(R.id.edittext_note_description)
    EditText editTextDescription;

    @Inject
    EditNotePresenter editNotePresenter;

    public static EditNoteFragment newInstance(@NonNull Long noteId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_NOTE_ID, noteId);

        EditNoteFragment editNoteFragment = new EditNoteFragment();
        editNoteFragment.setArguments(arguments);

        return editNoteFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Long noteId = getArguments().getLong(ARG_NOTE_ID);

        ((PaperApp) getActivity().getApplicationContext()).notesComponent()
                .plus(new EditNoteModule(noteId))
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder(ButterKnife.bind(this, view));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
    }

    @Override
    public void onResume() {
        super.onResume();
        editNotePresenter.attachView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        editNotePresenter.detachView();
    }

    @NonNull
    @Override
    public Observable<Object> toolbarNavigationButtonClicks() {
        return RxToolbar.navigationClicks(toolbar);
    }

    @NonNull
    @Override
    public Observable<String> noteTitleFieldChanges() {
        return RxTextView.textChanges(editTextTitle)
                .map(CharSequence::toString);
    }

    @NonNull
    @Override
    public Observable<String> noteDescriptionFieldChanges() {
        return RxTextView.textChanges(editTextDescription)
                .map(CharSequence::toString);
    }

    @NonNull
    @Override
    public Consumer<Note> showNote() {
        return (note) -> {
            editTextTitle.setText(note.title());
            editTextDescription.setText(note.description());
        };
    }

    @NonNull
    @Override
    public Consumer<Object> navigateUp() {
        return (event) -> {
            if (isAdded() && getActivity() != null) {
                getActivity().onBackPressed();
            }
        };
    }
}
