package com.uzapp.view.main.wagon.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.MainActivity;
import com.uzapp.view.main.wagon.fragment.WagonPlaceFragment;
import com.uzapp.view.main.wagon.model.Ticket;

import java.util.List;

/**
 * Created by vmazur on 23.11.2015.
 */
public class WagonTypeAdapter extends BaseAdapter implements View.OnClickListener {

    public List<Integer> listPlaces;
    public LayoutInflater layoutInflater;
    public Context context;
    private ViewHolder holder = null;
    private FragmentManager manager;
    private String wagonNumber;
    private int priceTicket, departureDate, arrivalDate, wagonClasses;
    private String typeWagon;

    private int placeLowStandardLeft, placeUpperStandardLeft, placeLowStandardRight,
            placeUpperStandardRight, placeLowSide, placeUpperSide;

    public WagonTypeAdapter(Context context, List<Integer> listPlaces, String wagonNumber,
                            int priceTicket, String typeWagon, int departureDate,
                            int arrivalDate, int wagonClasses) {
        this.context = context;
        this.listPlaces = listPlaces;
        this.wagonNumber = wagonNumber;
        this.priceTicket = priceTicket;
        this.typeWagon = typeWagon;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.wagonClasses = wagonClasses;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
        Button btnPlaceUpperSide;
        Button btnPlaceLowSide;
        Button btnPlaceLowStandardLeft;
        Button btnPlaceUpperStandardLeft;
        Button btnPlaceLowStandardRight;
        Button btnPlaceUpperStandardRight;
    }

    @Override
    public int getCount() {
        return listPlaces.size();
    }

    @Override
    public Object getItem(int position) {
        return listPlaces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            if (typeWagon.equalsIgnoreCase(Constants.TYPE_ECONOMY)) {
                view = layoutInflater.inflate(R.layout.item_fragment_econom, parent, false);
                placeLowStandardLeft = 1 + (position * 4);
                placeUpperStandardLeft = 2 + (position * 4);
                placeLowStandardRight = 3 + (position * 4);
                placeUpperStandardRight = 4 + (position * 4);
                placeLowSide = 59 - (position * 2);
                placeUpperSide = 60 - (position * 2);
            } else if (typeWagon.equalsIgnoreCase(Constants.TYPE_KUPE)) {
                view = layoutInflater.inflate(R.layout.item_fragment_kupe, parent, false);
                placeLowStandardLeft = 1 + (position * 4);
                placeUpperStandardLeft = 2 + (position * 4);
                placeLowStandardRight = 3 + (position * 4);
                placeUpperStandardRight = 4 + (position * 4);
            } else if (typeWagon.equalsIgnoreCase(Constants.TYPE_LUX)) {
                view = layoutInflater.inflate(R.layout.item_fragment_lux, parent, false);
                placeLowStandardLeft = 1 + (position * 2);
                placeLowStandardRight = 2 + (position * 2);
            }
            manager = ((MainActivity) context).getSupportFragmentManager();
            holder = new ViewHolder();
        }

        initViewItem(view);

        return view;
    }

    /**
     * @param view
     *
     * Initializing components (button)
     */
    private void initViewItem(View view) {
        holder.btnPlaceUpperSide = (Button) view.findViewById(R.id.btnPlaceUpperSide);
        if (holder.btnPlaceUpperSide != null) {
            setPlaceButton(holder.btnPlaceUpperSide, placeUpperSide, listPlaces.size());
            holder.btnPlaceUpperSide.setOnClickListener(this);
        }
        holder.btnPlaceLowSide = (Button) view.findViewById(R.id.btnPlaceLowSide);
        if (holder.btnPlaceLowSide != null) {
            setPlaceButton(holder.btnPlaceLowSide, placeLowSide, listPlaces.size());
            holder.btnPlaceLowSide.setOnClickListener(this);
        }
        holder.btnPlaceLowStandardLeft = (Button) view.findViewById(R.id.btnPlaceLowStandardLeft);
        if (holder.btnPlaceLowStandardLeft != null) {
            setPlaceButton(holder.btnPlaceLowStandardLeft, placeLowStandardLeft, listPlaces.size());
            holder.btnPlaceLowStandardLeft.setOnClickListener(this);
        }
        holder.btnPlaceUpperStandardLeft = (Button) view.findViewById(R.id.btnPlaceUpperStandardLeft);
        if (holder.btnPlaceUpperStandardLeft != null) {
            setPlaceButton(holder.btnPlaceUpperStandardLeft, placeUpperStandardLeft, listPlaces.size());
            holder.btnPlaceUpperStandardLeft.setOnClickListener(this);
        }
        holder.btnPlaceLowStandardRight = (Button) view.findViewById(R.id.btnPlaceLowStandardRight);
        if (holder.btnPlaceLowStandardRight != null) {
            setPlaceButton(holder.btnPlaceLowStandardRight, placeLowStandardRight, listPlaces.size());
            holder.btnPlaceLowStandardRight.setOnClickListener(this);
        }
        holder.btnPlaceUpperStandardRight = (Button) view.findViewById(R.id.btnPlaceUpperStandardRight);
        if (holder.btnPlaceUpperStandardRight != null) {
            setPlaceButton(holder.btnPlaceUpperStandardRight, placeUpperStandardRight, listPlaces.size());
            holder.btnPlaceUpperStandardRight.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String filterString;
        if (typeWagon.equalsIgnoreCase(Constants.TYPE_LUX)) {
            filterString = context.getString(R.string.filter_bottom);
        } else {
            filterString = (CommonUtils.isOdd((int) button.getTag()))
                    ? context.getString(R.string.filter_bottom) : context.getString(R.string.filter_top);
        }
        setBackgroundBtnPlace(button, filterString);
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
            button.setTextColor(CommonUtils.changeTextColorPlace(context, button, android.R.color.black));
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
        button.setBackground(CommonUtils.changeBackgroundPlace(context, button));
        button.setTextColor(CommonUtils.changeTextColorPlace(context, button, android.R.color.black));

        Ticket ticket = newInstanceTicket(button.getTag().hashCode(), button.getText().toString(), placeType);
        boolean isRemove = (CommonUtils.isSelectedPlace(button, ContextCompat
                .getDrawable(context, R.drawable.border_button_place_selected)));
        WagonPlaceFragment fragment = (WagonPlaceFragment) manager.findFragmentById(R.id.fragmentContainer);
        fragment.setAdapter(ticket, !isRemove);
    }

    private Ticket newInstanceTicket(int id, String placeNumber, String placeType) {
        return new Ticket(id, wagonNumber, placeNumber, placeType, priceTicket, departureDate,
                arrivalDate, wagonClasses,typeWagon);
    }
}
