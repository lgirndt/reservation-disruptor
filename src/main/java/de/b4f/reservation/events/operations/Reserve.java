package de.b4f.reservation.events.operations;

import de.b4f.reservation.business.TradingUnit;
import de.b4f.reservation.business.UnitStore;
import de.b4f.reservation.events.EventType;
import de.b4f.reservation.events.operations.AbstractUnitOperation;

public class Reserve extends AbstractUnitOperation {

    public Reserve(final UnitStore unitStore) {
        super(unitStore, EventType.RESERVE);
    }

    protected int handleUnit(final TradingUnit unit, final int quantity) {
        return unit.reserve(quantity);
    }
}
