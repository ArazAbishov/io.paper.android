package io.paper.android.notes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.paper.android.data.stores.Query;
import io.paper.android.data.stores.Store;
import io.paper.android.utils.ImmediateSchedulerProvider;
import io.paper.android.utils.SchedulerProvider;
import rx.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotesPresenterTest {

    @Mock
    Store<Note> noteStore;

    @Mock
    NotesView notesView;

    NotesPresenter notesPresenter;

    SchedulerProvider schedulerProvider;

    List<Note> notes = Arrays.asList(
            Note.builder().id(1L).title("TitleOne").description("DescriptionOne").build(),
            Note.builder().id(1L).title("TitleTwo").description("DescriptionTwo").build()
    );

    @Before
    public void setupNotesPresenter() {
        MockitoAnnotations.initMocks(this);

        schedulerProvider = new ImmediateSchedulerProvider();
        notesPresenter = new NotesPresenterImpl(schedulerProvider, noteStore);
    }

    @Test
    public void attachView_ShouldCallShowNotesOnView() {
        // make sure that store returns stub list of notes
        when(noteStore.query(any(Query.class))).thenReturn(Observable.just(notes));

        // we need first to attach view to presenter
        notesPresenter.attachView(notesView);

        // method should be called with list of given notes
        verify(notesView).showNotes(notes);
    }
}
