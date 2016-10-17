package io.paper.android.editnote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.paper.android.data.stores.Query;
import io.paper.android.data.stores.Store;
import io.paper.android.notes.Note;
import io.paper.android.ui.View;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

class EditNotePresenterImpl implements EditNotePresenter {

    @NonNull
    private final Long noteId;

    @NonNull
    private final Store<Note> noteStore;

    @Nullable
    private EditNoteView editNoteView;

    @NonNull
    private Subscription subscription;

    public EditNotePresenterImpl(@NonNull Long noteId, @NonNull Store<Note> noteStore) {
        this.noteId = noteId;
        this.noteStore = noteStore;
        this.subscription = Subscriptions.empty();
    }

    @Override
    public void updateTitle(@NonNull final CharSequence title) {
        subscription = noteStore.query(noteId, Query.builder().notifyForDescendents(false).build())
                .switchMap(new Func1<Note, Observable<Integer>>() {
                    @Override public Observable<Integer> call(Note note) {
                        return noteStore.update(note.toBuilder()
                                .title(title.toString())
                                .build());
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
                });
    }

    @Override
    public void updateDescription(@NonNull final CharSequence description) {
        subscription = noteStore.query(noteId, Query.builder().notifyForDescendents(false).build())
                .switchMap(new Func1<Note, Observable<Integer>>() {
                    @Override public Observable<Integer> call(Note note) {
                        return noteStore.update(note.toBuilder()
                                .description(description.toString())
                                .build());
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
                });
    }

    @Override
    public void attachView(@NonNull View view) {
        if (editNoteView != null && view instanceof EditNoteView) {
            editNoteView = (EditNoteView) view;
            subscription = noteStore.query(noteId, Query.builder()
                    .notifyForDescendents(false).build())
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
                    });
        }
    }

    @Override
    public void detachView() {
        editNoteView = null;

        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription = Subscriptions.empty();
        }
    }
}
