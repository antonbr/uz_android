package com.uzapp.view.main.wagon.adapter;

import android.content.Context;

import com.uzapp.pojo.TrainModel;
import com.uzapp.pojo.WagonClass;
import com.uzapp.util.Constants;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

/**
 * Created by viktoria on 2/3/17.
 */

public class WagonAdapterFactory {

    public static SimpleWagonAdapter getWagonAdapter(Wagon wagon, TrainModel trainModel, List<Integer> availablePlaces, Context context,
                                                     SimpleWagonAdapter.OnPlaceSelectionListener listener) {
        //todo refactor type code
        SimpleWagonAdapter adapter = null;
        if (wagon.getTypeCode().equalsIgnoreCase(Constants.TYPE_KUPE)) {
            adapter = new WagonKupeAdapter(wagon, availablePlaces, context, listener);
        } else if (wagon.getTypeCode().equalsIgnoreCase(Constants.TYPE_LUX)) {
            adapter = new WagonLuxAdapter(wagon, availablePlaces, context, listener);
        } else if (wagon.getTypeCode().equalsIgnoreCase(Constants.TYPE_ECONOMY)) {
            adapter = new WagonPlatskartAdapter(wagon, availablePlaces, context, listener);
        } else if (trainModel == TrainModel.HYUNDAI) {
            //todo create other factory
            if (wagon.getClassCode().equalsIgnoreCase(WagonClass.FIRST_SEATING.getShortName())) {
                adapter = new HyundaiFirstClassAdapter(wagon, availablePlaces, context, listener);
            }
        }
        return adapter;
    }
}
