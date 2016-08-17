package com.uzapp.view.main.menu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uzapp.R;
import com.uzapp.util.PrefsUtil;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.login.LoginFlowActivity;
import com.uzapp.view.main.profile.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    @BindView(R.id.btnLoginYourAccount)
    Button btnLoginYourAccount;

    private Unbinder unbinder;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.btnLoginYourAccount)
    void onClickBtnLoginYourAccount() {
        if (TextUtils.isEmpty(PrefsUtil.getStringPreference(getContext(), PrefsUtil.USER_ACCESS_TOKEN))) {
            Intent i = new Intent(getActivity(), LoginFlowActivity.class);
            startActivity(i);
        } else {
            ((BaseActivity) getActivity()).replaceFragment(new ProfileFragment(), true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
