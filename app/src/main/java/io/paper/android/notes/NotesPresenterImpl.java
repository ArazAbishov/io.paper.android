package io.paper.android.notes;

import android.support.annotation.NonNull;

import java.util.List;

import io.paper.android.data.stores.Query;
import io.paper.android.data.stores.Store;
import io.paper.android.ui.View;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class NotesPresenterImpl implements NotesPresenter {
    private final Store<Note> noteStore;
    private Subscription subscriptions;
    private NotesView notesView;

    public NotesPresenterImpl(Store<Note> noteStore) {
        this.noteStore = noteStore;
        this.subscriptions = Subscriptions.empty();
    }

    @Override
    public void listNotes() {
        subscriptions = noteStore.query(Query.builder()
                .notifyForDescendents(true).build())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Note>>() {
                    @Override
                    public void call(List<Note> notes) {
                        if (notesView != null) {
                            notesView.renderNotes(notes);
                        }
                    }
                });
    }

    @Override
    public void createNote() {
        Note note = Note.builder().title("").description("").build();
        subscriptions = noteStore.insert(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long noteId) {
                        if (notesView != null) {
                            notesView.navigateToEditNoteView(noteId);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void attachView(@NonNull View view) {
        if (view instanceof NotesView) {
            notesView = (NotesView) view;

            // list notes
            listNotes();
        }
    }

    @Override
    public void detachView() {
        // un-subscribing in order not to leak memory
        if (!subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
            subscriptions = Subscriptions.empty();
        }
    }
}
