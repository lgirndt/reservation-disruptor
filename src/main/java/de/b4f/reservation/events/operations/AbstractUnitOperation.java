package de.b4f.reservation.events.operations;

import de.b4f.reservation.business.TradingUnit;
import de.b4f.reservation.business.UnitStore;
import de.b4f.reservation.events.EventType;
import de.b4f.reservation.events.UnitEvent;
import de.b4f.reservation.events.operations.Operation;

public abstract class AbstractUnitOperation implements Operation {

    private final UnitStore unitStore;
    protected final EventType eventType;


    public AbstractUnitOperation(final UnitStore unitStore,EventType type) {
        this.unitStore = unitStore;
        this.eventType = type;
    }

    @Override
    public final EventType handles() {
        return eventType;
    }

    @Override
    public final void handle(final UnitEvent unitEvent) {

        long unitId = unitEvent.getUnitId();
        TradingUnit unit = unitStore.get(unitId);

        if(unit == null){
            unitEvent.setResult(0);
            return;
        }

        int quantity = unitEvent.getOperand();
        unitEvent.setResult(handleUnit(unit, quantity));
    }

    protected abstract int handleUnit(final TradingUnit unit, final int quantity);
}
