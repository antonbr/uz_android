package com.uzapp.view.main.wagon.type;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
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
    @BindView(R.id.btnPlaceLowStandardLeft) Button btnPlaceLowStandardLeft;
    @BindView(R.id.btnPlaceUpperStandardLeft) Button btnPlaceUpperStandardLeft;
    @BindView(R.id.btnPlaceLowStandardRight) Button btnPlaceLowStandardRight;
    @BindView(R.id.btnPlaceUpperStandardRight) Button btnPlaceUpperStandardRight;

    private String type, wagonNumber;
    private double priceTicket;
    private int departureDate, arrivalDate;
    private String wagonClasses;
    private int placeLowStandardLeft, placeUpperStandardLeft, placeLowStandardRight,
            placeUpperStandardRight;

    public WagonKupeView(Context context, String wagonNumber, double priceTicket,
                         int departureDate, int arrivalDate, String wagonClasses) {
        this(context, null, wagonNumber, priceTicket, departureDate, arrivalDate, wagonClasses);
    }

    private void newInstanceData( String wagonNumber, double priceTicket,
                                 int departureDate, int arrivalDate, String wagonClasses) {
        this.wagonNumber = wagonNumber;
        this.priceTicket = priceTicket;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.wagonClasses = wagonClasses;
    }

    public WagonKupeView(Context context, AttributeSet attrs, String wagonNumber, double priceTicket,
                         int departureDate, int arrivalDate, String wagonClasses) {
        super(context, attrs);
        newInstanceData(wagonNumber, priceTicket, departureDate, arrivalDate, wagonClasses);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.item_fragment_kupe, this, true);
        ButterKnife.bind(this);
    }

    public void initView(List<Integer> listPlaces, int position) {
            initPlaceKupe(position);
        if (btnPlaceLowStandardLeft != null) {
            setPlaceButton(btnPlaceLowStandardLeft, placeLowStandardLeft, listPlaces);
        }
        if (btnPlaceUpperStandardLeft != null) {
            setPlaceButton(btnPlaceUpperStandardLeft, placeUpperStandardLeft, listPlaces);
        }
        if (btnPlaceLowStandardRight != null) {
            setPlaceButton(btnPlaceLowStandardRight, placeLowStandardRight, listPlaces);
        }
        if (btnPlaceUpperStandardRight != null) {
            setPlaceButton(btnPlaceUpperStandardRight, placeUpperStandardRight, listPlaces);
        }
    }

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

    private void initPlaceKupe(int position) {
        placeLowStandardLeft = 1 + (position * 4);
        placeUpperStandardLeft = 2 + (position * 4);
        placeLowStandardRight = 3 + (position * 4);
        placeUpperStandardRight = 4 + (position * 4);
    }

    public void onClickPlace(Button buttonPlace) {
        String filterString = (CommonUtils.isOdd((int) buttonPlace.getTag()))
                    ? getContext().getString(R.string.filter_bottom) : getContext().getString(R.string.filter_top);
        setBackgroundBtnPlace(buttonPlace, filterString);
    }

    private void setPlaceButton(Button button, int place, List<Integer> freePlaces) {
        button.setText(Integer.toString(place));
        button.setTag(place);
        if (isEnabledPlace(button, freePlaces)) {
            button.setEnabled(true);
            button.setTextColor(CommonUtils.changeTextColorPlace(getContext(), button, android.R.color.black));
        }
    }

    private boolean isEnabledPlace(Button button, List<Integer> freePlaces) {
        return (freePlaces.contains((int) button.getTag()));
    }

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

    private Ticket newInstanceTicket(String wagonNumber, String placeNumber, String placeType, double priceTicket,
                                     int departureDate, int arrivalDate, String wagonClasses, String typeWagon) {
        return new Ticket(wagonNumber, placeNumber, placeType, priceTicket, departureDate, arrivalDate, wagonClasses, typeWagon);
    }
}
