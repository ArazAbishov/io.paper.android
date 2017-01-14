package io.paper.android.ui;

import android.support.v4.app.Fragment;

import io.paper.android.PaperApp;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        PaperApp.refWatcher(getActivity()).watch(this);
    }
}
