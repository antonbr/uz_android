package com.uzapp.view.main.wagon.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiErrorUtil;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.WagonType;
import com.uzapp.pojo.placeslist.PricesPlacesList;
import com.uzapp.pojo.placeslist.WagonsPlacesList;
import com.uzapp.pojo.prices.Prices;
import com.uzapp.pojo.prices.WagonsPrices;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.purchase.fragment.PreparePurchaseFragment;
import com.uzapp.view.main.wagon.adapter.HorizontalAdapter;
import com.uzapp.view.main.wagon.adapter.TicketAdapter;
import com.uzapp.view.main.wagon.adapter.WagonKupeAdapter;
import com.uzapp.view.main.wagon.adapter.WagonLuxAdapter;
import com.uzapp.view.main.wagon.adapter.WagonPlatskartAdapter;
import com.uzapp.view.main.wagon.model.Ticket;
import com.uzapp.view.main.wagon.model.Wagon;
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
public class WagonPlaceFragment extends Fragment {

    public static final String EXTRA_PRICES = "EXTRA_PRICES";
    public static final String EXTRA_PRICES_POSITION = "EXTRA_PRICES_POSITION";
    public static final String EXTRA_TRAIN_DEPARTURE_DATE = "EXTRA_TRAIN_DEPARTURE_DATE";
    public static final String EXTRA_TRAIN_ARRIVAL_DATE = "EXTRA_TRAIN_ARRIVAL_DATE";
    public static final String EXTRA_TRAIN_DATE = "EXTRA_TRAIN_DATE";
    public static final String EXTRA_WAGON_TYPE = "EXTRA_WAGON_TYPE";
    @BindView(R.id.wagonRecyclerView)
    RecyclerView wagonRecyclerView;
    @BindView(R.id.layoutBuyReserveTicket)
    LinearLayout layoutBuyReserveTicket;
    @BindView(R.id.txtWagonNumber)
    TextView txtWagonNumber;
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.horizontalRecyclerView)
    RecyclerView horizontalRecyclerView;
    @BindView(R.id.listViewSelectTicket)
    ListViewMaxHeight listViewSelectTicket;
    @BindView(R.id.btnGoToRegistration)
    Button btnGoToRegistration;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Unbinder unbinder;

    private int stationFromCode, stationToCode, dateTrain, departureDate, arrivalDate, position;
    private String train, wagonTypes, wagonClasses, wagonNumbers, trainName, stationFromName, stationToName;
    private boolean isBooking = true;
    private boolean isReserve = false;
    private List<Ticket> listTickets = new ArrayList<>();
    private List<Wagon> wagonsLists;
    private Prices prices;
    private long selectDate;
    private WagonType wagonType;

    public static WagonPlaceFragment newInstance(Prices prices, int position, int departureDate, int arrivalDate, long selectDate, WagonType type) {
        WagonPlaceFragment fragment = new WagonPlaceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PRICES, prices);
        bundle.putInt(EXTRA_PRICES_POSITION, position);
        bundle.putInt(EXTRA_TRAIN_DEPARTURE_DATE, departureDate);
        bundle.putInt(EXTRA_TRAIN_ARRIVAL_DATE, arrivalDate);
        bundle.putLong(EXTRA_TRAIN_DATE, selectDate);
        bundle.putSerializable(EXTRA_WAGON_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wagon_place, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        ((MainActivity) getActivity()).hideNavigationBar();
        initUI();
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataFromArgument();
    }

    /**
     * Set data in variable from arguments
     */
    private void setDataFromArgument() {
        if (getArguments() != null) {
            prices = getArguments().getParcelable(EXTRA_PRICES);
            position = getArguments().getInt(EXTRA_PRICES_POSITION);
            departureDate = getArguments().getInt(EXTRA_TRAIN_DEPARTURE_DATE);
            arrivalDate = getArguments().getInt(EXTRA_TRAIN_ARRIVAL_DATE);
            selectDate = getArguments().getLong(EXTRA_TRAIN_DATE);
            wagonType = (WagonType) getArguments().getSerializable(EXTRA_WAGON_TYPE);
            if (prices != null) {
                stationFromCode = prices.getStation_from_code();
                stationToCode = prices.getStationToCode();
                train = prices.getTrain().getNumber();
                trainName = train + " \"" + prices.getTrain().getFastedName() + "\"";
                stationFromName = prices.getStationFromName();
                stationToName = prices.getStationToName();
                StringBuilder wagonTypesSb = new StringBuilder();
                StringBuilder wagonClassesSb = new StringBuilder();
                StringBuilder wagonNumbersSb = new StringBuilder();
                List<WagonsPrices> wagonsPrices = prices.getTrain().getWagons();
                for (WagonsPrices wagon : wagonsPrices) {
                    wagonTypesSb.append(wagon.getTypeCode().getShortName()).append(",");
                    wagonClassesSb.append(wagon.getClassCode()).append(",");
                    wagonNumbersSb.append(wagon.getNumber()).append(",");
                }
                wagonTypes = wagonsPrices.size() > 0 ? wagonTypesSb.substring(0, wagonTypesSb.lastIndexOf(",")) : "";
                wagonClasses = wagonsPrices.size() > 0 ? wagonClassesSb.substring(0, wagonClassesSb.lastIndexOf(",")) : "";
                wagonNumbers = wagonsPrices.size() > 0 ? wagonNumbersSb.substring(0, wagonNumbersSb.lastIndexOf(",")) : "";
                dateTrain = prices.getTrain().getDate();
            }
        }
    }


    private void initUI() {
        showProgress(true);
        showWagonLayout(false);
        wagonRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ((SimpleItemAnimator) wagonRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        Call<List<PricesPlacesList>> call = ApiManager.getApi(getActivity()).getPlacesList(stationFromCode,
                stationToCode, train, wagonTypes, wagonClasses, wagonNumbers, dateTrain);
        call.enqueue(listPlacesCallback);
        if (listTickets != null && listTickets.size() > 0) {
            layoutBuyReserveTicket.setVisibility(View.VISIBLE);
            setAdapter(null, false);
        }
    }

    @OnClick(R.id.leftImageBtn)
    void onBackBtnPressed() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btnGoToRegistration)
    void onClickBtnGoToRegistration() {
        ((MainActivity) getActivity()).replaceFragment(PreparePurchaseFragment
                .getInstance(listTickets, trainName, stationFromName, stationToName,
                        train, selectDate, stationFromCode, stationToCode), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showBuyReserveLayout(boolean show) {
        layoutBuyReserveTicket.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showWagonLayout(boolean show) {
        wagonRecyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private Callback<List<PricesPlacesList>> listPlacesCallback = new Callback<List<PricesPlacesList>>() {

        @Override
        public void onResponse(Call<List<PricesPlacesList>> call, Response<List<PricesPlacesList>> response) {
            if (response.isSuccessful()) {
                List<PricesPlacesList> pricesPlacesLists = response.body();
                List<WagonsPlacesList> wagonsPlacesLists = new ArrayList<>();
                for (PricesPlacesList pricePlace : pricesPlacesLists) {
                    wagonsPlacesLists.addAll(pricePlace.getTrainPlacesList().getWagons());
                }
                wagonsLists = getWagonsList(prices.getTrain().getWagons(), wagonsPlacesLists);

                setHorizontalWagonsAdapter(wagonsLists);
                showWagon(wagonsLists, 0);

                showProgress(false);
                showWagonLayout(true);
            } else {
                String error = ApiErrorUtil.getErrorMessage(response, getActivity());
                CommonUtils.showSnackbar(getView(), error);
            }
        }

        @Override
        public void onFailure(Call<List<PricesPlacesList>> call, Throwable t) {
            String error = ApiErrorUtil.getErrorMessage(t, getActivity());
            CommonUtils.showSnackbar(getView(), error);
        }
    };

    /**
     * @param listWagonsPrices
     * @param listWagonsPlaces
     * @return wagonArray
     * <p/>
     * Set data to Wagon object and return ArrayList
     */
    private List<Wagon> getWagonsList(List<WagonsPrices> listWagonsPrices, List<WagonsPlacesList> listWagonsPlaces) {
        List<Wagon> wagonArray = new ArrayList<>();
        for (int i = 0; i < listWagonsPrices.size(); i++) {
            if (listWagonsPrices.get(i).getTypeCode() != wagonType) continue;
            Wagon wagon = new Wagon();
            wagon.setCharline(listWagonsPrices.get(i).getCharline());
            wagon.setNumber(listWagonsPrices.get(i).getNumber());
            wagon.setTypeCode(listWagonsPrices.get(i).getTypeCode().getShortName());
            wagon.setTypeName(listWagonsPrices.get(i).getTypeName());
            wagon.setCountryName(listWagonsPrices.get(i).getCountryName());
            wagon.setCountryCode(listWagonsPrices.get(i).getCountryCode());
            wagon.setRailwayCode(listWagonsPrices.get(i).getRailwayCode());
            wagon.setRailwayName(listWagonsPrices.get(i).getRailwayName());
            wagon.setSitting(listWagonsPrices.get(i).isSitting());
            wagon.setClassName(listWagonsPrices.get(i).getClassName());
            wagon.setClassCode(listWagonsPrices.get(i).getClassCode());
            wagon.setCostCurrency(listWagonsPrices.get(i).getCostCurrency());
            wagon.setCost(listWagonsPrices.get(i).getCost());
            wagon.setCostReserve(listWagonsPrices.get(i).getCostReserve());
            wagon.setAllowBonus(listWagonsPrices.get(i).isAllowBonus());
            wagon.setServices(listWagonsPrices.get(i).getServices());
            wagon.setPlacesPrices(listWagonsPrices.get(i).getPlacesPrices());
            wagon.setPlacesCount(listWagonsPlaces.get(i).getPlaceCount());
            wagon.setAvailablePlaces(listWagonsPlaces.get(i).getPlaces());
            wagonArray.add(wagon);
        }
        return wagonArray;
    }

    /**
     * @param ticket
     * @param isRemoveItem Set adapter for list items wagon
     */
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
                showBuyReserveLayout(true);
            }
        }
        adapter.notifyDataSetChanged();
        listViewSelectTicket.setAdapter(adapter);

        if (listTickets.isEmpty()) {
            showBuyReserveLayout(false);
        }
    }

    /**
     * @param wagonsList
     * @param position   Generated wagon with places
     */
    public void showWagon(List<Wagon> wagonsList, int position) {
        Wagon wagon = wagonsList.get(position);
        this.position = position;

        String title = wagon.getTypeName();
//                + " (" + prices.getTrain().getWagons().get(position).getPlacesPrices().getTotal() + ")";
        toolbarTitle.setText(title);
        txtWagonNumber.setText(getString(R.string.wagon) + " №" + wagon.getNumber());

        List<Integer> listPlaces = wagon.getAvailablePlaces();
        if (wagon.getTypeCode().equalsIgnoreCase(Constants.TYPE_KUPE)) {
            WagonKupeAdapter adapter = new WagonKupeAdapter(wagon, listPlaces, getActivity());
            wagonRecyclerView.setAdapter(adapter);
        } else if(wagon.getTypeCode().equalsIgnoreCase(Constants.TYPE_LUX)){
            WagonLuxAdapter adapter = new WagonLuxAdapter(wagon, listPlaces, getActivity());
            wagonRecyclerView.setAdapter(adapter);
        } else if(wagon.getTypeCode().equalsIgnoreCase(Constants.TYPE_ECONOMY)){
            WagonPlatskartAdapter adapter = new WagonPlatskartAdapter(wagon, listPlaces, getActivity());
            wagonRecyclerView.setAdapter(adapter);
        }
//
//
//        for (int i = 0; i < Constants.SECTION; i++) {
//            if (wagon.getTypeCode().equalsIgnoreCase(Constants.TYPE_LUX)) {
//                WagonLuxView wagonLuxView = new WagonLuxView(getContext(), wagonsList.get(position).getTypeCode(),
//                        wagon.getNumber(), wagonsList.get(position).getCost(), departureDate, arrivalDate,
//                        wagon.getClassCode());
//                linearLayout.addView(wagonLuxView);
//                wagonLuxView.initView(listPlaces, i);
//            } else if (wagon.getTypeCode().equalsIgnoreCase(Constants.TYPE_KUPE)) {
//                WagonKupeView wagonKupeView = new WagonKupeView(getContext(),
//                        wagonsList.get(position).getNumber(), wagonsList.get(position).getCost(), departureDate, arrivalDate,
//                        wagonsList.get(position).getClassCode());
//                linearLayout.addView(wagonKupeView);
//                wagonKupeView.initView(listPlaces, i);
//            }
//        }

//        if (listTickets != null) {
//            ticketPlaceCheckedView(listTickets, wagonsList.get(position).getNumber());
//        }
    }

//    /**
//     * @param place
//     * @param wagonNumber Remove place in wagon
//     */
//    public void ticketPlaceRemoveView(int place, String wagonNumber) {
//        Button placesBtn = ((Button) findPlaceInWagonView(place));
//        if (placesBtn != null) {
//            String currentNumber = (wagonsFilterList == null) ? wagonsLists.get(position).getNumber()
//                    : wagonsFilterList.get(position).getNumber();
//            if (placesBtn.getTag() == Integer.valueOf(place) &&
//                    wagonNumber.equals(currentNumber)) {
//                placesBtn.setBackground(CommonUtils.changeBackgroundPlace(getActivity(), placesBtn));
//                placesBtn.setTextColor(CommonUtils.changeTextColorPlace(getActivity(), placesBtn, android.R.color.black));
//            }
//        }
//    }

//    /**
//     * @param places
//     * @param wagonNumber Select place in wagon
//     */
//    public void ticketPlaceCheckedView(List<Ticket> places, String wagonNumber) {
//        for (int i = 0; i < places.size(); ++i) {
//            int placeNumber = Integer.parseInt(places.get(i).getPlaceNumber());
//            Button placesBtn = ((Button) findPlaceInWagonView(placeNumber));
//            if (placesBtn != null) {
//                if (placesBtn.getTag() == Integer.valueOf(placeNumber) && places.get(i).getWagonNumber().equals(wagonNumber)) {
//                    placesBtn.setBackground(CommonUtils.changeBackgroundPlace(getActivity(), placesBtn));
//                    placesBtn.setTextColor(CommonUtils.changeTextColorPlace(getActivity(), placesBtn, android.R.color.black));
//                }
//            }
//        }
//    }
//
//    /**
//     * @param place
//     * @return Get view button - fragment_wagon_places.layout
//     */
//    public View findPlaceInWagonView(int place) {
//        for (int i = 0; i < Constants.SECTION; i++) {
//            // content in view
//            for (int index = 0; index < (linearLayout).getChildCount(); ++index) {
//                LinearLayout linearLayoutChildAt = (LinearLayout) linearLayout.getChildAt(index);
//                for (int index2 = 0; index2 < linearLayoutChildAt.getChildCount(); ++index2) {
//                    RelativeLayout relativeLayoutChildAt = (RelativeLayout) linearLayoutChildAt.getChildAt(index2);
//                    for (int index3 = 0; index3 < relativeLayoutChildAt.getChildCount(); ++index3) {
//                        RelativeLayout relativeLayoutChildAt2 = (RelativeLayout) relativeLayoutChildAt.getChildAt(index3);
//                        for (int index4 = 0; index4 < relativeLayoutChildAt2.getChildCount(); ++index4) {
//                            View viewChildAt2 = relativeLayoutChildAt2.getChildAt(index4);
//                            if (viewChildAt2 instanceof Button) {
//                                if (viewChildAt2.getTag() == Integer.valueOf(place)) {
//                                    return viewChildAt2;
//                                }
//                            } else {
//                                for (int index5 = 0; index5 < relativeLayoutChildAt2.getChildCount(); ++index5) {
//                                    RelativeLayout linearLayoutChildAt2 = (RelativeLayout) relativeLayoutChildAt2.getChildAt(index5);
//                                    for (int index6 = 0; index6 < linearLayoutChildAt2.getChildCount(); ++index6) {
//                                        View viewChildAt3 = linearLayoutChildAt2.getChildAt(index6);
//                                        if (viewChildAt3.getTag() == Integer.valueOf(place)) {
//                                            return viewChildAt3;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }

    /**
     * @param wagonsList Set adapter for list wagon in bottom slide menu
     */
    private void setHorizontalWagonsAdapter(List<Wagon> wagonsList) {
        HorizontalAdapter horizontalAdapter = new HorizontalAdapter(getActivity(), wagonsList);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(linearLayoutManager);
        horizontalRecyclerView.setAdapter(horizontalAdapter);
    }
}
