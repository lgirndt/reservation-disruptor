package de.b4f.reservation.events.operations;

import de.b4f.reservation.business.TradingUnit;
import de.b4f.reservation.business.UnitStore;
import de.b4f.reservation.events.EventType;
import de.b4f.reservation.events.operations.AbstractUnitOperation;

public class GetInStock extends AbstractUnitOperation {

    public GetInStock(final UnitStore unitStore) {
        super(unitStore, EventType.GET_IN_STOCK);
    }

    @Override
    protected int handleUnit(final TradingUnit unit, final int quantity) {
        return unit.getInStock();
    }
}
