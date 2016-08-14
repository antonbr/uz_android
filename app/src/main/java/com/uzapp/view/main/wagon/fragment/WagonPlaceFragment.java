package com.uzapp.view.main.wagon.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.placeslist.PricesPlacesList;
import com.uzapp.pojo.prices.Prices;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.wagon.adapter.HorizontalAdapter;
import com.uzapp.view.main.wagon.adapter.TicketAdapter;
import com.uzapp.view.main.wagon.adapter.WagonTypeAdapter;
import com.uzapp.view.main.wagon.model.Ticket;
import com.uzapp.view.main.wagon.view.ListViewMaxHeight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class WagonPlaceFragment extends Fragment implements SlidingDrawer.OnDrawerOpenListener,
        SlidingDrawer.OnDrawerCloseListener {

    public static final String EXTRA_PRICES = "EXTRA_PRICES";
    public static final String EXTRA_PRICES_POSITION = "EXTRA_PRICES_POSITION";

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.layoutBuyReserveTicket)
    LinearLayout layoutBuyReserveTicket;
    @BindView(R.id.txtWagonNumber)
    TextView txtWagonNumber;
    @BindView(R.id.backBtn)
    ImageButton backImageBtn;
    @BindView(R.id.okBtn)
    Button btnFilter;
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.slidePanelFooter)
    ImageButton slidePanelFooter;
    @BindView(R.id.slidingDrawer)
    SlidingDrawer slidingDrawer;
    @BindView(R.id.horizontalRecyclerView)
    RecyclerView horizontalRecyclerView;
    @BindView(R.id.listViewSelectTicket)
    ListViewMaxHeight listViewSelectTicket;
    @BindView(R.id.btnBuyTicket)
    Button btnBuyTicket;
    @BindView(R.id.btnReserveTicket)
    Button btnReserveTicket;
    @BindView(R.id.btnGoToRegistration)
    Button btnGoToRegistration;

    private static List<Ticket> listTickets = new ArrayList<>();

    private Unbinder unbinder;

    private int stationFromCode, stationToCode, date, cost, position;;
    private String train, wagonTypes, wagonClasses, wagonNumbers;
    private List<PricesPlacesList> listPlaces;
    private Prices prices;

    public WagonPlaceFragment newInstance(Prices prices, int position) {
        WagonPlaceFragment fragment = new WagonPlaceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PRICES, prices);
        bundle.putInt(EXTRA_PRICES_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prices = getArguments().getParcelable(EXTRA_PRICES);
            position = getArguments().getInt(EXTRA_PRICES_POSITION);

            if (prices != null) {
                stationFromCode = prices.getStation_from_code();
                stationToCode = prices.getStationToCode();
                train = prices.getTrain().getNumber();
                wagonTypes = prices.getTrain().getWagons().get(position).getTypeCode();
                wagonClasses = String.valueOf(prices.getTrain().getWagons().get(position).getClass_code());
                wagonNumbers = prices.getTrain().getWagons().get(position).getNumber();
                date = prices.getTrain().getDate();
                cost = prices.getTrain().getWagons().get(position).getCost();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_wagon_place, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        setHorizontalWagonsAdapter(prices);

        String title = prices.getTrain().getWagons().get(position).getTypeName();

        toolbarTitle.setText(title + " (" + prices.getTrain().getWagons().get(position).getPlacesPrices().getTotal() + ")");
        txtWagonNumber.setText(getString(R.string.wagon) + " №" + wagonNumbers);

        Call<List<PricesPlacesList>> call = ApiManager.getApi(getActivity()).getPlacesList(stationFromCode,
                stationToCode, train, wagonTypes, wagonClasses, wagonNumbers, date);
        call.enqueue(listPlacesCallback);

        initSlideMenu();

        if (listTickets != null && listTickets.size() > 0) {
            layoutBuyReserveTicket.setVisibility(View.VISIBLE);
            setAdapter(null, false);
        }

        return rootView;
    }

    @OnClick(R.id.backBtn)
    void onBackBtnPressed() {
        getActivity().onBackPressed();
        listTickets = new ArrayList<>();
    }

    @OnClick(R.id.btnBuyTicket)
    void onClickBtnBuyTicket() {
        Toast.makeText(getActivity(), "buy", Toast.LENGTH_SHORT).show();
        setBackgroundBtn();
    }

    @OnClick(R.id.btnReserveTicket)
    void onClickBtnReserveTicket() {
        Toast.makeText(getActivity(), "reserve", Toast.LENGTH_SHORT).show();
        setBackgroundBtn();
    }

    @OnClick(R.id.btnGoToRegistration)
    void onClickBtnGoToRegistration() {
        Toast.makeText(getActivity(), "registration", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.okBtn)
    void onFilterClicked() {

    }

    @Override
    public void onDrawerClosed() {
        slidePanelFooter.setImageResource(R.drawable.ic_footer_up);
    }

    @Override
    public void onDrawerOpened() {
        slidePanelFooter.setImageResource(R.drawable.ic_footer_down);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Callback<List<PricesPlacesList>> listPlacesCallback = new Callback<List<PricesPlacesList>>()  {

        @Override
        public void onResponse(Call<List<PricesPlacesList>> call, Response<List<PricesPlacesList>> response) {
            if (response.isSuccessful()) {
                listPlaces = response.body();
                addWagonView();
            }
        }

        @Override
        public void onFailure(Call<List<PricesPlacesList>> call, Throwable t) {
            Toast.makeText(getActivity(), call.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void setAdapter(Ticket ticket, boolean isRemoveItem) {
        TicketAdapter adapter = new TicketAdapter(getActivity(), listTickets);
        if (isRemoveItem) {
            Iterator<Ticket> it = listTickets.iterator();
            while (it.hasNext()) {
                Ticket itemTicketList = it.next();
                if ((itemTicketList.getPlaceNumber()).equals(ticket.getPlaceNumber()) &&
                        (itemTicketList.getWagonNumber()).equals(ticket.getWagonNumber())) {
                    it.remove();
                }
            }
        } else {
            if (ticket != null) {
                listTickets.add(ticket);
                layoutBuyReserveTicket.setVisibility(View.VISIBLE);
            }
        }
        adapter.notifyDataSetChanged();
        listViewSelectTicket.setAdapter(adapter);

        if (listTickets.isEmpty()) {
            layoutBuyReserveTicket.setVisibility(View.GONE);
        }
    }

    private void setBackgroundBtn() {
        btnReserveTicket.setBackground(CommonUtils.changeBackgroundPlace(getActivity(), btnReserveTicket));
        btnReserveTicket.setTextColor(CommonUtils.changeTextColorPlace(getActivity(), btnReserveTicket, R.color.bgButtonWagonPlacesSelected));
        btnBuyTicket.setBackground(CommonUtils.changeBackgroundPlace(getActivity(), btnBuyTicket));
        btnBuyTicket.setTextColor(CommonUtils.changeTextColorPlace(getActivity(), btnBuyTicket, R.color.bgButtonWagonPlacesSelected));
    }

    private void initSlideMenu() {
        slidingDrawer.setOnDrawerOpenListener(this);
        slidingDrawer.setOnDrawerCloseListener(this);
    }

    private void addWagonView() {
        for (int i = 0; i < Constants.SECTION; i++) {
            List<Integer> places = listPlaces.get(0).getTrainPlacesList()
                    .getWagons().get(Integer.parseInt(wagonNumbers) - 1).getPlaces();
            WagonTypeAdapter adapter = new WagonTypeAdapter(getActivity(), places, wagonNumbers, cost, wagonTypes);
            View viewWagon  = adapter.getView(i, null, linearLayout);
            // content in view
            linearLayout.addView(viewWagon);
        }

        if (listTickets != null) {
            ticketPlaceCheckedView(listTickets);
        }
    }

    public void ticketPlaceRemoveView(int place, String wagonNumber) {
        Button placesBtn = ((Button) findPlaceInWagonView(place));
        if (placesBtn != null) {
            if (placesBtn.getTag() == Integer.valueOf(place) && wagonNumber.equals(wagonNumbers)) {
                placesBtn.setBackground(getResources().getDrawable(R.drawable.border_button_place));
                placesBtn.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    public void ticketPlaceCheckedView(List<Ticket> places) {
        for (int i = 0; i < places.size(); ++i) {
            int placeNumber = Integer.parseInt(places.get(i).getPlaceNumber());
            Button placesBtn = ((Button) findPlaceInWagonView(placeNumber));
            if (placesBtn != null) {
                if (placesBtn.getTag() == Integer.valueOf(placeNumber) && places.get(i).getWagonNumber().equals(wagonNumbers)) {
                    placesBtn.setBackground(getResources().getDrawable(R.drawable.border_button_place_selected));
                    placesBtn.setTextColor(getResources().getColor(android.R.color.white));
                }
            }
        }
    }

    public View findPlaceInWagonView(int place) {
        for (int i = 0; i < Constants.SECTION; i++) {
            // content in view
            for(int index = 0; index < (linearLayout).getChildCount(); ++index) {
                LinearLayout linearLayoutChildAt = (LinearLayout) linearLayout.getChildAt(index);
                for(int index2 = 0; index2 < linearLayoutChildAt.getChildCount(); ++index2) {
                    RelativeLayout relativeLayoutChildAt = (RelativeLayout) linearLayoutChildAt.getChildAt(index2);
                    for(int index3 = 0; index3 < relativeLayoutChildAt.getChildCount(); ++index3) {
                        RelativeLayout relativeLayoutChildAt2 = (RelativeLayout) relativeLayoutChildAt.getChildAt(index3);
                        for(int index4 = 0; index4 < relativeLayoutChildAt2.getChildCount(); ++index4) {
                            View viewChildAt2 = relativeLayoutChildAt2.getChildAt(index4);
                            if (viewChildAt2 instanceof Button) {
                                if (viewChildAt2.getTag() == Integer.valueOf(place)) {
                                    return viewChildAt2;
                                }
                            } else {
                                for(int index5 = 0; index5 < relativeLayoutChildAt2.getChildCount(); ++index5) {
                                    LinearLayout linearLayoutChildAt2 = (LinearLayout) relativeLayoutChildAt2.getChildAt(index5);
                                    for(int index6 = 0; index6 < linearLayoutChildAt2.getChildCount(); ++index6) {
                                        View viewChildAt3 = linearLayoutChildAt2.getChildAt(index6);
                                        if (viewChildAt3.getTag() == Integer.valueOf(place)) {
                                            return viewChildAt3;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void setHorizontalWagonsAdapter(Prices prices) {
        HorizontalAdapter horizontalAdapter = new HorizontalAdapter(getActivity(), prices);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(linearLayoutManager);
        horizontalRecyclerView.setAdapter(horizontalAdapter);
    }
}
