package io.paper.android.notes;

import android.support.annotation.NonNull;

import io.paper.android.ui.View;
import io.paper.android.utils.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class NotesPresenterImpl implements NotesPresenter {
    private final SchedulerProvider schedulerProvider;
    private final NotesRepository notesRepository;
    private final CompositeDisposable disposable;
    private NotesView notesView;

    public NotesPresenterImpl(SchedulerProvider schedulerProvider,
            NotesRepository notesRepository) {
        this.schedulerProvider = schedulerProvider;
        this.notesRepository = notesRepository;
        this.disposable = new CompositeDisposable();
    }

    @Override
    public void createNote() {
        disposable.add(notesRepository.add("", "")
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
            disposable.add(notesRepository.list()
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
        disposable.clear();
    }
}
