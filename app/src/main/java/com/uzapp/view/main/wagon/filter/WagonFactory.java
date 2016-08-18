package com.uzapp.view.main.wagon.filter;

import android.content.Context;

import com.uzapp.R;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladimir on 16.08.2016.
 */
public class WagonFactory {

    /**
     * @param context
     * @param listWagons
     * @param valueFilter
     * @param typeFilter
     * @return list of type filter
     *
     * use getWagons method to get list of type filter
     */
    public List<Wagon> getWagons(Context context, List<Wagon> listWagons,
                                            String valueFilter, String typeFilter) {
        List<Wagon> listAllWagons = new ArrayList<>();
        listAllWagons.addAll(listWagons);

        if (typeFilter == null) {
            return null;
        }

        if (typeFilter.equalsIgnoreCase(context.getString(R.string.wagon_joint_visit))) {
            return new FilterWagon(context, valueFilter, listAllWagons).getFilterJoinVisitList();

        } else if (typeFilter.equalsIgnoreCase(context.getString(R.string.wagon_location_place))) {
            return new FilterWagon(context, valueFilter, listAllWagons).getSelectPlacesList();

        } else if (typeFilter.equalsIgnoreCase(context.getString(R.string.wagon_upper_low))) {
            return new FilterWagon(context, valueFilter, listAllWagons).getLowUpList();
        }

        return null;
    }
}
