package com.uzapp.view.main.tickets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.uzapp.view.main.MainActivity;
import com.viewpagerindicator.IconPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 02.09.16.
 */
public class TodayTicketsFragment extends Fragment {
    private static final int NUMBER_OF_STACKED_ITEMS = 2;
    private static final float DEFAULT_CURRENT_PAGE_SCALE = 1f;
    private static final float DEFAULT_TOP_STACKED_SCALE = 0.81f;
    private static final float DEFAULT_ALPHA_FACTOR = 0.4f;
    private static final float DEFAULT_SHIFT_FACTOR = 0.15f;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.closeBtn) ImageButton closeBtn;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.pageIndicator) IconPageIndicator pageIndicator;
    @BindView(R.id.noContentLayout) ViewGroup noContentLayout;
    @BindDimen(R.dimen.small_padding) int marginLeft;
    private Unbinder unbinder;
    private TodayTicketsPagerAdapter ticketAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_ticket_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.tickets_today);
        progressBar.setVisibility(View.VISIBLE);
        noContentLayout.setVisibility(View.GONE);
        pageIndicator.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        loadTickets();
        return view;
    }

    private void setupViewPager(List<ShortTicket> ticketForAdapterList) {
        ticketAdapter = new TodayTicketsPagerAdapter(getChildFragmentManager());
        ticketAdapter.addTickets(ticketForAdapterList);
        viewPager.setPageTransformer(true, new StackPageTransformer(marginLeft, NUMBER_OF_STACKED_ITEMS,
                DEFAULT_CURRENT_PAGE_SCALE, DEFAULT_TOP_STACKED_SCALE, DEFAULT_ALPHA_FACTOR, DEFAULT_SHIFT_FACTOR));
        viewPager.setOffscreenPageLimit(NUMBER_OF_STACKED_ITEMS + 1);
        viewPager.setAdapter(ticketAdapter);
        pageIndicator.setViewPager(viewPager);
        onPageSelected(0);
    }

    private void loadTickets() {
        Call call = ApiManager.getApi(getContext()).getUserTickets(null, null, null);//TODO pass today seconds
        call.enqueue(ticketsCallback);
    }

    @OnPageChange(R.id.viewPager)
    void onPageSelected(int position) {
        ticketAdapter.setCurrentPosition(position);
        pageIndicator.setCurrentItem(position);
        pageIndicator.notifyDataSetChanged();
    }

    @OnClick(R.id.searchBtn)
    void onSearchBtnClicked() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.closeBtn)
    void onCloseBtnClicked() {
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).hideNavigationBar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).showNavigationBar();
    }

    private void prepareAndShowTickets(List<TicketsResponse> ticketsResponses) {
        List<ShortTicket> ticketForAdapterList = new ArrayList<>();
        for (TicketsResponse ticketsResponse : ticketsResponses) {
            for (Order order : ticketsResponse.getOrders()) {
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
        if (ticketForAdapterList.size() == 0) {
            progressBar.setVisibility(View.GONE);
            noContentLayout.setVisibility(View.VISIBLE);
        } else {
            setupViewPager(ticketForAdapterList);
            progressBar.setVisibility(View.GONE);
            toolbarTitle.setText(getString(R.string.tickets_today_count, ticketForAdapterList.size()));
            pageIndicator.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
        }
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
