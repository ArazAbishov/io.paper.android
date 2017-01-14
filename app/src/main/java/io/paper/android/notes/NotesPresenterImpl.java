package io.paper.android.notes;

import android.support.annotation.NonNull;

import io.paper.android.ui.View;
import io.paper.android.utils.SchedulerProvider;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class NotesPresenterImpl implements NotesPresenter {
    private final SchedulerProvider schedulerProvider;
    private final NotesRepository notesRepository;
    private CompositeSubscription subscriptions;
    private NotesView notesView;

    public NotesPresenterImpl(SchedulerProvider schedulerProvider,
            NotesRepository notesRepository) {
        this.schedulerProvider = schedulerProvider;
        this.notesRepository = notesRepository;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void createNote() {
        subscriptions.add(notesRepository.add("", "")
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe((noteId) -> {
                            if (notesView != null) {
                                notesView.navigateToEditNoteView(noteId);
                            }
                        }, Timber::e
                ));
    }

    @Override
    public void attachView(@NonNull View view) {
        if (view instanceof NotesView) {
            notesView = (NotesView) view;

            // list notes
            subscriptions.add(notesRepository.list()
                    .observeOn(schedulerProvider.ui())
                    .subscribe((notes) -> {
                        if (notesView != null) {
                            notesView.showNotes(notes);
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
