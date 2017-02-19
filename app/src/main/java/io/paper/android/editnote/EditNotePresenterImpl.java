package io.paper.android.editnote;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.paper.android.notes.NotesRepository;
import io.paper.android.ui.View;
import io.paper.android.utils.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class EditNotePresenterImpl implements EditNotePresenter {

    @NonNull
    private final Long noteId;

    @NonNull
    private final SchedulerProvider schedulerProvider;

    @NonNull
    private final NotesRepository notesRepository;

    @NonNull
    private final CompositeDisposable disposable;

    EditNotePresenterImpl(@NonNull Long noteId,
            @NonNull SchedulerProvider schedulerProvider,
            @NonNull NotesRepository notesRepository) {
        this.noteId = noteId;
        this.schedulerProvider = schedulerProvider;
        this.notesRepository = notesRepository;
        this.disposable = new CompositeDisposable();
    }

    @Override
    public void attachView(@NonNull View view) {
        if (view instanceof EditNoteView) {
            EditNoteView editNoteView = (EditNoteView) view;

            disposable.add(editNoteView.toolbar()
                    .subscribeOn(schedulerProvider.ui())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(editNoteView.navigateUp(), Timber::e));

            disposable.add(editNoteView.noteTitle()
                    .debounce(256, TimeUnit.MILLISECONDS, schedulerProvider.computation())
                    .switchMap((title) -> notesRepository.putTitle(noteId, title))
                    .subscribeOn(schedulerProvider.ui())
                    .observeOn(schedulerProvider.io())
                    .subscribe((updated) -> Timber.i("%d notes were updated", updated), Timber::e));

            disposable.add(editNoteView.noteDescription()
                    .debounce(256, TimeUnit.MILLISECONDS, schedulerProvider.computation())
                    .switchMap((description) -> notesRepository.putDescription(noteId, description))
                    .subscribeOn(schedulerProvider.ui())
                    .observeOn(schedulerProvider.io())
                    .subscribe((updated) -> Timber.i("%d notes were updated", updated), Timber::e));

            disposable.add(notesRepository.get(noteId)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(editNoteView.showNote(), Timber::e));
        }
    }

    @Override
    public void detachView() {
        disposable.clear();
    }
}
