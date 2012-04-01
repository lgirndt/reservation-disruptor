package de.b4f.reservation.infrastructure;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.SingleThreadedClaimStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;

import de.b4f.reservation.events.UnitEvent;

public class DisruptorPipeline implements Pipeline {

    private final int RING_SIZE = 2 << 12;
    private final int NUMBER_OF_HANDLERS = 2;

    private final Executor executor;
    private Disruptor<UnitEvent> disruptor;

    public DisruptorPipeline(
            final EventHandler<UnitEvent> businessHandler,
            final EventHandler<UnitEvent> outputHandler) {

        this.executor = Executors.newFixedThreadPool(NUMBER_OF_HANDLERS);

        disruptor = new Disruptor<UnitEvent>(UnitEvent.FACTORY,
                executor,
                new SingleThreadedClaimStrategy(RING_SIZE),
                new SleepingWaitStrategy());

        disruptor.handleEventsWith(businessHandler).then(outputHandler);
        disruptor.start();
    }

    @Override
    public void process(final UnitEvent unitEvent) {
        disruptor.publishEvent(new EventTranslator<UnitEvent>() {
            @Override
            public UnitEvent translateTo(final UnitEvent event, final long sequence) {
                event.assignFrom(unitEvent);
                return event;
            }
        });
    }
}
