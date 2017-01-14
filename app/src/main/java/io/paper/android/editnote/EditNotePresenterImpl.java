package io.paper.android.editnote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.paper.android.notes.NotesRepository;
import io.paper.android.ui.View;
import io.paper.android.utils.SchedulerProvider;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
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

    @Nullable
    private EditNoteView editNoteView;

    @NonNull
    private CompositeSubscription subscription;

    EditNotePresenterImpl(@NonNull Long noteId,
            @NonNull SchedulerProvider schedulerProvider,
            @NonNull NotesRepository notesRepository) {
        this.noteId = noteId;
        this.schedulerProvider = schedulerProvider;
        this.notesRepository = notesRepository;
        this.noteTitleSubject = PublishSubject.create();
        this.noteDescriptionSubject = PublishSubject.create();
        this.subscription = new CompositeSubscription();
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
            subscription.add(notesRepository.get(noteId)
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

        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription = new CompositeSubscription();
        }
    }

    private void observeNoteTitleChanges() {
        subscription.add(noteTitleSubject
                .debounce(256, TimeUnit.MILLISECONDS)
                .switchMap(new Func1<String, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(String title) {
                        return notesRepository.putTitle(noteId, title);
                    }
                })
                .subscribe((updated) -> Timber.i("%d notes were updated", updated), Timber::e)
        );
    }

    private void observeNoteDescriptionChanges() {
        subscription.add(noteDescriptionSubject
                .debounce(256, TimeUnit.MILLISECONDS)
                .switchMap(new Func1<String, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(String description) {
                        return notesRepository.putDescription(noteId, description);
                    }
                })
                .subscribe((updated) -> Timber.i("%d notes were updated", updated), Timber::e));
    }
}
