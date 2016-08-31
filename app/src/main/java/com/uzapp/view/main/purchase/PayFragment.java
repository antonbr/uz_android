package com.uzapp.view.main.purchase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

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
    @BindView(R.id.cardNumberEditText) EditText cardNumberEditText;
    @BindView(R.id.cardDateEditText) EditText cardDateEditText;
    @BindView(R.id.cardCVCEditText) EditText cardCVCEditText;

    private Unbinder unbinder;
    private ProgressCountDownTimer timer;

    public PayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String title = getString(R.string.purchase_tickets) + ", " + getString(R.string.ticket_currency);
        toolbarTitle.setText(title);

        timer = new ProgressCountDownTimer(getActivity(), 900000, 1000, progressTime, txtTimerPurchase);
        timer.start();
    }

    private void showLayout(boolean isPayment, boolean isConfirmPayment) {
        layoutPayment.setVisibility(isPayment ? View.VISIBLE : View.GONE);
        layoutConfirmPayment.setVisibility(isConfirmPayment ? View.VISIBLE : View.GONE);
        payBtn.setText(isPayment ? getString(R.string.payment) : getString(R.string.confirm_payment));
    }

    @OnClick(R.id.backBtn)
    void onClickBack() {
        if (layoutConfirmPayment.getVisibility() == View.VISIBLE) {
            showLayout(true, false);
        } else {
            getActivity().onBackPressed();
        }
    }

    @OnClick(R.id.payBtn)
    void onClickPay() {
        if (payBtn.getText().toString().contains(getString(R.string.payment))) {
            attemptPayment();
        } else {
            attemptConfirmPayment();
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
            Toast.makeText(getActivity(), "Send request", Toast.LENGTH_SHORT).show();
        }
    }

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
        }

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
