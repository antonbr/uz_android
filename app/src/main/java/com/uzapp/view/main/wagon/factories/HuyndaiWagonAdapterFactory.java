package com.uzapp.view.main.wagon.factories;

import android.content.Context;

import com.uzapp.pojo.WagonClass;
import com.uzapp.view.main.wagon.adapter.SimpleWagonAdapter;
import com.uzapp.view.main.wagon.adapter.hyundai.HyundaiFirstClassAdapter;
import com.uzapp.view.main.wagon.adapter.hyundai.HyundaiSecondClassAdapter;
import com.uzapp.view.main.wagon.adapter.hyundai.HyundaiSecondClassFirstWagonAdapter;
import com.uzapp.view.main.wagon.adapter.hyundai.HyundaiSecondClassKafeAdapter;
import com.uzapp.view.main.wagon.adapter.hyundai.HyundaiSecondClassLastWagonAdapter;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

/**
 * Created by viktoria on 2/13/17.
 */
class HuyndaiWagonAdapterFactory {
    static SimpleWagonAdapter getWagonAdapter(Wagon wagon, List<Integer> availablePlaces, Context context,
                                              SimpleWagonAdapter.OnPlaceSelectionListener listener) {
        SimpleWagonAdapter adapter = null;
        if (wagon.getClassCode().equalsIgnoreCase(WagonClass.FIRST_SEATING.getShortName())) {
            adapter = new HyundaiFirstClassAdapter(wagon, availablePlaces, context, listener);
        } else {
            int wagonNumber = Integer.valueOf(wagon.getNumber());
            if (wagonNumber == 4 || wagonNumber == 6 || wagonNumber == 7) {
                adapter = new HyundaiSecondClassAdapter(wagon, availablePlaces, context, listener);
            } else if (wagonNumber == 3) {
                adapter = new HyundaiSecondClassKafeAdapter(wagon, availablePlaces, context, listener);
            } else if (wagonNumber == 9) {
                adapter = new HyundaiSecondClassLastWagonAdapter(wagon, availablePlaces, context, listener);
            } else if (wagonNumber == 1) {
                adapter = new HyundaiSecondClassFirstWagonAdapter(wagon, availablePlaces, context, listener);
            }
        }
        return adapter;
    }
}
