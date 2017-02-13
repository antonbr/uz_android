package com.uzapp.view.main.wagon.factories;

import android.content.Context;

import com.uzapp.pojo.TrainModel;
import com.uzapp.pojo.WagonType;
import com.uzapp.view.main.wagon.adapter.SimpleWagonAdapter;
import com.uzapp.view.main.wagon.adapter.lying.WagonKupeAdapter;
import com.uzapp.view.main.wagon.adapter.lying.WagonLuxAdapter;
import com.uzapp.view.main.wagon.adapter.lying.WagonPlatskartAdapter;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

/**
 * Created by viktoria on 2/3/17.
 */

public class WagonAdapterFactory {

    public static SimpleWagonAdapter getWagonAdapter(Wagon wagon, TrainModel trainModel, List<Integer> availablePlaces, Context context,
                                                     SimpleWagonAdapter.OnPlaceSelectionListener listener) {
        SimpleWagonAdapter adapter = null;
        if (wagon.getTypeCode().equalsIgnoreCase(WagonType.COUPE.getShortName())) {
            adapter = new WagonKupeAdapter(wagon, availablePlaces, context, listener);
        } else if (wagon.getTypeCode().equalsIgnoreCase(WagonType.SUITE.getShortName())) {
            adapter = new WagonLuxAdapter(wagon, availablePlaces, context, listener);
        } else if (wagon.getTypeCode().equalsIgnoreCase(WagonType.PLATSKART.getShortName())) {
            adapter = new WagonPlatskartAdapter(wagon, availablePlaces, context, listener);
        } else if (trainModel == TrainModel.HYUNDAI) {
            adapter = HuyndaiWagonAdapterFactory.getWagonAdapter(wagon, availablePlaces, context, listener);
        } else if (trainModel == TrainModel.EKP1) { //tarpan
            adapter = TarpanWagonAdapterFactory.getWagonAdapter(wagon, availablePlaces, context, listener);
        } else if (trainModel == TrainModel.SKODA) {
            adapter = SkodaWagonAdapterFactory.getWagonAdapter(wagon, availablePlaces, context, listener);
        }
        return adapter;
    }
}
