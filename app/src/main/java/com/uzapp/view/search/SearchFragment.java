package com.uzapp.view.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.uzapp.R;
import com.uzapp.view.search.utils.CheckableImageView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vika on 13.07.16.
 */
public class SearchFragment extends Fragment {
    @BindView(R.id.pathFrom) EditText pathFrom;
    @BindView(R.id.pathTo) EditText pathTo;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.useLocationBtn)
    void onLocationBtnClicked(CheckableImageView imageButton) {
        imageButton.toggle();
        pathFrom.setTranslationY(imageButton.isChecked() ? hintPadding : 0);
       // pathFrom.setText(imageButton.isChecked() ? "Ивано-Франковск" : "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
