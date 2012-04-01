package de.b4f.reservation.events.operations;

import de.b4f.reservation.business.TradingUnit;
import de.b4f.reservation.business.UnitStore;
import de.b4f.reservation.events.EventType;
import de.b4f.reservation.events.UnitEvent;
import de.b4f.reservation.events.operations.Operation;

public class AddUnit implements Operation {

    protected final UnitStore unitStore;

    public AddUnit(final UnitStore unitStore) {
        this.unitStore = unitStore;
    }

    @Override
    public EventType handles() {
        return EventType.ADD;
    }

    @Override
    public void handle(final UnitEvent unitEvent) {
        long unitId = unitEvent.getUnitId();
        if(unitStore.get(unitId) != null){
            unitEvent.setResult(-1);
            return;
        }

        final TradingUnit unit = new TradingUnit(unitId, unitEvent.getOperand());
        unitStore.add(unit);
        unitEvent.setResult(1);
    }
}
