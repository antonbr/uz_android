package com.uzapp.view.main.wagon.type;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.wagon.fragment.WagonPlaceFragment;
import com.uzapp.view.main.wagon.model.Ticket;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vladimir on 15.09.2016.
 */
public class WagonKupeView extends LinearLayout {

//    @BindView(R.id.btnPlaceUpperSide) Button btnPlaceUpperSide;
//    @BindView(R.id.btnPlaceLowSide) Button btnPlaceLowSide;
    @BindView(R.id.btnPlaceLowStandardLeft) Button btnPlaceLowStandardLeft;
    @BindView(R.id.btnPlaceUpperStandardLeft) Button btnPlaceUpperStandardLeft;
    @BindView(R.id.btnPlaceLowStandardRight) Button btnPlaceLowStandardRight;
    @BindView(R.id.btnPlaceUpperStandardRight) Button btnPlaceUpperStandardRight;

    private String type, wagonNumber;
    private int priceTicket, departureDate, arrivalDate;
    private String wagonClasses;
    private int placeLowStandardLeft, placeUpperStandardLeft, placeLowStandardRight,
            placeUpperStandardRight, placeLowSide, placeUpperSide;

    public WagonKupeView(Context context, String type, String wagonNumber, int priceTicket,
                         int departureDate, int arrivalDate, String wagonClasses) {
        this(context, null, type, wagonNumber, priceTicket, departureDate, arrivalDate, wagonClasses);
    }

    private void newInstanceData(String type, String wagonNumber, int priceTicket,
                         int departureDate, int arrivalDate, String wagonClasses) {
        this.type = type;
        this.wagonNumber = wagonNumber;
        this.priceTicket = priceTicket;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.wagonClasses = wagonClasses;
    }

