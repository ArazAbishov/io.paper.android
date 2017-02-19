package io.paper.android.notes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.paper.android.utils.ImmediateSchedulerProvider;
import io.paper.android.utils.SchedulerProvider;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class NotesPresenterTests {

    @Mock
    NotesRepository notesRepository;

    @Mock
    NotesView notesView;

    NotesPresenter notesPresenter;

    SchedulerProvider schedulerProvider;

    List<Note> notes = Arrays.asList(
            Note.builder().id(1L).title("TitleOne").description("DescriptionOne").build(),
            Note.builder().id(2L).title("TitleTwo").description("DescriptionTwo").build()
    );

    @Before
    public void setupNotesPresenter() {
        MockitoAnnotations.initMocks(this);

        schedulerProvider = new ImmediateSchedulerProvider();
        notesPresenter = new NotesPresenterImpl(schedulerProvider, notesRepository);
    }

    @Test
    public void attachView_shouldCallShowNotesOnView() {
        // make sure that store returns stub list of notes
        when(notesRepository.list()).thenReturn(Observable.just(notes));

        // we need first to attach view to presenter
        notesPresenter.attachView(notesView);

        // method should be called with list of given notes
        verify(notesView).showNotes(notes);
    }

    @Test
    public void createNote_shouldCallInsertOnStore() {
        // mock store behaviour both for attach view and create note
        when(notesRepository.list()).thenReturn(Observable.just(notes));
        when(notesRepository.add(any(String.class), any(String.class))).thenReturn(Observable.just(11L));

        // first we need to attach view
        notesPresenter.attachView(notesView);

        // call presenter implementation
        notesPresenter.createNote();

        // check if view is called with corresponding
        verify(notesView).navigateToEditNoteView(11L);
    }
}
