package com.uzapp.view.main.tickets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.uzapp.view.main.search.date.PickDateFragment;
import com.uzapp.view.utils.SpaceItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 29.08.16.
 */
public class MyTicketsFragment extends Fragment {
    protected static final int SELECT_FILTER_DATE = 1;
    private SimpleDateFormat filterDateFormat = new SimpleDateFormat("EEEE, d MMM");
    private Unbinder unbinder;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.myTicketsList) RecyclerView myTicketsList;
    @BindView(R.id.filterDate) TextView filterDateView;
    @BindView(R.id.ticketCloseBtn) ImageButton ticketCloseBtn;
    @BindView(R.id.ticketCalendarBtn) ImageButton ticketCalendarBtn;
    @BindString(R.string.ticket_pick_date) String ticketPickDateHint;
    MyTicketsAdapter ticketAdapter;
    Date filterDate;
    Date todayDate;

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
        myTicketsList.setItemAnimator(new MyTicketsItemAnimator());
        myTicketsList.addItemDecoration(new SpaceItemDecoration((int) getContext().getResources().getDimension(R.dimen.small_padding)));
        Calendar calendar = Calendar.getInstance();
        todayDate = calendar.getTime();
        loadTickets();
        return view;
    }

    private void loadTickets() {
        progressBar.setVisibility(View.VISIBLE);
        Long filterDateSeconds = (filterDate == null) ? null : TimeUnit.MILLISECONDS.toSeconds(filterDate.getTime());
        Long fromDateSeconds = filterDate == null ? TimeUnit.MILLISECONDS.toSeconds(todayDate.getTime()) : null;
        Call call = ApiManager.getApi(getContext()).getUserTickets(filterDateSeconds, fromDateSeconds, null);
        call.enqueue(ticketsCallback);
    }

    @OnClick(R.id.ticketCalendarBtn)
    void onTicketCalendarBtnClicked() {
        Calendar calendar = CommonUtils.getCalendar();
        PickDateFragment fragment = PickDateFragment.getInstance(calendar.getTime());
        fragment.setTargetFragment(this, SELECT_FILTER_DATE);
        ((MainActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
    }

    @OnClick(R.id.ticketCloseBtn)
    void onTicketCloseBtnClicked() {
        hideFilterDate();
        filterDate = null;
        ticketAdapter.clearTickets();
        loadTickets();
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
        Collections.sort(ticketForAdapterList, new Comparator<ShortTicket>() {
            public int compare(ShortTicket t1, ShortTicket t2) {
                return (int) (t1.departureDate - t2.departureDate);
            }
        });
        ticketAdapter.setTickets(ticketForAdapterList);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILTER_DATE) {
                filterDate = (Date) data.getSerializableExtra("date");
                loadTickets();
                ticketAdapter.clearTickets();
                showFilterDate();
            }
        }
    }

    private void showFilterDate() {
        filterDateView.setText(filterDateFormat.format(filterDate));
        ticketCloseBtn.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams ticketCalendarBtnLayoutParams = (RelativeLayout.LayoutParams) ticketCalendarBtn.getLayoutParams();
        ticketCalendarBtnLayoutParams.addRule(RelativeLayout.LEFT_OF, ticketCloseBtn.getId());
        ticketCalendarBtnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
    }

    private void hideFilterDate() {
        filterDateView.setText(ticketPickDateHint);
        ticketCloseBtn.setVisibility(View.GONE);
        RelativeLayout.LayoutParams ticketCalendarBtnLayoutParams = (RelativeLayout.LayoutParams) ticketCalendarBtn.getLayoutParams();
        ticketCalendarBtnLayoutParams.addRule(RelativeLayout.LEFT_OF, 0);
        ticketCalendarBtnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
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