    public WagonKupeView(Context context, AttributeSet attrs, String type, String wagonNumber, int priceTicket,
                         int departureDate, int arrivalDate, String wagonClasses) {
        super(context, attrs);
        newInstanceData(type, wagonNumber, priceTicket, departureDate, arrivalDate, wagonClasses);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (type.equalsIgnoreCase(Constants.TYPE_ECONOMY)) {
//            inflater.inflate(R.layout.item_fragment_econom_redesign, this, true);
//        }else
            if (type.equalsIgnoreCase(Constants.TYPE_KUPE)) {
            inflater.inflate(R.layout.item_fragment_kupe_redesign, this, true);
        }
//    else
//            if (type.equalsIgnoreCase(Constants.TYPE_LUX)) {
//            inflater.inflate(R.layout.item_fragment_lux, this, true);
//        }
        ButterKnife.bind(this);
//        setCardBackgroundColor(Color.WHITE);
    }

    public void initView(List<Integer> listPlaces, int position) {
//        if (type.equalsIgnoreCase(Constants.TYPE_ECONOMY)) {
//            initPlaceEconomy(position);
//        } else
            if (type.equalsIgnoreCase(Constants.TYPE_KUPE)) {
            initPlaceKupe(position);
        }
//    else
//        if (type.equalsIgnoreCase(Constants.TYPE_LUX)) {
//            initPlaceLux(position);
//        }

//        if (btnPlaceUpperSide != null) {
//            setPlaceButton(btnPlaceUpperSide, placeUpperSide, listPlaces.size());
//        }
//        if (btnPlaceLowSide != null) {
//            setPlaceButton(btnPlaceLowSide, placeLowSide, listPlaces.size());
//        }
        if (btnPlaceLowStandardLeft != null) {
            setPlaceButton(btnPlaceLowStandardLeft, placeLowStandardLeft, listPlaces.size());
        }
        if (btnPlaceUpperStandardLeft != null) {
            setPlaceButton(btnPlaceUpperStandardLeft, placeUpperStandardLeft, listPlaces.size());
        }
        if (btnPlaceLowStandardRight != null) {
            setPlaceButton(btnPlaceLowStandardRight, placeLowStandardRight, listPlaces.size());
        }
        if (btnPlaceUpperStandardRight != null) {
            setPlaceButton(btnPlaceUpperStandardRight, placeUpperStandardRight, listPlaces.size());
        }
    }

//    @OnClick(R.id.btnPlaceUpperSide)
//    void onClickPlaceUpperSideBtn() {
//        onClickPlace(btnPlaceUpperSide);
//    }
//
//    @OnClick(R.id.btnPlaceLowSide)
//    void onClickPlaceLowSideBtn() {
//        onClickPlace(btnPlaceLowSide);
//    }

    @OnClick(R.id.btnPlaceLowStandardLeft)
    void onClickPlaceLowStandardLeftBtn() {
        onClickPlace(btnPlaceLowStandardLeft);
    }

    @OnClick(R.id.btnPlaceUpperStandardLeft)
    void onClickPlaceUpperStandardLeftBtn() {
        onClickPlace(btnPlaceUpperStandardLeft);
    }

    @OnClick(R.id.btnPlaceLowStandardRight)
    void onClickPlaceLowStandardRightBtn() {
        onClickPlace(btnPlaceLowStandardRight);
    }

    @OnClick(R.id.btnPlaceUpperStandardRight)
    void onClickPlaceUpperStandardRightBtn() {
        onClickPlace(btnPlaceUpperStandardRight);
    }

//    private void initPlaceEconomy(int position) {
//        placeLowStandardLeft = 1 + (position * 4);
//        placeUpperStandardLeft = 2 + (position * 4);
//        placeLowStandardRight = 3 + (position * 4);
//        placeUpperStandardRight = 4 + (position * 4);
//        placeLowSide = 59 - (position * 2);
//        placeUpperSide = 60 - (position * 2);
//    }
//
    private void initPlaceKupe(int position) {
        placeLowStandardLeft = 1 + (position * 4);
        placeUpperStandardLeft = 2 + (position * 4);
        placeLowStandardRight = 3 + (position * 4);
        placeUpperStandardRight = 4 + (position * 4);
    }

//    private void initPlaceLux(int position) {
//        placeLowStandardLeft = 1 + (position * 2);
//        placeLowStandardRight = 2 + (position * 2);
//    }

    public void onClickPlace(Button buttonPlace) {
        String filterString;
        if (type.equalsIgnoreCase(Constants.TYPE_LUX)) {
            filterString = getContext().getString(R.string.filter_bottom);
        } else {
            filterString = (CommonUtils.isOdd((int) buttonPlace.getTag()))
                    ? getContext().getString(R.string.filter_bottom) : getContext().getString(R.string.filter_top);
        }
        setBackgroundBtnPlace(buttonPlace, filterString);
    }

    /**
     * @param button
     * @param place
     * @param sizeFreely
     *
     * Set available place in wagon
     */
    private void setPlaceButton(Button button, int place, int sizeFreely) {
        button.setText(Integer.toString(place));
        button.setTag(place);
        if (isEnabledPlace(button, sizeFreely)) {
            button.setEnabled(true);
            button.setTextColor(CommonUtils.changeTextColorPlace(getContext(), button, android.R.color.black));
        }
    }

    /**
     * @param button
     * @param sizeFreely
     * @return enabled place
     *
     * Is enabled place
     */
    private boolean isEnabledPlace(Button button, int sizeFreely) {
        return (sizeFreely >= (int) button.getTag());
    }

    /**
     * @param button
     * @param placeType
     *
     * Set background button (place)
     */
    private void setBackgroundBtnPlace(Button button, String placeType) {
        button.setBackground(CommonUtils.changeBackgroundPlace(getContext(), button));
        button.setTextColor(CommonUtils.changeTextColorPlace(getContext(), button, android.R.color.black));

        Ticket ticket = newInstanceTicket(wagonNumber, button.getText().toString(),
                placeType, priceTicket, departureDate, arrivalDate, wagonClasses, type);

        boolean isRemove = (CommonUtils.isSelectedPlace(button, ContextCompat
                .getDrawable(getContext(), R.drawable.border_button_place_selected)));

        WagonPlaceFragment fragment = (WagonPlaceFragment) ((MainActivity) getContext())
                .getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        fragment.setAdapter(ticket, !isRemove);
    }

    private Ticket newInstanceTicket(String wagonNumber, String placeNumber, String placeType, int priceTicket,
                                     int departureDate, int arrivalDate, String wagonClasses, String typeWagon) {
        return new Ticket(wagonNumber, placeNumber, placeType, priceTicket, departureDate, arrivalDate, wagonClasses,typeWagon);
    }
}
