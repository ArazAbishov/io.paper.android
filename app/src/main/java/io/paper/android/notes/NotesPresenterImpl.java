package io.paper.android.notes;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import io.paper.android.ui.View;
import io.paper.android.utils.SchedulerProvider;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class NotesPresenterImpl implements NotesPresenter {
    private static final String TAG = NotesPresenterImpl.class.getSimpleName();
    private final SchedulerProvider schedulerProvider;
    private final NotesRepository notesRepository;
    private CompositeSubscription subscriptions;
    private NotesView notesView;

    public NotesPresenterImpl(SchedulerProvider schedulerProvider, NotesRepository notesRepository) {
        this.schedulerProvider = schedulerProvider;
        this.notesRepository = notesRepository;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void createNote() {
        subscriptions.add(notesRepository.add("", "")
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long noteId) {
                        if (notesView != null) {
                            notesView.navigateToEditNoteView(noteId);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage(), throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull View view) {
        if (view instanceof NotesView) {
            notesView = (NotesView) view;

            // list notes
            subscriptions.add(notesRepository.list()
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
