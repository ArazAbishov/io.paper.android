package io.paper.android.notes;

import android.support.annotation.NonNull;

import java.util.List;

import io.paper.android.data.stores.Query;
import io.paper.android.data.stores.Store;
import io.paper.android.ui.View;
import io.paper.android.utils.SchedulerProvider;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class NotesPresenterImpl implements NotesPresenter {
    private final SchedulerProvider schedulerProvider;
    private final Store<Note> noteStore;
    private CompositeSubscription subscriptions;
    private NotesView notesView;

    public NotesPresenterImpl(SchedulerProvider schedulerProvider, Store<Note> noteStore) {
        this.schedulerProvider = schedulerProvider;
        this.noteStore = noteStore;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void createNote() {
        Note note = Note.builder().title("").description("").build();
        subscriptions.add(noteStore.insert(note)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
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
                }));
    }

    @Override
    public void attachView(@NonNull View view) {
        if (view instanceof NotesView) {
            notesView = (NotesView) view;

            // list notes
            subscriptions.add(noteStore.query(Query.builder()
                    .notifyForDescendents(true).build())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(new Action1<List<Note>>() {
                        @Override
                        public void call(List<Note> notes) {
                            if (notesView != null) {
                                notesView.showNotes(notes);
                            }
                        }
                    }));
        }
    }

    @Override
    public void detachView() {
        notesView = null;

        // un-subscribing in order not to leak memory
        if (!subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
            subscriptions = new CompositeSubscription();
        }
    }
}
