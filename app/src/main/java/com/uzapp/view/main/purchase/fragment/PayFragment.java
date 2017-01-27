package com.uzapp.view.main.purchase.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.profile.User;
import com.uzapp.pojo.booking.Booking;
import com.uzapp.pojo.booking.Uio;
import com.uzapp.pojo.tickets.TicketsResponse;
import com.uzapp.pojo.transportation.Transportation;
import com.uzapp.network.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.PrefsUtil;
import com.uzapp.view.login.PhoneNumberTextInputEditText;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.purchase.ProgressCountDownTimer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayFragment extends Fragment {

    private static final int CARD_NUMBER_TOTAL_SYMBOLS = 19; // size of pattern 0000 0000 0000 0000
    private static final int CARD_NUMBER_TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int CARD_NUMBER_DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char CARD_NUMBER_DIVIDER = ' ';

    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';

    private static final int CARD_CVC_TOTAL_SYMBOLS = 3;

    public static final String KEY_PRICE_TICKETS = "KEY_PRICE_TICKETS";
    public static final String KEY_LIST_BOOKINGS = "KEY_LIST_BOOKINGS";
    public static final String KEY_LIST_BAGGAGE = "KEY_LIST_BAGGAGE";

    @BindView(R.id.txtTimerPurchase) TextView txtTimerPurchase;
    @BindView(R.id.progressTime) ProgressBar progressTime;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.backBtn) ImageButton backBtn;
    @BindView(R.id.imageViewInfo) ImageView imageViewInfo;
    @BindView(R.id.payBtn) Button payBtn;
    @BindView(R.id.layoutPayment) LinearLayout layoutPayment;
    @BindView(R.id.layoutConfirmPayment) LinearLayout layoutConfirmPayment;
    @BindView(R.id.emailEditText) EditText emailEditText;
    @BindView(R.id.firstSureNameEditText) EditText firstSureNameEditText;
    @BindView(R.id.phoneField) PhoneNumberTextInputEditText phoneField;
    @BindView(R.id.cardNumberEditText) EditText cardNumberEditText;
    @BindView(R.id.cardDateEditText) EditText cardDateEditText;
    @BindView(R.id.cardCVCEditText) EditText cardCVCEditText;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.scrollView) ScrollView scrollView;

    private Unbinder unbinder;
    private ProgressCountDownTimer timer;
    private Realm realm;

    private int priceTickets;
    private String btnText;
    private List<Booking> listBookings;
    private List<Transportation> listTransportation;
    private List<TicketsResponse> listTicketsResponses = new ArrayList<>();
    private int waitTime;

    public PayFragment() {
        // Required empty public constructor
    }

    public static PayFragment newInstance(int priceTickets, List<Booking> listBookings, List<Transportation> listTransportation) {
        PayFragment fragment = new PayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PRICE_TICKETS, priceTickets);
        bundle.putParcelableArrayList(KEY_LIST_BOOKINGS, (ArrayList<? extends Parcelable>) listBookings);
        bundle.putParcelableArrayList(KEY_LIST_BAGGAGE, (ArrayList<? extends Parcelable>) listTransportation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            priceTickets = getArguments().getInt(KEY_PRICE_TICKETS);
            listBookings = getArguments().getParcelableArrayList(KEY_LIST_BOOKINGS);
            listTransportation = getArguments().getParcelableArrayList(KEY_LIST_BAGGAGE);

            if (listBookings != null && !listBookings.isEmpty()) {
                waitTime = listBookings.get(0).getWaitSeconds();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        unbinder = ButterKnife.bind(this, view);
        initComponents();
        return view;
    }

    private void initComponents() {
        String title = getString(R.string.purchase_tickets) + " - " + priceTickets + " " + getString(R.string.ticket_currency);
        btnText = getString(R.string.payment) + " " + priceTickets + " " + getString(R.string.ticket_currency);
        toolbarTitle.setText(title);
        payBtn.setText(btnText);

        setFieldsUserData();
        startTimer();
    }

    private void setFieldsUserData() {
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null) {
            if (user.getEmail() != null) {
                emailEditText.setText(user.getEmail());
            }
            if (user.getFirstName() != null && user.getLastName() != null) {
                String fullName = user.getFirstName() + " " + user.getLastName();
                firstSureNameEditText.setText(fullName);
            }
            if (user.getPhoneNumber() != null) {
                phoneField.setText(user.getPhoneNumber());
            }
        }
    }

    private void startTimer() {
        int time = (waitTime * 1000);
        progressTime.setMax(waitTime);
        timer = new ProgressCountDownTimer(getActivity(),
                time, 1000, progressTime, txtTimerPurchase);
        timer.start();
    }

    private void showLayout(boolean isPayment, boolean isConfirmPayment) {
        layoutPayment.setVisibility(isPayment ? View.VISIBLE : View.GONE);
        layoutConfirmPayment.setVisibility(isConfirmPayment ? View.VISIBLE : View.GONE);
        payBtn.setText(isPayment ? btnText : getString(R.string.confirm_payment));
    }

    @OnClick(R.id.backBtn)
    void onClickBack() {
        if (layoutConfirmPayment.getVisibility() == View.VISIBLE) {
            showLayout(true, false);
        } else {
            getActivity().onBackPressed();
            cancelReserveTicket();
        }
    }

    private void showProgress(boolean show) {
        if (progressBar != null && scrollView != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            scrollView.setVisibility(!show ? View.VISIBLE : View.GONE);
        }
    }

    private void cancelReserveTicket() {
        showProgress(true);
        for (Booking booking : listBookings) {
            Call<Uio> call = ApiManager.getApi(getContext()).cancelReserveTickets(booking.getUio());
            call.enqueue(cancelCallback);
        }
        listBookings.clear();
        listTransportation.clear();
        priceTickets = 0;
    }

    private Callback<Uio> cancelCallback = new Callback<Uio>() {

        @Override
        public void onResponse(Call<Uio> call, Response<Uio> response) {
            if (response.isSuccessful()) {
                Uio uio = response.body();
                showProgress(false);
            } else {
                showProgress(false);
                String error = ApiErrorUtil.getErrorMessage(response, getActivity());
                CommonUtils.showSnackbar(getView(), error);
            }
        }

        @Override
        public void onFailure(Call<Uio> call, Throwable t) {
            Log.d("PayFragment", call.toString());
            showProgress(false);
            String error = ApiErrorUtil.getErrorMessage(t, getActivity());
            CommonUtils.showSnackbar(getView(), error);
        }
    };

    @OnClick(R.id.payBtn)
    void onClickPay() {
        if (payBtn.getText().toString().contains(getString(R.string.payment))) {
            attemptPayment();
        } else {
            attemptConfirmPayment();
        }
    }

    @OnFocusChange(R.id.phoneField)
    void onFieldFocusChanged(boolean focus, TextInputEditText view) {
        if (view.getId() == R.id.phoneField) {
            String phoneText = phoneField.getText().toString();
            if (phoneText.length() == 0 || (phoneText.length() == 1 && phoneText.startsWith("+"))) {
                phoneField.setText(focus ? "+" : "");
            }
        }
    }

    @OnTextChanged(value = R.id.cardNumberEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onCardNumberTextChanged(Editable s) {
        if (!CommonUtils.isInputCorrect(s, CARD_NUMBER_TOTAL_SYMBOLS, CARD_NUMBER_DIVIDER_MODULO, CARD_NUMBER_DIVIDER)) {
            s.replace(0, s.length(), CommonUtils.concatString(CommonUtils.getDigitArray(s, CARD_NUMBER_TOTAL_DIGITS), CARD_NUMBER_DIVIDER_POSITION, CARD_NUMBER_DIVIDER));
        }
    }

    @OnTextChanged(value = R.id.cardDateEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onCardDateTextChanged(Editable s) {
        if (!CommonUtils.isInputCorrect(s, CARD_DATE_TOTAL_SYMBOLS, CARD_DATE_DIVIDER_MODULO, CARD_DATE_DIVIDER)) {
            s.replace(0, s.length(), CommonUtils.concatString(CommonUtils.getDigitArray(s, CARD_DATE_TOTAL_DIGITS), CARD_DATE_DIVIDER_POSITION, CARD_DATE_DIVIDER));
        }
    }

    @OnTextChanged(value = R.id.cardCVCEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onCardCVCTextChanged(Editable s) {
        if (s.length() > CARD_CVC_TOTAL_SYMBOLS) {
            s.delete(CARD_CVC_TOTAL_SYMBOLS, s.length());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        timer.cancel();
        realm.close();
    }

    private void attemptConfirmPayment() {
        // Reset errors.
        emailEditText.setError(null);
        firstSureNameEditText.setError(null);
        String email = emailEditText.getText().toString();
        String firstSureName = firstSureNameEditText.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(firstSureName)) {
            firstSureNameEditText.setError(getString(R.string.error_field_required));
            focusView = firstSureNameEditText;
            cancel = true;
        } else if (CommonUtils.isFullName(firstSureName)) {
            firstSureNameEditText.setError(getString(R.string.error_name_invalid));
            focusView = firstSureNameEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (!CommonUtils.isEmailValid(email)) {
            emailEditText.setError(getString(R.string.error_invalid_email));
            focusView = emailEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            callPayment(firstSureName);
        }
    }

    private void callPayment(String firstSureName) {
        for (Booking booking : listBookings) {
            String cardNumber = (cardNumberEditText.getText().toString()).replace(" ", "");
            int expMonth = Integer.parseInt((cardDateEditText.getText().toString()).substring(0, 2));
            int expYear = Integer.parseInt(20 + (cardDateEditText.getText().toString()).substring(3, 5));
            String cvv = cardCVCEditText.getText().toString();
            String firstName = firstSureName.substring(0, firstSureName.indexOf(" "));
            String lastName = firstSureName.substring(firstSureName.indexOf(" "), firstSureName.length());
            String phone = phoneField.getPhoneNumber();
            String accessToken = PrefsUtil.getStringPreference(getActivity(), PrefsUtil.USER_ACCESS_TOKEN);

            Call<TicketsResponse> call = ApiManager.getApi(getContext()).paymentTickets(booking.getUio(), accessToken,
                    cardNumber, expMonth, expYear, cvv, firstName, lastName, emailEditText.getText().toString(), phone);
            call.enqueue(paymentCallback);
        }
    }

    private Callback<TicketsResponse> paymentCallback = new Callback<TicketsResponse>() {

        @Override
        public void onResponse(Call<TicketsResponse> call, Response<TicketsResponse> response) {
            if (response.isSuccessful()) {
                TicketsResponse ticketsResponse = response.body();
                listTicketsResponses.add(ticketsResponse);
                if (listBookings.size() == listTicketsResponses.size()) {
                    showProgress(false);
                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ((MainActivity) getActivity()).replaceFragment(new PaymentDoneFragment(), true);
                }
            } else {
                showProgress(false);
                String error = ApiErrorUtil.getErrorMessage(response, getActivity());
                CommonUtils.showSnackbar(getView(), error);
            }
        }

        @Override
        public void onFailure(Call<TicketsResponse> call, Throwable t) {
            Log.d("PayFragment", call.toString());
            showProgress(false);
            String error = ApiErrorUtil.getErrorMessage(t, getActivity());
            CommonUtils.showSnackbar(getView(), error);
        }
    };

    private void attemptPayment() {
        // Reset errors.
        cardNumberEditText.setError(null);
        cardDateEditText.setError(null);
        cardCVCEditText.setError(null);

        String cardNumber = cardNumberEditText.getText().toString();
        String cardDate = cardDateEditText.getText().toString();
        String cardCVV = cardCVCEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(cardNumber)) {
            cardNumberEditText.setError(getString(R.string.error_field_required));
            focusView = cardNumberEditText;
            cancel = true;
        } else if (cardNumber.length() < 19) {
            cardNumberEditText.setError(getString(R.string.error_field_short));
            focusView = cardNumberEditText;
            cancel = true;
        }
//        else if (CommonUtils.isVisa(cardNumber)) {
//            cardNumberEditText.setError(getString(R.string.error_card_number_is_invalid));
//            focusView = cardNumberEditText;
//            cancel = true;
//        } else if (CommonUtils.isMasterCard(cardNumber)) {
//            cardNumberEditText.setError(getString(R.string.error_card_number_is_invalid));
//            focusView = cardNumberEditText;
//            cancel = true;
//        }

        if (TextUtils.isEmpty(cardDate)) {
            cardDateEditText.setError(getString(R.string.error_field_required));
            focusView = cardDateEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(cardCVV)) {
            cardCVCEditText.setError(getString(R.string.error_field_required));
            focusView = cardCVCEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showLayout(false, true);
        }
    }
}
