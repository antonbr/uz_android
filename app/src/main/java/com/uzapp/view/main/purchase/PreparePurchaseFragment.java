package com.uzapp.view.main.purchase;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uzapp.R;
import com.uzapp.view.main.wagon.fragment.WagonPlaceFragment;
import com.uzapp.view.main.wagon.model.Ticket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreparePurchaseFragment extends Fragment {

    public static final String KEY_TICKET_LIST = "KEY_TICKET_LIST";
    public static final String KEY_IS_BOOKING = "KEY_IS_BOOKING";
    public static final String KEY_IS_RESERVE = "KEY_IS_RESERVE";
    public static final String KEY_STATION_FROM_NAME = "KEY_STATION_FROM_NAME";
    public static final String KEY_STATION_TO_NAME = "KEY_STATION_TO_NAME";
    public static final String KEY_TRAIN_NAME = "KEY_TRAIN_NAME";
    public static final String KEY_TRAIN = "KEY_TRAIN";
    public static final String KEY_STATION_FROM_CODE = "KEY_STATION_FROM_CODE";
    public static final String KEY_STATION_TO_CODE = "KEY_STATION_TO_CODE";

    private SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
//    private SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");

    @BindView(R.id.purchasesList) RecyclerView purchasesList;
    @BindView(R.id.txtTotalTickets) TextView txtTotalTickets;
    @BindView(R.id.txtTotalSum) TextView txtTotalSum;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.toPayBtn) Button toPayBtn;
    @BindView(R.id.backBtn) ImageButton backBtn;
    @BindView(R.id.txtTimerPurchase) TextView txtTimerPurchase;
    @BindView(R.id.progressTime) ProgressBar progressTime;

    private Unbinder unbinder;
    private ProgressCountDownTimer timer;

    private ArrayList listTickets = new ArrayList<>();
    private boolean isBooking, isReserve;
    private String stationFromName, stationToName, trainName, train;
    private int stationFromCode, stationToCode;
    private long selectDate;

    public PreparePurchaseFragment() {
        // Required empty public constructor
    }

    public static PreparePurchaseFragment getInstance(List<Ticket> listTickets, boolean isBooking, boolean isReserve,
                                                      String trainName, String stationFrom, String stationTo, String train,
                                                      long selectDate, int stationFromCode, int stationToCode) {
        PreparePurchaseFragment fragment = new PreparePurchaseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_TICKET_LIST, (ArrayList) listTickets);
        bundle.putBoolean(KEY_IS_BOOKING, isBooking);
        bundle.putBoolean(KEY_IS_RESERVE, isReserve);
        bundle.putString(KEY_TRAIN_NAME, trainName);
        bundle.putString(KEY_STATION_FROM_NAME, stationFrom);
        bundle.putString(KEY_STATION_TO_NAME, stationTo);
        bundle.putString(KEY_TRAIN, train);
        bundle.putInt(KEY_STATION_FROM_CODE, stationFromCode);
        bundle.putInt(KEY_STATION_TO_CODE, stationToCode);
        bundle.putLong(WagonPlaceFragment.EXTRA_TRAIN_DATE, selectDate);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listTickets = getArguments().getParcelableArrayList(KEY_TICKET_LIST);
            isBooking = getArguments().getBoolean(KEY_IS_BOOKING);
            isReserve = getArguments().getBoolean(KEY_IS_RESERVE);
            trainName = getArguments().getString(KEY_TRAIN_NAME);
            stationFromName = getArguments().getString(KEY_STATION_FROM_NAME);
            stationToName = getArguments().getString(KEY_STATION_TO_NAME);

            train = getArguments().getString(KEY_TRAIN);
            selectDate = getArguments().getLong(WagonPlaceFragment.EXTRA_TRAIN_DATE);
            stationFromCode= getArguments().getInt(KEY_STATION_FROM_CODE);
            stationToCode = getArguments().getInt(KEY_STATION_TO_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prepare_purchase, container, false);
        unbinder = ButterKnife.bind(this, view);
        initComponents();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        timer.cancel();
    }

    @OnClick(R.id.backBtn)
    void onClickBack() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.toPayBtn)
    void onClickToPay() {
        Toast.makeText(getActivity(), "Reserve: " + isReserve + " Booking: " + isBooking, Toast.LENGTH_SHORT).show();

    }

    private void initComponents() {
        toolbarTitle.setText(getString(R.string.prepare_purchase));
        txtTotalTickets.setText(String.valueOf(listTickets.size()));
        int totalPrice = 0;
        for (Object object : listTickets)
            totalPrice = ((Ticket) object).getTicketPrice() + totalPrice;
        txtTotalSum.setText(String.valueOf(totalPrice));

        setAdapter();
        timer = new ProgressCountDownTimer(90000, 1000);
        timer.start();

//        String wagonType = ((Ticket) listTickets.get(0)).getWagonType();
//        int wagonClass = ((Ticket) listTickets.get(0)).getWagonClasses();
//        int wagonNumber = Integer.parseInt(((Ticket) listTickets.get(0)).getWagonNumber());
//        String wagonPlace = String.valueOf(((Ticket) listTickets.get(0)).getPlaceNumber());

//        String date = dateFormat.format(selectDate);

//        Call<Booking> call = (isBooking) ? ApiManager.getApi(getActivity()).getBooking(train, stationFromCode, stationToCode, wagonType, wagonClass, wagonNumber, selectDate, wagonPlace, null) :
//                ApiManager.getApi(getActivity()).getReserve(train, stationFromCode, stationToCode, wagonType, wagonClass, wagonNumber, selectDate, wagonPlace, 0, null, null);
//        call.enqueue(bookingCallback);
    }

//    private Callback<Booking> bookingCallback = new Callback<Booking>() {
//
//        @Override
//        public void onResponse(Call<Booking> call, Response<Booking> response) {
//            if (response.isSuccessful()) {
//                Booking booking = response.body();
//            } else {
//                String error = ApiErrorUtil.parseError(response);
//                CommonUtils.showMessage(getView(), error);
//            }
//        }
//
//        @Override
//        public void onFailure(Call<Booking> call, Throwable t) {
//            Toast.makeText(getActivity(), call.toString(), Toast.LENGTH_SHORT).show();
//        }
//    };

    private void setAdapter() {
        PurchasesAdapter purchasesAdapter = new PurchasesAdapter(getContext(), listTickets, stationFromName, stationToName, trainName);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        purchasesList.setLayoutManager(linearLayoutManager);
        purchasesList.setAdapter(purchasesAdapter);
    }

    public void updateProgress(long progress) {
        Date time = new Date(progress);
        if (progress < 30000) {
            progressTime.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.progress_bar_style_red));
        }
        if (progressTime != null && txtTimerPurchase != null) {
            progressTime.setProgress((int) (progress / 1000));
            txtTimerPurchase.setText(timeFormat.format(time));
        }
    }

    public class ProgressCountDownTimer extends CountDownTimer {

        public ProgressCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            updateProgress(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            txtTimerPurchase.setText(timeFormat.format(0));
            progressTime.setProgress(0);
//            getActivity().onBackPressed();
            Toast.makeText(getActivity(), "Finish", Toast.LENGTH_SHORT).show();
        }
    }
}
