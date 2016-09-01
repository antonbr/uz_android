package com.uzapp.view.main.purchase.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.booking.Booking;
import com.uzapp.util.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.purchase.PurchasesAdapter;
import com.uzapp.view.main.purchase.model.Document;
import com.uzapp.view.main.wagon.fragment.WagonPlaceFragment;
import com.uzapp.view.main.wagon.model.Ticket;

import java.util.ArrayList;
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

    @BindView(R.id.purchasesList) RecyclerView purchasesList;
//    @BindView(R.id.purchasesListView) ListView purchasesListView;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.toPayBtn) Button toPayBtn;
    @BindView(R.id.backBtn) ImageButton backBtn;

    private Unbinder unbinder;

    private ArrayList listTickets = new ArrayList<>();
    private List<Booking> listBookings = new ArrayList<>();

    private boolean isBooking, isReserve;
    private String stationFromName, stationToName, trainName, train;
    private int stationFromCode, stationToCode;
    private long selectDate;
    private int totalPrice = 0;

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
        setAdapter();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.backBtn)
    void onClickBack() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.toPayBtn)
    void onClickToPay() {
        bookingOrReserveTicket();
//        for (int i = 0; i < purchasesListView.getChildCount(); i++) {
//            TextInputEditText firstNameField = (TextInputEditText) purchasesListView.findViewById(R.id.firstNameField);
//            TextInputEditText lastNameField = (TextInputEditText) purchasesListView.findViewById(R.id.lastNameField);

//            if (firstNameField.getText() != null && lastNameField != null) {
//                isEmptyName = (firstNameField.getText().toString().isEmpty()
//                        && lastNameField.getText().toString().isEmpty());
//                if (isEmptyErrorField(firstNameField, lastNameField)) {
//                    attemptField(firstNameField, lastNameField);

//                }
//            }
//        }


//        for (int i = 0; i < listTickets.size(); i++) {
//            CardView linearLayoutChildAt = (CardView) purchasesList.getChildAt(i);
//            for (int index2 = 0; index2 < linearLayoutChildAt.getChildCount(); ++index2) {
//                LinearLayout linearLayoutChildAt2 = (LinearLayout) linearLayoutChildAt.getChildAt(i);
//                for (int index3 = 0; index3 < linearLayoutChildAt2.getChildCount(); ++index3) {
//                    View viewChildAt = linearLayoutChildAt2.getChildAt(index3);
//                    if (viewChildAt instanceof LinearLayout) {
//                        for (int index4 = 0; index4 < viewChildAt.getChildCount(); ++index4) {
//                            View viewChildAt2 = linearLayoutChildAt3.getChildAt(index4);
//                            if (viewChildAt2 instanceof TextInputLayout) {
//                                Toast.makeText(getActivity(), "FFFF", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    private boolean isEmptyErrorField(TextInputEditText firstNameField, TextInputEditText lastNameField) {
        // Reset errors.
        firstNameField.setError(null);
        lastNameField.setError(null);

        String email = firstNameField.getText().toString();
        String firstSureName = lastNameField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(firstSureName)) {
            firstNameField.setError(getString(R.string.error_field_required));
            focusView = firstNameField;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            lastNameField.setError(getString(R.string.error_field_required));
            focusView = lastNameField;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        return cancel;
    }

    public void initComponents() {
        String title = getString(R.string.prepare_purchase) + " " + listTickets.size() + " " + getString(R.string.tickets);
        toolbarTitle.setText(title);
    }

    private Callback<Booking> bookingCallback = new Callback<Booking>() {

        @Override
        public void onResponse(Call<Booking> call, Response<Booking> response) {
            if (response.isSuccessful()) {
                Booking booking = response.body();
                listBookings.add(booking);

                if (listTickets.size() == listBookings.size()) {
                    ((MainActivity) getActivity()).replaceFragment(PayFragment.newInstance(totalPrice, listBookings), true);
                    totalPrice = 0;
                }
            } else {
                String error = ApiErrorUtil.parseError(response);
                CommonUtils.showMessage(getView(), error);
            }
        }

        @Override
        public void onFailure(Call<Booking> call, Throwable t) {
            Toast.makeText(getActivity(), call.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private void setAdapter() {
        PurchasesAdapter purchasesAdapter = new PurchasesAdapter(getContext(), listTickets, stationFromName, stationToName, trainName);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        purchasesList.setLayoutManager(linearLayoutManager);
        purchasesList.setAdapter(purchasesAdapter);
    }

    private void bookingOrReserveTicket() {
        for (Object ticket : listTickets) {

            totalPrice = ((Ticket) ticket).getTicketPrice() + totalPrice;

            String wagonType = ((Ticket) ticket).getWagonType();
            int wagonClass = ((Ticket) ticket).getWagonClasses();
            int wagonNumber = Integer.parseInt(((Ticket) ticket).getWagonNumber());
            String wagonPlace = String.valueOf(((Ticket) ticket).getPlaceNumber());

            Document document = newInstanceDocument(wagonNumber, Integer.parseInt(wagonPlace), "Ddd", "Fff", "");

            Call<Booking> call = (isBooking) ? ApiManager.getApi(getActivity())
                    .getBooking(train, stationFromCode, stationToCode, wagonType, wagonClass,
                            wagonNumber, selectDate, wagonPlace, document.toString()) : ApiManager.getApi(getActivity())

                    .getReserve(train, stationFromCode, stationToCode, wagonType,
                            wagonClass, wagonNumber, selectDate, wagonPlace, 0, null, document.toString());

            call.enqueue(bookingCallback);
        }
    }

    private Document newInstanceDocument(int wagonNumber, int wagonPlace, String firstName,
                                         String lastName, String passport) {
        Document document = new Document();
        document.setNumber(wagonNumber);
        document.setCountPlace(wagonPlace);
        document.setFirstName(firstName);
        document.setLastName(lastName);
        document.setPassport(passport);
        return document;
    }
}
