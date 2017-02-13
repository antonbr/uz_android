package com.uzapp.view.main.wagon.factories;

import android.content.Context;

import com.uzapp.pojo.WagonClass;
import com.uzapp.view.main.wagon.adapter.SimpleWagonAdapter;
import com.uzapp.view.main.wagon.adapter.tarpan.TarpanFirstClassAdapter;
import com.uzapp.view.main.wagon.adapter.tarpan.TarpanSecondClassAdapter;
import com.uzapp.view.main.wagon.adapter.tarpan.TarpanSecondClassCafeAdapter;
import com.uzapp.view.main.wagon.adapter.tarpan.TarpanSecondClassEconomAdapter;
import com.uzapp.view.main.wagon.adapter.tarpan.TarpanSecondClassFirstLastWagonAdapter;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

/**
 * Created by viktoria on 2/13/17.
 */
class TarpanWagonAdapterFactory {
    static SimpleWagonAdapter getWagonAdapter(Wagon wagon, List<Integer> availablePlaces, Context context,
                                              SimpleWagonAdapter.OnPlaceSelectionListener listener) {
        SimpleWagonAdapter adapter = null;
        if (wagon.getClassCode().equalsIgnoreCase(WagonClass.FIRST_SEATING.getShortName())) {
            adapter = new TarpanFirstClassAdapter(wagon, availablePlaces, context, listener);
        } else {
            int wagonNumber = Integer.valueOf(wagon.getNumber());
            //todo
            if (wagonNumber == 3 || wagonNumber == 6 || wagonNumber == 7) {
                adapter = new TarpanSecondClassAdapter(wagon, availablePlaces, context, listener);
            } else if (wagonNumber == 4) {
                adapter = new TarpanSecondClassEconomAdapter(wagon, availablePlaces, context, listener);
            } else if (wagonNumber == 1 || wagonNumber == 9) {
                adapter = new TarpanSecondClassFirstLastWagonAdapter(wagon, availablePlaces, context, listener);
            } else if (wagonNumber == 5) {
                adapter = new TarpanSecondClassCafeAdapter(wagon, availablePlaces, context, listener);
            }
        }
        return adapter;
    }
}
