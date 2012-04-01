package de.b4f.reservation.handlers;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.lmax.disruptor.EventHandler;

import de.b4f.reservation.events.EventType;
import de.b4f.reservation.events.UnitEvent;
import de.b4f.reservation.events.operations.Operation;

public class ReservationEventHandler implements EventHandler<UnitEvent>{

    private final Map<EventType,Operation> operations;

    public ReservationEventHandler(final Iterable<Operation> operations) {
        this.operations = Maps.uniqueIndex(operations,new Function<Operation, EventType>() {
            @Override
            public EventType apply(@Nullable final Operation operation) {
                return operation.handles();
            }
        });
    }


    @Override
    public void onEvent(final UnitEvent event, final long sequence, final boolean endOfBatch) {
        Operation op = operations.get(event.getType());

//        System.out.println("BUSINESS Seq." + sequence + ", Handle Event " + event);

        if(op != null){
            op.handle(event);
        }
    }
}
