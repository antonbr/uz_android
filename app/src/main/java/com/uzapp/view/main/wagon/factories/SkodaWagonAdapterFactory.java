package com.uzapp.view.main.wagon.factories;

import android.content.Context;

import com.uzapp.view.main.wagon.adapter.SimpleWagonAdapter;
import com.uzapp.view.main.wagon.adapter.skoda.SkodaCafeWagonFirstFloorAdapter;
import com.uzapp.view.main.wagon.adapter.skoda.SkodaFirstLastWagonFirstFloorAdapter;
import com.uzapp.view.main.wagon.adapter.skoda.SkodaSecondWagonFirstFloorAdapter;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

/**
 * Created by viktoria on 2/13/17.
 */
class SkodaWagonAdapterFactory {

   protected static SimpleWagonAdapter getWagonAdapter(Wagon wagon, List<Integer> availablePlaces, Context context,
                                              SimpleWagonAdapter.OnPlaceSelectionListener listener) {
        SimpleWagonAdapter adapter = null;
        int wagonNumber = Integer.valueOf(wagon.getNumber());
        if (wagonNumber == 1 || wagonNumber == 6) {
            adapter = new SkodaFirstLastWagonFirstFloorAdapter(wagon, availablePlaces, context, listener);
        } else if (wagonNumber == 5) {
            adapter = new SkodaCafeWagonFirstFloorAdapter(wagon, availablePlaces, context, listener);
        } else {
            adapter = new SkodaSecondWagonFirstFloorAdapter(wagon, availablePlaces, context, listener);
        }
        //TODO second floor!!!
        return adapter;
    }
}
