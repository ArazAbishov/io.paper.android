package io.paper.android.editnote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.paper.android.notes.NotesRepository;
import io.paper.android.ui.View;
import io.paper.android.utils.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

class EditNotePresenterImpl implements EditNotePresenter {

    @NonNull
    private final Long noteId;

    @NonNull
    private final SchedulerProvider schedulerProvider;

    @NonNull
    private final NotesRepository notesRepository;

    @NonNull
    private final PublishSubject<String> noteTitleSubject;

    @NonNull
    private final PublishSubject<String> noteDescriptionSubject;

    @NonNull
    private final CompositeDisposable disposable;

    @Nullable
    private EditNoteView editNoteView;

    EditNotePresenterImpl(@NonNull Long noteId,
            @NonNull SchedulerProvider schedulerProvider,
            @NonNull NotesRepository notesRepository) {
        this.noteId = noteId;
        this.schedulerProvider = schedulerProvider;
        this.notesRepository = notesRepository;
        this.noteTitleSubject = PublishSubject.create();
        this.noteDescriptionSubject = PublishSubject.create();
        this.disposable = new CompositeDisposable();
    }

    @Override
    public void updateTitle(@NonNull String title) {
        noteTitleSubject.onNext(title);
    }

    @Override
    public void updateDescription(@NonNull String description) {
        noteDescriptionSubject.onNext(description);
    }

    @Override
    public void attachView(@NonNull View view) {
        if (view instanceof EditNoteView) {
            editNoteView = (EditNoteView) view;

            // render note
            disposable.add(notesRepository.get(noteId)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe((note) -> {
                        if (editNoteView != null) {
                            editNoteView.showNote(note);
                        }
                    }, Timber::e));

            observeNoteTitleChanges();
            observeNoteDescriptionChanges();
        }
    }

    @Override
    public void detachView() {
        editNoteView = null;
        disposable.clear();
    }

    private void observeNoteTitleChanges() {
        disposable.add(noteTitleSubject
                .debounce(256, TimeUnit.MILLISECONDS)
                .switchMap((title) -> notesRepository.putTitle(noteId, title))
                .subscribe((updated) -> Timber.i("%d notes were updated", updated), Timber::e)
        );
    }

    private void observeNoteDescriptionChanges() {
        disposable.add(noteDescriptionSubject
                .debounce(256, TimeUnit.MILLISECONDS)
                .switchMap((description) -> notesRepository.putDescription(noteId, description))
                .subscribe((updated) -> Timber.i("%d notes were updated", updated), Timber::e));
    }
}
