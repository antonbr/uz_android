package com.uzapp.view.main.wagon.filter;

import com.uzapp.view.main.wagon.model.Wagon;

import java.util.List;

/**
 * Created by Vladimir on 16.08.2016.
 */
public abstract class Filter {

    public abstract List<Wagon> getFilterJoinVisitList();
    public abstract List<Wagon> getSelectPlacesList();
    public abstract List<Wagon> getLowUpList();
}
