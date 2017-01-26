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
import com.uzapp.pojo.NewTicketDates;
import com.uzapp.pojo.tickets.Order;
import com.uzapp.pojo.tickets.Ticket;
import com.uzapp.pojo.tickets.TicketsResponse;
import com.uzapp.network.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.search.date.PickDateFragment;
import com.uzapp.view.utils.EndlessRecyclerScrollListener;
import com.uzapp.view.utils.SpaceItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
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
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.myTicketsList)
    RecyclerView myTicketsList;
    @BindView(R.id.filterDate)
    TextView filterDateView;
    @BindView(R.id.ticketCloseBtn)
    ImageButton ticketCloseBtn;
    @BindView(R.id.ticketCalendarBtn)
    ImageButton ticketCalendarBtn;
    @BindView(R.id.noContentLayout)
    ViewGroup noContentLayout;
    @BindView(R.id.ticketDateLayout)
    ViewGroup ticketDateLayout;
    @BindString(R.string.ticket_pick_date)
    String ticketPickDateHint;
    private Unbinder unbinder;
    private LinkedHashSet<Order> orderList = new LinkedHashSet<Order>();
    private MyTicketsListAdapter ticketAdapter;
    private Date filterDate;
    private Date todayDate;
    private EndlessRecyclerScrollListener scrollListener;
    private ArrayList<String> ticketDatesList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_tickets_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).showNavigationBar();
        ((MainActivity) getActivity()).getBottomNavigationBar().setCurrentItem(Constants.BOTTOM_NAVIGATION_TICKETS, false);
        ticketAdapter = new MyTicketsListAdapter(getContext());
        myTicketsList.setLayoutManager(new LinearLayoutManager(getContext()));
        myTicketsList.setAdapter(ticketAdapter);
        myTicketsList.setItemAnimator(new MyTicketsItemAnimator());
        myTicketsList.addItemDecoration(new SpaceItemDecoration((int) getContext().getResources().getDimension(R.dimen.small_padding)));
        setScrollListener();
        Calendar calendar = Calendar.getInstance();
        todayDate = calendar.getTime();
        ticketDateLayout.setVisibility(View.GONE);
        myTicketsList.setVisibility(View.GONE);
        noContentLayout.setVisibility(View.GONE);
        loadTicketDates();
        return view;
    }

    private void setScrollListener() {
        scrollListener = new EndlessRecyclerScrollListener((LinearLayoutManager) myTicketsList.getLayoutManager()) {
            @Override
            public void onLoadMore() {
                List<ShortTicket> ticketList = ticketAdapter.getTicketList();
                ShortTicket lastTicket = ticketList.get(ticketList.size() - 1);
                long lastDate = lastTicket.departureDate;
                loadTickets(lastDate);
            }
        };
        myTicketsList.addOnScrollListener(scrollListener);
    }

    private void removeScrollListener() {
        if (scrollListener != null) {
            myTicketsList.removeOnScrollListener(scrollListener);
        }
    }

    private void loadTickets(Long fromDateSeconds) {
        progressBar.setVisibility(View.VISIBLE);
        Long filterDateSeconds = (filterDate == null) ? null : TimeUnit.MILLISECONDS.toSeconds(filterDate.getTime());
        Call<List<TicketsResponse>> call = ApiManager.getApi(getContext()).getUserTickets(filterDateSeconds, filterDate == null ? fromDateSeconds : null, null);
        call.enqueue(ticketsCallback);
    }

    private void loadTicketDates() {
        progressBar.setVisibility(View.VISIBLE);
        Call<NewTicketDates> call = ApiManager.getApi(getContext()).getNewTicketDates();
        call.enqueue(ticketDatesCallback);
    }

    @OnClick(R.id.ticketCalendarBtn)
    void onTicketCalendarBtnClicked() {
        if (ticketDatesList != null) {
            PickDateFragment fragment = PickDateFragment.getInstance(ticketDatesList);
            fragment.setTargetFragment(this, SELECT_FILTER_DATE);
            ((MainActivity) getActivity()).addFragment(fragment, R.anim.slide_up, R.anim.slide_down);
        }
    }

    @OnClick(R.id.ticketCloseBtn)
    void onTicketCloseBtnClicked() {
        hideFilterDate();
        filterDate = null;
        ticketAdapter.clearTickets();
        loadTickets(TimeUnit.MILLISECONDS.toSeconds(todayDate.getTime()));
    }

    @OnClick(R.id.searchBtn)
    void onSearchBtnClicked() {
        ((MainActivity) getActivity()).getBottomNavigationBar().setCurrentItem(Constants.BOTTOM_NAVIGATION_SEARCH, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void prepareAndShowTickets(List<TicketsResponse> ticketsResponses) {
        List<ShortTicket> ticketForAdapterList = new ArrayList<>();
        for (TicketsResponse ticketsResponse : ticketsResponses) {
            for (Order order : ticketsResponse.getOrders()) {
                if (orderList.add(order)) {
                    for (Ticket ticket : order.getTickets()) {
                        for (int i = 0; i < ticket.getDocument().getPlacesCount(); i++) {//TODO decide if we need separate places in different tickets
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
        }
        ticketAdapter.addTickets(ticketForAdapterList);
        progressBar.setVisibility(View.GONE);
        ticketDateLayout.setVisibility(View.VISIBLE);
        myTicketsList.setVisibility(View.VISIBLE);
        noContentLayout.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILTER_DATE) {
                filterDate = (Date) data.getSerializableExtra("date");
                ticketAdapter.clearTickets();
                orderList.clear();
                showFilterDate();
                loadTickets(TimeUnit.MILLISECONDS.toSeconds(todayDate.getTime()));
            }
        }
    }

    private void showFilterDate() {
        filterDateView.setText(filterDateFormat.format(filterDate));
        ticketCloseBtn.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams ticketCalendarBtnLayoutParams = (RelativeLayout.LayoutParams) ticketCalendarBtn.getLayoutParams();
        ticketCalendarBtnLayoutParams.addRule(RelativeLayout.LEFT_OF, ticketCloseBtn.getId());
        ticketCalendarBtnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        removeScrollListener();
    }

    private void hideFilterDate() {
        filterDateView.setText(ticketPickDateHint);
        ticketCloseBtn.setVisibility(View.GONE);
        RelativeLayout.LayoutParams ticketCalendarBtnLayoutParams = (RelativeLayout.LayoutParams) ticketCalendarBtn.getLayoutParams();
        ticketCalendarBtnLayoutParams.addRule(RelativeLayout.LEFT_OF, 0);
        ticketCalendarBtnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        setScrollListener();
    }

    private Callback<List<TicketsResponse>> ticketsCallback = new Callback<List<TicketsResponse>>() {
        @Override
        public void onResponse(Call<List<TicketsResponse>> call, Response<List<TicketsResponse>> response) {
            if (getView() != null) {
                if (response.isSuccessful()) {
                    prepareAndShowTickets(response.body());
                } else {
                    String error = ApiErrorUtil.getErrorMessage(response, getActivity());
                    CommonUtils.showSnackbar(getView(), error);
                    progressBar.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailure(Call<List<TicketsResponse>> call, Throwable t) {
            if (getView() != null && t != null) {
                progressBar.setVisibility(View.GONE);
                String error = ApiErrorUtil.getErrorMessage(t, getActivity());
                CommonUtils.showSnackbar(getView(), error);
            }
        }
    };

    private Callback<NewTicketDates> ticketDatesCallback = new Callback<NewTicketDates>() {
        @Override
        public void onResponse(Call<NewTicketDates> call, Response<NewTicketDates> response) {
            if (getView() != null) {
                if (response.isSuccessful()) {
                    ticketDatesList = response.body().getDates();
                    if (ticketDatesList != null && ticketDatesList.size() > 0) {
                        loadTickets(TimeUnit.MILLISECONDS.toSeconds(todayDate.getTime()));
                    } else {
                        ticketDateLayout.setVisibility(View.GONE);
                        myTicketsList.setVisibility(View.GONE);
                        noContentLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    String error = ApiErrorUtil.getErrorMessage(response, getActivity());
                    CommonUtils.showSnackbar(getView(), error);
                }
            }
        }

        @Override
        public void onFailure(Call<NewTicketDates> call, Throwable t) {
            if (getView() != null && t != null) {
                String error = ApiErrorUtil.getErrorMessage(t, getActivity());
                CommonUtils.showSnackbar(getView(), error);
                progressBar.setVisibility(View.GONE);
            }
        }
    };
}
