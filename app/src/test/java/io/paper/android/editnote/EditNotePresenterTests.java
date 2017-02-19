package io.paper.android.editnote;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.paper.android.notes.Note;
import io.paper.android.notes.NotesRepository;
import io.paper.android.commons.schedulers.ImmediateSchedulerProvider;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class EditNotePresenterTests {

    @Mock
    private NotesRepository notesRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private EditNoteView editNoteView;

    private EditNotePresenter editNotePresenter;
    private PublishSubject<String> noteTitleSubject;
    private PublishSubject<String> noteDescriptionSubject;
    private PublishSubject<Object> toolbarNavigationClicks;
    private Note note;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        editNotePresenter = new EditNotePresenterImpl(11L,
                new ImmediateSchedulerProvider(), notesRepository);
        noteTitleSubject = PublishSubject.create();
        noteDescriptionSubject = PublishSubject.create();
        toolbarNavigationClicks = PublishSubject.create();

        note = Note.builder()
                .id(11L)
                .title("test_note_title")
                .description("test_note_description")
                .build();

        when(editNoteView.noteTitleFieldChanges()).thenReturn(noteTitleSubject);
        when(editNoteView.noteDescriptionFieldChanges()).thenReturn(noteDescriptionSubject);
        when(editNoteView.toolbarNavigationButtonClicks()).thenReturn(toolbarNavigationClicks);

        when(notesRepository.get(any())).thenReturn(Observable.just(note));
    }

    @Test
    public void attachViewShouldRenderNote() throws Exception {
        ArgumentCaptor<Note> showNoteConsumer = ArgumentCaptor.forClass(Note.class);
        editNotePresenter.attachView(editNoteView);

        verify(editNoteView).noteTitleFieldChanges();
        verify(editNoteView).noteDescriptionFieldChanges();
        verify(editNoteView.showNote()).accept(showNoteConsumer.capture());
        verify(notesRepository).get(11L);

        assertThat(showNoteConsumer.getValue()).isEqualTo(note);
    }

    @Test
    public void titleEditsShouldBePersisted() {
        when(notesRepository.putTitle(any(), any())).thenReturn(Observable.just(1));

        editNotePresenter.attachView(editNoteView);
        verify(editNoteView).noteTitleFieldChanges();

        noteTitleSubject.onNext("test_note_another_title");
        verify(notesRepository).putTitle(11L, "test_note_another_title");

        noteTitleSubject.onNext("test_note_more_edits");
        verify(notesRepository).putTitle(11L, "test_note_more_edits");
    }

    @Test
    public void descriptionEditsShouldBePersisted() {
        when(notesRepository.putDescription(any(), any())).thenReturn(Observable.just(1));

        editNotePresenter.attachView(editNoteView);
        verify(editNoteView).noteDescriptionFieldChanges();

        noteDescriptionSubject.onNext("test_note_another_description");
        verify(notesRepository).putDescription(11L, "test_note_another_description");

        noteDescriptionSubject.onNext("test_note_more_edits");
        verify(notesRepository).putDescription(11L, "test_note_more_edits");
    }

    @Test
    public void navigationClickShouldNavigateUp() throws Exception {
        ArgumentCaptor<Object> navigateUpConsumer = ArgumentCaptor.forClass(Object.class);

        editNotePresenter.attachView(editNoteView);
        toolbarNavigationClicks.onNext(new Object());

        verify(editNoteView.navigateUp()).accept(navigateUpConsumer.capture());
        assertThat(navigateUpConsumer.getValue()).isNotNull();
    }

    @Test
    public void detachViewShouldUnsubscribeFromViews() {
        editNotePresenter.attachView(editNoteView);
        assertThat(noteTitleSubject.hasObservers()).isTrue();
        assertThat(noteDescriptionSubject.hasObservers()).isTrue();
        assertThat(toolbarNavigationClicks.hasObservers()).isTrue();

        editNotePresenter.detachView();
        assertThat(noteTitleSubject.hasObservers()).isFalse();
        assertThat(noteDescriptionSubject.hasObservers()).isFalse();
        assertThat(toolbarNavigationClicks.hasObservers()).isFalse();
    }
}
