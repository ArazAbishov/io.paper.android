package io.paper.android.commons.views;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.paper.android.PaperApp;

public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((PaperApp) getActivity().getApplicationContext())
                .refWatcher().watch(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    protected void bind(@NonNull Object target, @NonNull android.view.View view) {
        this.unbinder = ButterKnife.bind(target, view);
    }
}
