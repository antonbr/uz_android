package com.uzapp.view.main.purchase.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.uzapp.R;
import com.uzapp.network.ApiErrorUtil;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.booking.Booking;
import com.uzapp.pojo.transportation.Transportation;
import com.uzapp.util.CommonUtils;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.purchase.PaymentTicketView;
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

    public final String TAG = PreparePurchaseFragment.class.getSimpleName();

    public static final String KEY_TICKET_LIST = "KEY_TICKET_LIST";
    public static final String KEY_STATION_FROM_NAME = "KEY_STATION_FROM_NAME";
    public static final String KEY_STATION_TO_NAME = "KEY_STATION_TO_NAME";
    public static final String KEY_TRAIN_NAME = "KEY_TRAIN_NAME";
    public static final String KEY_TRAIN = "KEY_TRAIN";
    public static final String KEY_STATION_FROM_CODE = "KEY_STATION_FROM_CODE";
    public static final String KEY_STATION_TO_CODE = "KEY_STATION_TO_CODE";

    @BindView(R.id.ticketsLinearLayout) LinearLayout ticketsLinearLayout;
    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.toPayBtn) Button toPayBtn;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private Unbinder unbinder;
    private Gson gson = new Gson();

    private ArrayList listTickets = new ArrayList<>();
    private List<Booking> listBookings = new ArrayList<>();
    private List<Transportation> listTransportation = new ArrayList<>();
    private List<String> listBaggage = new ArrayList<>();

    private String stationFromName, stationToName, trainName, train;
    private int stationFromCode, stationToCode;
    private long selectDate;
    private int totalPrice = 0;
    private boolean[] emptyFieldsArray;

    public PreparePurchaseFragment() {
        // Required empty public constructor
    }

    public static PreparePurchaseFragment getInstance(List<Ticket> listTickets, String trainName, String stationFrom, String stationTo, String train,
                                                      long selectDate, int stationFromCode, int stationToCode) {
        PreparePurchaseFragment fragment = new PreparePurchaseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_TICKET_LIST, (ArrayList) listTickets);
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
        initListTickets();
        return view;
    }

    public void initComponents() {
        String title = getString(R.string.prepare_purchase) + " " +
                listTickets.size() + " " + getString(R.string.tickets);
        toolbarTitle.setText(title);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.leftImageBtn)
    void onClickBack() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.toPayBtn)
    void onClickPay() {
        payment();
    }

    public void removePosition(int position) {
        listTickets.remove(position);
        ticketsLinearLayout.removeViewAt(position);
    }

    private void initListTickets() {
        for (Object ticket : listTickets) {
            int dp = CommonUtils.convertDpFromPx(getActivity(), 10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(dp, dp, dp, dp);

            PaymentTicketView ticketView = new PaymentTicketView(getContext());
            ticketsLinearLayout.addView(ticketView, params);
            ticketView.initView((Ticket) ticket, stationFromName, stationToName, trainName);
        }
    }

    private void payment() {
        emptyFieldsArray = new boolean[listTickets.size()];

        boolean isEmptyField = true;

        for (int i = 0; i < listTickets.size(); i++) {
            EditText firstNameEditText = (EditText) ticketsLinearLayout.getChildAt(i).findViewById(R.id.firstNameEditText);
            EditText lastNameEditText = (EditText) ticketsLinearLayout.getChildAt(i).findViewById(R.id.lastNameEditText);
            EditText studentEditText = (EditText) ticketsLinearLayout.getChildAt(i).findViewById(R.id.studentEditText);
            LinearLayout layoutStudentNumber = (LinearLayout) ticketsLinearLayout.getChildAt(i).findViewById(R.id.layoutStudentNumber);
            isEmptyErrorField(i, firstNameEditText, lastNameEditText, studentEditText, layoutStudentNumber);
            setTicketDataToList(i, firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                    studentEditText.getText().toString());
        }

        for (boolean isEmpty : emptyFieldsArray) {
            if (isEmpty) {
                isEmptyField = false;
                break;
            } else {
                isEmptyField = true;
            }
        }

        if (isEmptyField) {
            showProgress(true);
            bookingOrReserveTicket();
        }
    }

    private void isEmptyErrorField(int position, EditText firstNameField, EditText lastNameField,
                                   EditText studentEditText, LinearLayout layoutStudentNumber) {
        // Reset errors.
        firstNameField.setError(null);
        lastNameField.setError(null);
        studentEditText.setError(null);

        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String student = studentEditText.getText().toString();

        boolean cancel = false;

        if (TextUtils.isEmpty(firstName)) {
            firstNameField.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if (TextUtils.isEmpty(lastName)) {
            lastNameField.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if (layoutStudentNumber.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(student)) {
                studentEditText.setError(getString(R.string.error_field_required));
                cancel = true;
            }
        }

        emptyFieldsArray[position] = cancel;
    }

    private void setTicketDataToList(int position, String firstName, String lastName, String privilege) {
        Ticket ticket = (Ticket) listTickets.get(position);
        ticket.setIsBooking(isPressedLeftBtn(position, R.id.btnBuyTicket));
        ticket.setIsReserve(isPressedRightBtn(position, R.id.btnReserveTicket));
        ticket.setFirstName(firstName);
        ticket.setLastName(lastName);
        ticket.setKind(getCheckedParam3Btn(position, false, R.id.btnFull, R.id.btnChild, R.id.btnStudent));
        ticket.setPrivilege(privilege);

        if (isCheckedToggle(position, R.id.toggleBaggage)) {
            listBaggage.add(ticket.getBaggage());
            ticket.setBaggage(getCheckedParam3Btn(position, true, R.id.btnAnimal, R.id.btnEquipment, R.id.btnExcess));
        }

        if (isCheckedToggle(position, R.id.toggleTea)) {
            ticket.setService(getCheckedParam2Btn(position, R.id.btnOneTea, R.id.btnTwoTea));
        }
    }

    private void bookingOrReserveTicket() {
        for (int i = 0; i < listTickets.size(); i++) {
            Ticket ticket = (Ticket) listTickets.get(i);
            totalPrice = (ticket.isBooking()) ? ticket.getTicketPrice() + totalPrice : totalPrice + 17;

            String wagonType = ticket.getWagonType();
            String wagonPlace = ticket.getPlaceNumber();
            int wagonNumber = Integer.parseInt(ticket.getWagonNumber());
            int wagonClass = ticket.getWagonClasses();
            String firstName = ticket.getFirstName();
            String lastName = ticket.getLastName();
            String kind = ticket.getKind();
            String privilege = ticket.getPrivilege();
            String service = ticket.getService();
            int bedding = getBedding(i);

            Document document = (ticket.isBooking()) ?
                    new Document.DocumentBuilder().setNumber(i + 1).setCountPlace(Integer.parseInt(wagonPlace))
                            .setFirstName(firstName).setLastName(lastName).setPassport(privilege).build() :
                    new Document.DocumentBuilder().setNumber(i + 1).setKind(kind).setPrivilege(privilege)
                            .setCountPlace(Integer.parseInt(wagonPlace))
                            .setFirstName(firstName).setLastName(lastName).build();

            List<Document> documentList = new ArrayList<>();
            documentList.add(document);

            Call<Booking> call = (ticket.isBooking()) ?
                    ApiManager.getApi(getActivity()).getBooking(train, stationFromCode, stationToCode, wagonType,
                            wagonClass, wagonNumber, selectDate, wagonPlace, gson.toJson(documentList)) :
                    ApiManager.getApi(getActivity()).getReserve(train, stationFromCode, stationToCode, wagonType,
                            wagonClass, wagonNumber, selectDate, wagonPlace, bedding, service, gson.toJson(documentList));

            call.enqueue(new BookingCallback(i));
        }
    }

    private Callback<Transportation> transportationCallback = new Callback<Transportation>() {

        @Override
        public void onResponse(Call<Transportation> call, Response<Transportation> response) {
            if (response.isSuccessful()) {
                Transportation transportation = response.body();
                listTransportation.add(transportation);
                if (listTickets.size() == listBookings.size()) {
                    if (listBaggage.size() == listTransportation.size()) {
                        goPaymentFragment(listBookings, listTransportation);
                    }
                }
            } else {
                String error = ApiErrorUtil.getErrorMessage(response, getActivity());
                CommonUtils.showSnackbar(getView(), error);
                showProgress(false);
            }
        }

        @Override
        public void onFailure(Call<Transportation> call, Throwable t) {
            Log.e(TAG, call.toString());
            showProgress(false);
            String error = ApiErrorUtil.getErrorMessage(t, getActivity());
            CommonUtils.showSnackbar(getView(), error);
        }
    };

    private void goPaymentFragment(List<Booking> listBookings, List<Transportation> listTransportation) {
        showProgress(false);
        ((MainActivity) getActivity()).replaceFragment(PayFragment.newInstance(totalPrice, listBookings, listTransportation), true);
        totalPrice = 0;
        listBaggage.clear();
    }

    private void showProgress(boolean show) {
        if (progressBar != null && scrollView != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            scrollView.setVisibility(!show ? View.VISIBLE : View.GONE);
        }
    }

    private int getBedding(int position) {
        ToggleButton toggleBed = (ToggleButton) ticketsLinearLayout.getChildAt(position).findViewById(R.id.toggleBed);
        return toggleBed.isChecked() ? 1 : 0;
    }

    private boolean isCheckedToggle(int position, int idToggle) {
        ToggleButton toggleBaggage = (ToggleButton) ticketsLinearLayout.getChildAt(position).findViewById(idToggle);
        return toggleBaggage.isChecked();
    }

    private boolean isPressedLeftBtn(int position, int idLeftBtn) {
        Button leftBtn = (Button) ticketsLinearLayout.getChildAt(position).findViewById(idLeftBtn);
        return CommonUtils.isSelectedPlace(leftBtn,
                ContextCompat.getDrawable(getActivity(), R.drawable.button_pressed_left));
    }

    private boolean isPressedCenterBtn(int position, int idCenterBtn) {
        Button centerBtn = (Button) ticketsLinearLayout.getChildAt(position).findViewById(idCenterBtn);
        return CommonUtils.isSelectedPlace(centerBtn,
                ContextCompat.getDrawable(getActivity(), R.drawable.button_pressed_center));
    }

    private boolean isPressedRightBtn(int position, int idRightBtn) {
        Button rightBtn = (Button) ticketsLinearLayout.getChildAt(position).findViewById(idRightBtn);
        return CommonUtils.isSelectedPlace(rightBtn,
                ContextCompat.getDrawable(getActivity(), R.drawable.button_pressed_right));
    }

    private String getCheckedParam3Btn(int position, boolean isBaggage, int idLeftBtn, int idCenterBtn, int idRightBtn) {
        if (isPressedLeftBtn(position, idLeftBtn)) {
            return isBaggage ? "С" : "full";
        } else if (isPressedCenterBtn(position, idCenterBtn)) {
            return isBaggage ? "А" : "child";
        } else if (isPressedRightBtn(position, idRightBtn)) {
            return isBaggage ? "Б" : "privilege";
        }
        return null;
    }

    private String getCheckedParam2Btn(int position, int idLeftBtn, int idRightBtn) {
        if (isPressedLeftBtn(position, idLeftBtn)) {
            return "Ш";
        } else if (isPressedRightBtn(position, idRightBtn)) {
            return "Ч";
        }
        return null;
    }

    class BookingCallback implements Callback<Booking> {

        public final String TAG = BookingCallback.class.getSimpleName();

        private int position;

        public BookingCallback(int position) {
            this.position = position;
        }

        @Override
        public void onResponse(Call<Booking> call, Response<Booking> response) {
            if (response.isSuccessful()) {
                Booking booking = response.body();
                listBookings.add(booking);
                if (isCheckedToggle(position, R.id.toggleBaggage)) {
                    String baggage = ((Ticket) listTickets.get(position)).getBaggage();
                    Call<Transportation> callTransportation = ApiManager.getApi(getActivity())
                            .getTransportation(baggage, booking.getDocumentsList().get(0).getUid(), "2");
                    callTransportation.enqueue(transportationCallback);
                } else {
                    if (listTickets.size() == listBookings.size()) {
                        if (listBaggage.size() == listTransportation.size()) {
                            goPaymentFragment(listBookings, listTransportation);
                        }
                    }
                }
            } else {
                String error = ApiErrorUtil.getErrorMessage(response, getActivity());
                CommonUtils.showSnackbar(getView(), error);
                showProgress(false);
            }
        }

        @Override
        public void onFailure(Call<Booking> call, Throwable t) {
            Log.e(TAG, call.toString());
            showProgress(false);
            String error = ApiErrorUtil.getErrorMessage(t, getActivity());
            CommonUtils.showSnackbar(getView(), error);
        }
    }
}
