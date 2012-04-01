package de.b4f.reservation.events.operations;

import de.b4f.reservation.events.EventType;
import de.b4f.reservation.events.UnitEvent;

public interface Operation {

    EventType handles();

    void handle(UnitEvent unitEvent);
}
