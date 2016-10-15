package io.paper.android.notes;

import android.support.annotation.NonNull;

import java.util.List;

import io.paper.android.data.stores.Store;
import io.paper.android.ui.View;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class NotesPresenterImpl implements NotesPresenter {
    private final Store<Note> noteStore;
    private Subscription subscriptions;
    private NotesView notesView;

    public NotesPresenterImpl(Store<Note> noteStore) {
        this.noteStore = noteStore;
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
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
    }
}
