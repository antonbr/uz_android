package com.uzapp.view.main.wagon.filter;

import android.content.Context;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.main.wagon.model.Wagon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vladimir on 16.08.2016.
 */
public class FilterWagon extends Filter {

    private Context context;
    private String valueFilter;
    private List<Wagon> listWagons;

    public FilterWagon(Context context, String valueFilter, List<Wagon> listWagons) {
        this.context = context;
        this.valueFilter = valueFilter;
        this.listWagons = listWagons;
    }

    /**
     * @return sort list -> filter join visit
     */
    @Override
    public List<Wagon> getFilterJoinVisitList() {
        Iterator<Wagon> iterator = listWagons.iterator();
        List<Integer> placesSection = new ArrayList<>();
        List<Integer> places = new ArrayList<>();
        if (valueFilter.equalsIgnoreCase(context.getString(R.string.filter_two_people))) {
            while (iterator.hasNext()) {
                Wagon itemList = iterator.next();
                for (int i = 0; i < 10; i++) {
                    for (int j = (1 + (i * 2)); j < (3 + (i * 2)); j++)
                        if (itemList.getAvailablePlaces().size() >= j) {
                            if (itemList.getAvailablePlaces().get(j - 1) == j) {
                                placesSection.add(j - 1, itemList.getAvailablePlaces().get(j - 1));
                            }
                            if (placesSection.size() == 2) {
                                places.addAll(placesSection);
                            }
                        }
                }
                if (places.size() < 2) {
                    iterator.remove();
                }
            }

        } else if (valueFilter.equalsIgnoreCase(context.getString(R.string.filter_four_people))) {
            while (iterator.hasNext()) {
                Wagon itemList = iterator.next();
                if (itemList.getTypeCode().equalsIgnoreCase(Constants.TYPE_LUX)) {
                    iterator.remove();
                } else {
                    for (int i = 0; i < 10; i++) {
                        for (int j = (1 + (i * 4)); j < (5 + (i * 4)); j++)
                            if (itemList.getAvailablePlaces().size() >= j) {
                                if (itemList.getAvailablePlaces().get(j - 1) == j) {
                                    placesSection.add(j - 1, itemList.getAvailablePlaces().get(j - 1));
                                }
                                if (placesSection.size() == 4) {
                                    places.addAll(placesSection);
                                }
                            }
                    }
                    if (places.size() < 4) {
                        iterator.remove();
                    }
                }
            }
        }
        return listWagons;
    }

    /**
     * @return sort list -> filter select place
     */
    @Override
    public List<Wagon> getSelectPlacesList() {
        Iterator<Wagon> iterator = listWagons.iterator();
        List<Integer> places = new ArrayList<>();
        if (valueFilter.equalsIgnoreCase(context.getString(R.string.filter_standard))) {
            while (iterator.hasNext()) {
                Wagon itemList = iterator.next();
                for (int i = 0; i < itemList.getAvailablePlaces().size(); i++)
                    if (itemList.getAvailablePlaces().get(i) < 40) {
                        places.add(itemList.getAvailablePlaces().get(i));
                    }
                if (places.isEmpty()) {
                    iterator.remove();
                }
            }

        } else if (valueFilter.equalsIgnoreCase(context.getString(R.string.filter_lateral))) {
            while (iterator.hasNext()) {
                Wagon itemList = iterator.next();
                if (itemList.getTypeCode().equalsIgnoreCase(Constants.TYPE_KUPE)
                        || itemList.getTypeCode().equalsIgnoreCase(Constants.TYPE_LUX)) {
                    iterator.remove();
                } else {
                    for (int i = 0; i < itemList.getAvailablePlaces().size(); i++)
                        if (itemList.getAvailablePlaces().get(i) > 40) {
                            places.add(itemList.getAvailablePlaces().get(i));
                        }
                    if (places.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
        }
        return listWagons;
    }

    /**
     * @return sort list -> filter up low place
     */
    @Override
    public List<Wagon> getLowUpList() {
        Iterator<Wagon> iterator = listWagons.iterator();
        List<Integer> places = new ArrayList<>();
        if (valueFilter.equalsIgnoreCase(context.getString(R.string.filter_bottom))) {
            while (iterator.hasNext()) {
                Wagon itemList = iterator.next();
                for (int i = 0; i < itemList.getAvailablePlaces().size(); i++)
                    if (CommonUtils.isOdd(itemList.getAvailablePlaces().get(i))) {
                        places.add(itemList.getAvailablePlaces().get(i));
                    }
                if (places.isEmpty()) {
                    iterator.remove();
                }
            }

        } else if (valueFilter.equalsIgnoreCase(context.getString(R.string.filter_top))) {
            while (iterator.hasNext()) {
                Wagon itemList = iterator.next();
                if (itemList.getTypeCode().equalsIgnoreCase(Constants.TYPE_LUX)) {
                    iterator.remove();
                } else {
                    for (int i = 0; i < itemList.getAvailablePlaces().size(); i++)
                        if (!CommonUtils.isOdd(itemList.getAvailablePlaces().get(i))) {
                            places.add(itemList.getAvailablePlaces().get(i));
                        }
                    if (places.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
        }
        return listWagons;
    }
}
