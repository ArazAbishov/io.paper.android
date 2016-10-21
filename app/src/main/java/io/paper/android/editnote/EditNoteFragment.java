package io.paper.android.editnote;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import io.paper.android.PaperApp;
import io.paper.android.R;
import io.paper.android.notes.Note;
import rx.subscriptions.CompositeSubscription;

public class EditNoteFragment extends Fragment implements EditNoteView {
    private static final String ARG_NOTE_ID = "arg:noteId";

    @BindView(R.id.toolbar_add_note)
    Toolbar toolbar;

    @BindView(R.id.edittext_note_title)
    EditText editTextTitle;

    @BindView(R.id.edittext_note_description)
    EditText editTextDescription;

    @Nullable
    Unbinder unbinder;

    @Nullable
    CompositeSubscription subscriptions;

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

        // note id
        Long noteId = getArguments().getLong(ARG_NOTE_ID);

        // inject dependencies
        PaperApp.getEditNoteComponent(context, noteId).inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        subscriptions = new CompositeSubscription();
        unbinder = ButterKnife.bind(this, view);

        // toolbar configuration
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (isAdded() && getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void showNote(@NonNull Note note) {
        editTextTitle.setText(note.title());
        editTextDescription.setText(note.description());
    }

    @OnTextChanged(value = {
            R.id.edittext_note_title
    }, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTitleChanged(CharSequence title) {
        editNotePresenter.updateTitle(title.toString());
    }

    @OnTextChanged(value = {
            R.id.edittext_note_description
    }, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onDescriptionChanged(CharSequence description) {
        editNotePresenter.updateDescription(description.toString());
    }
}
