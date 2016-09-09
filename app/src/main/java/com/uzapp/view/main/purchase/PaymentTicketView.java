package com.uzapp.view.main.purchase;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.uzapp.R;
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.purchase.fragment.PreparePurchaseFragment;
import com.uzapp.view.main.wagon.model.Ticket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Vladimir on 03.09.2016.
 */
public class PaymentTicketView extends CardView {

    private SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.HOURS_MINUTES_FORTMAT);
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DAY_MONTH_YEAR_FORMAT);

    @BindView(R.id.txtPlaceType) TextView txtPlaceType;
    @BindView(R.id.txtWagonType) TextView txtWagonType;
    @BindView(R.id.txtWagonNumber) TextView txtWagonNumber;
    @BindView(R.id.txtPlaceNumber) TextView txtPlaceNumber;
    @BindView(R.id.txtDispatchDate) TextView txtDispatchDate;
    @BindView(R.id.txtDispatchTime) TextView txtDispatchTime;
    @BindView(R.id.txtArrivalDate) TextView txtArrivalDate;
    @BindView(R.id.txtArrivalTime) TextView txtArrivalTime;
    @BindView(R.id.trainName) TextView trainNameTextView;
    @BindView(R.id.stationFrom) TextView stationFromTextView;
    @BindView(R.id.stationTo) TextView stationToTextView;
    @BindView(R.id.txtDeleteTicket) TextView txtDeleteTicket;
    @BindView(R.id.studentEditText) EditText studentEditText;

    @BindView(R.id.btnBuyTicket) Button btnBuyTicket;
    @BindView(R.id.btnReserveTicket) Button btnReserveTicket;

    @BindView(R.id.btnFull) Button btnFull;
    @BindView(R.id.btnChild) Button btnChild;
    @BindView(R.id.btnStudent) Button btnStudent;

    @BindView(R.id.btnOneTea) Button btnOneTea;
    @BindView(R.id.btnTwoTea) Button btnTwoTea;

    @BindView(R.id.btnAnimal) Button btnAnimal;
    @BindView(R.id.btnEquipment) Button btnEquipment;
    @BindView(R.id.btnExcess) Button btnExcess;

    @BindView(R.id.toggleBed) ToggleButton toggleBed;
    @BindView(R.id.toggleTea) ToggleButton toggleTea;
    @BindView(R.id.toggleBaggage) ToggleButton toggleBaggage;

    @BindView(R.id.layoutBaggage) LinearLayout layoutBaggage;
    @BindView(R.id.layoutTea) LinearLayout layoutTea;
    @BindView(R.id.layoutStudentNumber) LinearLayout layoutStudentNumber;

    public PaymentTicketView(Context context) {
        this(context, null);
    }

    public PaymentTicketView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_fragment_purchase, this, true);
        ButterKnife.bind(this);
        setCardBackgroundColor(Color.WHITE);
    }

    public void initView(Ticket ticket, String stationFrom, String stationTo, String trainName) {
        String price = String.valueOf(ticket.getTicketPrice())
                + " " + getContext().getString(R.string.ticket_currency);
        Date departureDate = new Date(TimeUnit.SECONDS.toMillis(ticket.getDepartureDate()));
        Date arrivalDate = new Date(TimeUnit.SECONDS.toMillis(ticket.getArrivalDate()));

        String wagonType;
        if (ticket.getWagonType().equalsIgnoreCase(Constants.TYPE_ECONOMY)) {
            wagonType = getContext().getString(R.string.wagon_type_berth);
        } else if (ticket.getWagonType().equalsIgnoreCase(Constants.TYPE_KUPE)) {
            wagonType = getContext().getString(R.string.wagon_type_coupe);
        } else {
//        if (ticket.getWagonType().equalsIgnoreCase(Constants.TYPE_LUX)) {
            wagonType = getContext().getString(R.string.wagon_type_suite);
        }

        txtPlaceType.setText(ticket.getPlaceType());
        txtWagonType.setText(wagonType);
        txtWagonNumber.setText(ticket.getWagonNumber());
        txtPlaceNumber.setText(ticket.getPlaceNumber());
        txtDispatchDate.setText(dateFormat.format(departureDate));
        txtDispatchTime.setText(timeFormat.format(departureDate));
        txtArrivalDate.setText(dateFormat.format(arrivalDate));
        txtArrivalTime.setText(timeFormat.format(arrivalDate));

        trainNameTextView.setText(trainName);
        stationFromTextView.setText(stationFrom);
        stationToTextView.setText(stationTo);

        btnBuyTicket.setText("Купить, " + price);
        btnReserveTicket.setText("Резерв, 17 грн");
    }

