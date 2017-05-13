package io.paper.android.listnotes;

import android.support.v7.util.DiffUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.paper.android.commons.schedulers.ImmediateSchedulerProvider;
import io.paper.android.commons.tuples.Pair;
import io.paper.android.notes.Note;
import io.paper.android.notes.NotesRepository;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ListNotesPresenterTests {

    @Mock
    private NotesRepository notesRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ListNotesView listNotesView;

    @Captor
    private ArgumentCaptor<Pair<List<Note>, DiffUtil.DiffResult>> notesCaptor;

    @Captor
    private ArgumentCaptor<Long> navigateToEditNoteViewCaptor;

    private PublishSubject<Object> createNoteSubject;
    private ListNotesPresenter listNotesPresenter;

    private List<Note> notes = Arrays.asList(
            Note.builder().id(1L).title("test_title_one").description("test_description_one").build(),
            Note.builder().id(2L).title("test_title_two").description("test_description_two").build()
    );

    @Before
    public void setupNotesPresenter() {
        MockitoAnnotations.initMocks(this);

        createNoteSubject = PublishSubject.create();
        listNotesPresenter = new ListNotesPresenterImpl(new ImmediateSchedulerProvider(), notesRepository);

        when(listNotesView.createNoteButtonClicks()).thenReturn(createNoteSubject);
        when(notesRepository.list()).thenReturn(Observable.just(notes));
        when(notesRepository.add(any(String.class), any(String.class)))
                .thenReturn(Observable.just(11L));
    }

    @Test
    public void attachViewShouldShowNotes() throws Exception {
        listNotesPresenter.attachView(listNotesView);

        verify(notesRepository).list();
        verify(listNotesView.showNotes()).accept(notesCaptor.capture());
        assertThat(notesCaptor.getValue()).isEqualTo(notes);
        verifyNoMoreInteractions(notesRepository);
    }

    @Test
    public void addButtonClickShouldCreateNote() throws Exception {
        listNotesPresenter.attachView(listNotesView);

        // trigger click
        createNoteSubject.onNext(new Object());

        verify(listNotesView.navigateToEditNoteView())
                .accept(navigateToEditNoteViewCaptor.capture());
        assertThat(navigateToEditNoteViewCaptor.getValue()).isEqualTo(11L);
    }
}
