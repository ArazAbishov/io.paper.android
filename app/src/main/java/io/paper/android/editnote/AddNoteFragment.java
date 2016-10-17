package io.paper.android.editnote;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;

import javax.inject.Inject;

import io.paper.android.PaperApp;
import io.paper.android.R;
import io.paper.android.data.stores.Store;
import io.paper.android.notes.Note;
import io.paper.android.notes.NotesFragment;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static butterknife.ButterKnife.findById;

public class AddNoteFragment extends DialogFragment {

    public static AddNoteFragment newInstance() {
        return new AddNoteFragment();
    }

    @Inject
    Store<Note> noteStore;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        PaperApp.getAppComponent(context).inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final PublishSubject<String> createdClick = PublishSubject.create();

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_add_note, null);

        EditText editText = findById(view, android.R.id.input);
        Observable.combineLatest(createdClick, RxTextView.afterTextChangeEvents(editText),
                new Func2<String, TextViewAfterTextChangeEvent, String>() {
                    @Override
                    public String call(String text, TextViewAfterTextChangeEvent event) {
                        return event.editable().toString();
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String noteText) {
                        Note note = Note.builder()
                                .title("StubTitle")
                                .description(noteText)
                                .build();
                        noteStore.insert(note).toBlocking().first();

                        Log.d(AddNoteFragment.class.getSimpleName(), noteText);
                    }
                });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_note)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createdClick.onNext(AddNoteFragment.class.getSimpleName());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Log.d(NotesFragment.class.getSimpleName(), "onCancel()");
                    }
                })
                .create();
    }
}