//    private static final int CARD_NUMBER_TOTAL_SYMBOLS = 11;
//    private static final int CARD_NUMBER_TOTAL_DIGITS = 10; // max numbers of digits in pattern: 0000 x 4
//    private static final int CARD_NUMBER_DIVIDER_MODULO = 3; // means divider position is every 5th symbol beginning with 1
//    private static final int CARD_NUMBER_DIVIDER_POSITION = 2; // means divider position is every 4th symbol beginning with 0
//    private static final char CARD_NUMBER_DIVIDER = ' ';

//    @OnTextChanged(value = R.id.studentEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
//    protected void onStudentNumberTextChanged(Editable s) {
//        if (!CommonUtils.isInputCorrect(s, CARD_NUMBER_TOTAL_SYMBOLS, CARD_NUMBER_DIVIDER_MODULO, CARD_NUMBER_DIVIDER)) {
//            s.replace(0, s.length(), CommonUtils.concatString(CommonUtils.getDigitArray(s, CARD_NUMBER_TOTAL_DIGITS),
//                    CARD_NUMBER_DIVIDER_POSITION, CARD_NUMBER_DIVIDER));
//        }
//    }

    @OnClick(R.id.btnAnimal)
    void onClickAnimalBtn() {
        setBackgroundFullChildAndStudentBtn(btnAnimal, btnEquipment, btnExcess, true, false, false);
    }

    @OnClick(R.id.btnEquipment)
    void onClickEquipmentBtn() {
        setBackgroundFullChildAndStudentBtn(btnAnimal, btnEquipment, btnExcess, false, true, false);
    }

    @OnClick(R.id.btnExcess)
    void onClickExcessBtn() {
        setBackgroundFullChildAndStudentBtn(btnAnimal, btnEquipment, btnExcess, false, false, true);
    }

    @OnClick(R.id.btnOneTea)
    void onClickOneTeaBtn() {
        setBackgroundBuyAndReserveBtn(btnOneTea, btnTwoTea, true, false);
    }

    @OnClick(R.id.btnTwoTea)
    void onClickTwoTeaBtn() {
        setBackgroundBuyAndReserveBtn(btnOneTea, btnTwoTea, false, true);
    }

    @OnClick(R.id.btnFull)
    void onClickFullBtn() {
        setBackgroundFullChildAndStudentBtn(btnFull, btnChild, btnStudent, true, false, false);
        showFieldStudentNumber(layoutStudentNumber, false);
    }

    @OnClick(R.id.btnChild)
    void onClickChildBtn() {
        setBackgroundFullChildAndStudentBtn(btnFull, btnChild, btnStudent, false, true, false);
        showFieldStudentNumber(layoutStudentNumber, false);
    }

    @OnClick(R.id.btnStudent)
    void onClickStudentBtn() {
        setBackgroundFullChildAndStudentBtn(btnFull, btnChild, btnStudent, false, false, true);
        showFieldStudentNumber(layoutStudentNumber, true);
    }

    @OnClick(R.id.btnBuyTicket)
    void onClickBuyTicketBtn() {
        setBackgroundBuyAndReserveBtn(btnBuyTicket, btnReserveTicket, true, false);
    }

    @OnClick(R.id.btnReserveTicket)
    void onClickReserveTicketBtn() {
        setBackgroundBuyAndReserveBtn(btnBuyTicket, btnReserveTicket, false, true);
    }

    @OnClick(R.id.txtDeleteTicket)
    void onClickDeleteTicketTxt() {
        FragmentManager manager = ((MainActivity) getContext()).getSupportFragmentManager();
        PreparePurchaseFragment fragment = (PreparePurchaseFragment) manager.findFragmentById(R.id.fragmentContainer);
        fragment.removePosition(txtDeleteTicket.getVerticalScrollbarPosition());
        fragment.initComponents();
    }

    @OnCheckedChanged(R.id.toggleBed)
    void onCheckedChangedBedToggle() {

    }

    @OnCheckedChanged(R.id.toggleTea)
    void onCheckedChangedTeaToggle() {
        layoutTea.setVisibility(toggleTea.isChecked() ? View.VISIBLE : View.GONE);
    }

    @OnCheckedChanged(R.id.toggleBaggage)
    void onCheckedChangedBaggageToggle() {
        layoutBaggage.setVisibility(toggleBaggage.isChecked() ? View.VISIBLE : View.GONE);
    }

    private void showFieldStudentNumber(LinearLayout layoutStudentNumber, boolean isVisible) {
        layoutStudentNumber.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * Change background Full, Child and Student button on click
     */
    private void setBackgroundFullChildAndStudentBtn(Button btnFull, Button btnChild, Button btnStudent,
                                                     boolean isFull, boolean isChild, boolean isStudent) {
        btnFull.setBackground(isFull ? ContextCompat.getDrawable(getContext(), R.drawable.button_pressed_left) :
                ContextCompat.getDrawable(getContext(), R.drawable.button_enabled_left));
        btnFull.setTextColor(isFull ? ContextCompat.getColor(getContext(), android.R.color.white) :
                ContextCompat.getColor(getContext(), R.color.accentColor));

        btnChild.setBackground(isChild ? ContextCompat.getDrawable(getContext(), R.drawable.button_pressed_center) :
                ContextCompat.getDrawable(getContext(), R.drawable.button_enabled_center));
        btnChild.setTextColor(isChild ? ContextCompat.getColor(getContext(), android.R.color.white) :
                ContextCompat.getColor(getContext(), R.color.accentColor));

        btnStudent.setBackground(isStudent ? ContextCompat.getDrawable(getContext(), R.drawable.button_pressed_right) :
                ContextCompat.getDrawable(getContext(), R.drawable.button_enabled_right));
        btnStudent.setTextColor(isStudent ? ContextCompat.getColor(getContext(), android.R.color.white) :
                ContextCompat.getColor(getContext(), R.color.accentColor));
    }

    /**
     * Change background Buy and Reserve button on click
     */
    private void setBackgroundBuyAndReserveBtn(Button btnBuyTicket, Button btnReserveTicket,
                                               boolean isBuy, boolean isReserve) {
        btnBuyTicket.setBackground(isBuy ? ContextCompat.getDrawable(getContext(), R.drawable.button_pressed_left) :
                ContextCompat.getDrawable(getContext(), R.drawable.button_enabled_left));
        btnBuyTicket.setTextColor(isBuy ? ContextCompat.getColor(getContext(), android.R.color.white) :
                ContextCompat.getColor(getContext(), R.color.accentColor));

        btnReserveTicket.setBackground(isReserve ? ContextCompat.getDrawable(getContext(), R.drawable.button_pressed_right) :
                ContextCompat.getDrawable(getContext(), R.drawable.button_enabled_right));
        btnReserveTicket.setTextColor(isReserve ? ContextCompat.getColor(getContext(), android.R.color.white) :
                ContextCompat.getColor(getContext(), R.color.accentColor));
    }
}
