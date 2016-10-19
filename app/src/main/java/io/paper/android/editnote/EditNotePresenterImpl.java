package io.paper.android.editnote;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.paper.android.data.stores.Query;
import io.paper.android.data.stores.Store;
import io.paper.android.notes.Note;
import io.paper.android.notes.NotesContract;
import io.paper.android.ui.View;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

class EditNotePresenterImpl implements EditNotePresenter {

    @NonNull
    private final Long noteId;

    @NonNull
    private final Store<Note> noteStore;

    @NonNull
    private final PublishSubject<String> noteTitleSubject;

    @NonNull
    private final PublishSubject<String> noteDescriptionSubject;

    @Nullable
    private EditNoteView editNoteView;

    @NonNull
    private CompositeSubscription subscription;

    EditNotePresenterImpl(@NonNull Long noteId, @NonNull Store<Note> noteStore) {
        this.noteId = noteId;
        this.noteStore = noteStore;
        this.noteTitleSubject = PublishSubject.create();
        this.noteDescriptionSubject = PublishSubject.create();
        this.subscription = new CompositeSubscription();
    }

    @Override
    public void updateTitle(@NonNull final String title) {
        noteTitleSubject.onNext(title);
    }

    @Override
    public void updateDescription(@NonNull final String description) {
        noteDescriptionSubject.onNext(description);
    }

    @Override
    public void attachView(@NonNull View view) {
        if (view instanceof EditNoteView) {
            editNoteView = (EditNoteView) view;

            // render note
            subscription.add(noteStore.query(Query.builder()
                    .id(noteId).notifyForDescendents(false).build())
                    .flatMap(new Func1<List<Note>, Observable<Note>>() {
                        @Override public Observable<Note> call(List<Note> notes) {
                            return Observable.from(notes);
                        }
                    })
                    .take(1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Note>() {
                        @Override public void call(Note note) {
                            if (editNoteView != null) {
                                editNoteView.renderNote(note);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }));

            subscription.add(noteTitleSubject
                    .debounce(256, TimeUnit.MILLISECONDS)
                    .switchMap(new Func1<String, Observable<Integer>>() {
                        @Override public Observable<Integer> call(String title) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(NotesContract.COLUMN_TITLE, title);
                            return noteStore.update(noteId, contentValues);
                        }
                    })
                    .subscribe(new Action1<Integer>() {
                        @Override public void call(Integer integer) {
                            System.out.println(integer);
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }));

            subscription.add(noteDescriptionSubject
                    .debounce(256, TimeUnit.MILLISECONDS)
                    .switchMap(new Func1<String, Observable<Integer>>() {
                        @Override public Observable<Integer> call(String description) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(NotesContract.COLUMN_DESCRIPTION, description);
                            return noteStore.update(noteId, contentValues);
                        }
                    })
                    .subscribe(new Action1<Integer>() {
                        @Override public void call(Integer integer) {
                            System.out.println(integer);
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }));

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
}
