package io.paper.android.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.paper.android.PaperApp;
import io.paper.android.R;
import io.paper.android.models.Note;

public final class NotesFragment extends Fragment {

    @BindView(R.id.toolbar_notes)
    Toolbar toolbar;

    @BindView(R.id.recyclerview_notes)
    RecyclerView recyclerView;

    @Nullable
    Unbinder unbinder;

    NotesAdapter notesAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // injecting dependencies
        PaperApp.getAppComponent(context).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        setupToolbar();
        setupRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.app_name));
    }

    private void setupRecyclerView() {
        notesAdapter = new NotesAdapter(LayoutInflater.from(getActivity()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notesAdapter);

        notesAdapter.swapData(generateStubNotes());
    }

    private static List<Note> generateStubNotes() {
        return Arrays.asList(
                new Note("Note 1", "Some stupid description"),
                new Note("Note 2", "Some stupid description"));
    }
}
