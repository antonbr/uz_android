package com.uzapp.view.main.tickets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.tickets.Order;
import com.uzapp.pojo.tickets.Ticket;
import com.uzapp.pojo.tickets.TicketsResponse;
import com.uzapp.util.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 29.08.16.
 */
public class MyTicketsFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.myTicketsList) RecyclerView myTicketsList;
    MyTicketsAdapter ticketAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_tickets_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).showNavigationBar();
        ((MainActivity) getActivity()).getBottomNavigationBar().setCurrentItem(Constants.BOTTOM_NAVIGATION_TICKETS, false);
        ticketAdapter = new MyTicketsAdapter(getContext());
        myTicketsList.setLayoutManager(new LinearLayoutManager(getContext()));
        myTicketsList.setAdapter(ticketAdapter);
        myTicketsList.addItemDecoration(new SpaceItemDecoration((int) getContext().getResources().getDimension(R.dimen.small_padding)));
        progressBar.setVisibility(View.VISIBLE);
        loadTickets();
        return view;
    }

    private void loadTickets() {
        Call call = ApiManager.getApi(getContext()).getUserTickets();
        call.enqueue(ticketsCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ((MainActivity) getActivity()).hideNavigationBar();
    }

    private void prepareAndShowTickets(List<TicketsResponse> ticketsResponses) {
        List<ShortTicket> ticketForAdapterList = new ArrayList<>();
        for (TicketsResponse ticketsResponse : ticketsResponses) {
            for (Order order : ticketsResponse.getOrders()) {
                for (Ticket ticket : order.getTickets()) {
                    for (int i = 0; i < ticket.getDocument().getPlacesCount(); i++) {
                        ShortTicket shortTicket = new ShortTicket();
                        shortTicket.electronic = order.electronic;
                        shortTicket.qrImage = ticket.qrImage;
                        shortTicket.barcodeImage = ticket.barcodeImage;
                        shortTicket.uid = ticket.getDocument().getUid();
                        shortTicket.kind = ticket.getDocument().getKind();
                        shortTicket.departureDate = ticket.getDocument().getDepartureDate();
                        shortTicket.arrivalDate = ticket.getDocument().getArrivalDate();
                        shortTicket.stationFromName = ticket.getDocument().getStationFromName();
                        shortTicket.stationToName = ticket.getDocument().getStationToName();
                        shortTicket.train = ticket.getDocument().getTrain();
                        shortTicket.wagon = ticket.getDocument().getWagon();
                        shortTicket.place = ticket.getDocument().getPlaces().get(i);
                        shortTicket.wagonClass = ticket.getDocument().getWagonClass();
                        shortTicket.wagonType = ticket.getDocument().getWagonType();
                        try {
                            shortTicket.cost = ticket.getDocument().getCosts().get(i).getCost();
                        } catch (Exception e) {
                            shortTicket.cost = 111; //TODO test data on backend
                        }
                        shortTicket.firstname = ticket.getDocument().getFirstname();
                        shortTicket.lastname = ticket.getDocument().getLastname();
                        ticketForAdapterList.add(shortTicket);
                    }
                }
            }
        }
        ticketAdapter.setTickets(ticketForAdapterList);
        progressBar.setVisibility(View.GONE);
    }

    private Callback<List<TicketsResponse>> ticketsCallback = new Callback<List<TicketsResponse>>() {
        @Override
        public void onResponse(Call<List<TicketsResponse>> call, Response<List<TicketsResponse>> response) {
            if (getView() != null) {
                if (response.isSuccessful()) {
                    prepareAndShowTickets(response.body());
                } else {
                    String error = ApiErrorUtil.parseError(response);
                    CommonUtils.showMessage(getView(), error);
                    progressBar.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailure(Call<List<TicketsResponse>> call, Throwable t) {
            if (getView() != null && t != null) {
                CommonUtils.showMessage(getView(), t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        }
    };
}
