package com.uzapp.view.main.purchase.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uzapp.R;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.tickets.MyTicketsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentDoneFragment extends Fragment {

    @BindView(R.id.btnGoMyTicket) Button btnGoMyTicket;

    private Unbinder unbinder;

    public PaymentDoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_done, container, false);
        unbinder = ButterKnife.bind(this, view);
        initComponents();
        return view;
    }

    private void initComponents() {
        ((MainActivity) getActivity()).showNavigationBar();
    }

    @OnClick(R.id.btnGoMyTicket)
    void onClickPay() {
        ((MainActivity) getActivity()).replaceFragment(new MyTicketsFragment(), false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
