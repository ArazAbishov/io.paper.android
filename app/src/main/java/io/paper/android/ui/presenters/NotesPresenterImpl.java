package io.paper.android.ui.presenters;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite.BriteContentResolver;

import java.util.List;

import io.paper.android.models.Note;
import io.paper.android.stores.NotesContract;
import io.paper.android.stores.NotesMapper;
import io.paper.android.ui.views.NotesView;
import io.paper.android.ui.views.View;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class NotesPresenterImpl implements NotesPresenter {
    private final BriteContentResolver contentResolver;

    private Subscription subscriptions;
    private NotesView notesView;

    public NotesPresenterImpl(BriteContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public void listNotes() {
        subscriptions = contentResolver
                .createQuery(NotesContract.CONTENT_URI, null, null, null, null, true)
                .mapToList(new Func1<Cursor, Note>() {
                    @Override
                    public Note call(Cursor cursor) {
                        return NotesMapper.INSTANCE.toModel(cursor);
                    }
                })
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
