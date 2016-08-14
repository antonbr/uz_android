package com.uzapp.view.main.wagon.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.view.main.wagon.view.WheelView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FilterWagonFragment extends Fragment {

    @BindView(R.id.layoutJoinVisit)
    LinearLayout layoutJoinVisit;
    @BindView(R.id.layoutLocationPlaces)
    LinearLayout layoutLocationPlaces;
    @BindView(R.id.layoutUpperLower)
    LinearLayout layoutUpperLower;
    @BindView(R.id.txtJoinVisit)
    TextView txtJoinVisit;
    @BindView(R.id.txtLocationPlaces)
    TextView txtLocationPlaces;
    @BindView(R.id.txtUpperLower)
    TextView txtUpperLower;

    private Unbinder unbinder;

    private String titleJoinVisit, titleLocationPlaces, titleUpperLower = null;
    private String valueItem, joinVisitItem, locationPlacesItem, upperLowerItem = null;

    public FilterWagonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        titleJoinVisit = getString(R.string.wagon_joint_visit);
        titleLocationPlaces = context.getString(R.string.wagon_location_place);
        titleUpperLower = getString(R.string.wagon_upper_low);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_wagon, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.layoutJoinVisit)
    void onFilterJoinVisit() {
        showWheelDialog(titleJoinVisit,
                getResources().getStringArray(R.array.filter_join_visit_array));
    }

    @OnClick(R.id.layoutLocationPlaces)
    void onFilterLocationPlaces() {
        showWheelDialog(titleLocationPlaces,
                getResources().getStringArray(R.array.filter_location_places_array));
    }

    @OnClick(R.id.layoutUpperLower)
    void onFilterUpperLower() {
        showWheelDialog(titleUpperLower,
                getResources().getStringArray(R.array.filter_upper_lower_array));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void showWheelDialog(final String title, String[] filter) {
        final String[] value = {null};

        View outerView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_filter_wheel_view, null);
        WheelView wheelView = (WheelView) outerView.findViewById(R.id.wheelView);
        wheelView.setOffset(1);
        wheelView.setItems(Arrays.asList(filter));
        wheelView.setSeletion(3);
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                value[0] = item;
            }
        });

        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(outerView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setFilterData(title, value[0]);
                    }
                }).show();
    }

    private void setFilterData(String title, String value) {
        if (value != null) {
            if (title.equalsIgnoreCase(titleJoinVisit)) {
                txtJoinVisit.setText(value);
                setJoinVisitItem(value);
            } else if (title.equalsIgnoreCase(titleLocationPlaces)) {
                txtLocationPlaces.setText(value);
                setLocationPlacesItem(value);
            } else if (title.equalsIgnoreCase(titleUpperLower)) {
                txtUpperLower.setText(value);
                setUpperLowerItem(value);
            }
        }
    }

    public String getJoinVisitItem() {
        return joinVisitItem;
    }

    public void setJoinVisitItem(String joinVisitItem) {
        this.joinVisitItem = joinVisitItem;
    }

    public String getLocationPlacesItem() {
        return locationPlacesItem;
    }

    public void setLocationPlacesItem(String locationPlacesItem) {
        this.locationPlacesItem = locationPlacesItem;
    }

    public String getUpperLowerItem() {
        return upperLowerItem;
    }

    public void setUpperLowerItem(String upperLowerItem) {
        this.upperLowerItem = upperLowerItem;
    }
}
