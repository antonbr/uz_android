package com.uzapp.view.main.tickets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.tickets.Order;
import com.uzapp.pojo.tickets.Ticket;
import com.uzapp.pojo.tickets.TicketsResponse;
import com.uzapp.util.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;
import com.viewpagerindicator.IconPageIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 23.08.16.
 */
public class MyTicketsFragment extends Fragment {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy, EEEE");
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.backBtn) ImageButton backBtn;
    @BindView(R.id.okBtn) Button okBtn;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.ticketDateLayout) ViewGroup ticketDateLayout;
    @BindView(R.id.ticketDate) TextView ticketDate;
    @BindView(R.id.pageIndicator) IconPageIndicator pageIndicator;
    private Unbinder unbinder;
    private MyTicketsAdapter ticketAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_tickets_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).showNavigationBar();
        ((MainActivity) getActivity()).getBottomNavigationBar().setCurrentItem(Constants.BOTTOM_NAVIGATION_TICKETS, false);
        toolbarTitle.setText(R.string.menu_my_ticket);
        backBtn.setVisibility(View.INVISIBLE);
        okBtn.setVisibility(View.INVISIBLE);
        ticketAdapter = new MyTicketsAdapter(getContext());
        viewPager.setAdapter(ticketAdapter);
        pageIndicator.setViewPager(viewPager);
        progressBar.setVisibility(View.VISIBLE);
        ticketDateLayout.setVisibility(View.GONE);
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

    @OnPageChange(R.id.viewPager)
    void onPageSelected(int position) {
        showDepartureDate(position);
        ticketAdapter.setCurrentPosition(position);
        pageIndicator.setCurrentItem(position);
        pageIndicator.notifyDataSetChanged();
    }

    private void showDepartureDate(int position) {
        long departureSeconds = ticketAdapter.getTicketAtPosition(position).departureDate;
        Date departureDate = new Date(TimeUnit.SECONDS.toMillis(departureSeconds));
        ticketDate.setText(dateFormat.format(departureDate));
    }

    private void prepareAndShowTickets(List<TicketsResponse> ticketsResponses) {
        List<TicketForAdapter> ticketForAdapterList = new ArrayList<>();
        for (TicketsResponse ticketsResponse : ticketsResponses) {
            for (Order order : ticketsResponse.getOrders()) {
                for (Ticket ticket : order.getTickets()) {
                    for (int i = 0; i < ticket.getDocument().getPlacesCount(); i++) {
                        TicketForAdapter ticketForAdapter = new TicketForAdapter();
                        ticketForAdapter.electronic = order.electronic;
                        ticketForAdapter.qrImage = ticket.qrImage;
                        ticketForAdapter.barcodeImage = ticket.barcodeImage;
                        ticketForAdapter.uid = ticket.getDocument().getUid();
                        ticketForAdapter.kind = ticket.getDocument().getKind();
                        ticketForAdapter.departureDate = ticket.getDocument().getDepartureDate();
                        ticketForAdapter.arrivalDate = ticket.getDocument().getArrivalDate();
                        ticketForAdapter.stationFromName = ticket.getDocument().getStationFromName();
                        ticketForAdapter.stationToName = ticket.getDocument().getStationToName();
                        ticketForAdapter.train = ticket.getDocument().getTrain();
                        ticketForAdapter.wagon = ticket.getDocument().getWagon();
                        ticketForAdapter.place = ticket.getDocument().getPlaces().get(i);
                        try {
                            ticketForAdapter.cost = ticket.getDocument().getCosts().get(i).getCost();
                        } catch (Exception e) {
                            ticketForAdapter.cost = 111; //TODO test data on backend
                        }
                        ticketForAdapter.firstname = ticket.getDocument().getFirstname();
                        ticketForAdapter.lastname = ticket.getDocument().getLastname();
                        ticketForAdapterList.add(ticketForAdapter);
                    }
                }
            }
        }
        ticketAdapter.addTickets(ticketForAdapterList);
        progressBar.setVisibility(View.GONE);
        ticketDateLayout.setVisibility(View.VISIBLE);
        showDepartureDate(0);
        pageIndicator.setCurrentItem(0);
        pageIndicator.notifyDataSetChanged();
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
