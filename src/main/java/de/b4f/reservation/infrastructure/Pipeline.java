package de.b4f.reservation.infrastructure;

import de.b4f.reservation.events.UnitEvent;

public interface Pipeline {
    void process(UnitEvent unitEvent);
}
