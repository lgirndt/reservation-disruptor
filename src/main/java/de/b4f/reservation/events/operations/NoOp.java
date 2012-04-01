package de.b4f.reservation.events.operations;

import de.b4f.reservation.events.EventType;
import de.b4f.reservation.events.UnitEvent;
import de.b4f.reservation.events.operations.Operation;

public class NoOp implements Operation {

    @Override
    public EventType handles() {
        return null;
    }

    @Override
    public void handle(final UnitEvent unitEvent) {

    }
}
