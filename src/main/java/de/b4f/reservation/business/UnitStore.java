package de.b4f.reservation.business;

import java.util.Map;

import com.google.common.collect.Maps;

public class UnitStore {

    private final Map<Long,TradingUnit> units;

    public UnitStore() {
        this.units = Maps.newHashMap();
    }

    public void add(final TradingUnit unit){
        units.put(unit.getId(),unit);
    }

    public TradingUnit get(final long id){
        return units.get(id);
    }
}
