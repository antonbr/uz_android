package com.uzapp.view.main.purchase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.view.main.wagon.model.Ticket;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreparePurchaseFragment extends Fragment {

    @BindView(R.id.purchasesList)
    RecyclerView purchasesList;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.okBtn)
    Button btnFilter;

    public static final String KEY_TICKET_LIST = "KEY_TICKET_LIST";

    private Unbinder unbinder;

    private ArrayList listTickets = new ArrayList<>();
    private List<Wagon> wagonsLists;

    public PreparePurchaseFragment() {
        // Required empty public constructor
    }

    public static PreparePurchaseFragment getInstance(List<Ticket> listTickets) {
        PreparePurchaseFragment fragment = new PreparePurchaseFragment();
        Bundle bundle = new Bundle();
//        bundle.putSerializable(KEY_TICKET_LIST, (Serializable) listTickets);
        bundle.putParcelableArrayList(KEY_TICKET_LIST, (ArrayList) listTickets);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listTickets = getArguments().getParcelableArrayList(KEY_TICKET_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prepare_purchase, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText("Оформить покупку");
        PurchasesAdapter purchasesAdapter = new PurchasesAdapter(getContext(), listTickets);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        purchasesList.setLayoutManager(linearLayoutManager);
        purchasesList.setAdapter(purchasesAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
