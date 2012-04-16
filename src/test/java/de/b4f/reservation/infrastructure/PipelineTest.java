package de.b4f.reservation.infrastructure;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.lmax.disruptor.EventHandler;

import de.b4f.reservation.business.UnitStore;
import de.b4f.reservation.events.EventType;
import de.b4f.reservation.events.UnitEvent;
import de.b4f.reservation.events.operations.AddUnit;
import de.b4f.reservation.events.operations.GetInStock;
import de.b4f.reservation.events.operations.Operation;
import de.b4f.reservation.events.operations.Reserve;
import de.b4f.reservation.handlers.ReservationEventHandler;

public class PipelineTest {

    private final int ITERATIONS = 1000 * 1000 * 10;

    private final int MAX_UNITS = 50000;
    private Random random;
    private Pipeline disruptorPipeline;
    private Pipeline queuedPipeline;

    private CountDownLatch latch;

    @Before
    public void setUp() throws Exception {
        random = new Random();

        System.out.println("Benchmark on " + ITERATIONS + " iterations.");;

        latch = new CountDownLatch(ITERATIONS + MAX_UNITS);

        UnitStore unitStore = new UnitStore();
        List<Operation> operations = Lists.newArrayList(
                new Reserve(unitStore),
                new GetInStock(unitStore),
                new AddUnit(unitStore)
        );

        disruptorPipeline = new DisruptorPipeline(new ReservationEventHandler(operations), new LatchCountingHandler(latch));
        queuedPipeline = new QueuedPipeline(new ReservationEventHandler(operations), new LatchCountingHandler(latch));
    }

    @Test
    public void disruptorPerformance() throws InterruptedException {
        System.out.println("Disruptor");
        runTest(disruptorPipeline, latch);
    }

    @Test
    public void queuePerformance() throws InterruptedException {
        System.out.println("Queue");
        runTest(queuedPipeline, latch);
    }

    private void runTest(final Pipeline thePipeline, final CountDownLatch theLatch) throws InterruptedException {
        fillStore(thePipeline);

        long start = System.currentTimeMillis();
        for(long i = 0; i < ITERATIONS; i++){
            thePipeline.process(nextEvent());
        }

        theLatch.await();

        long end = System.currentTimeMillis();
        double duration = (end -start) / 1000.0;
        double tps = (double) ITERATIONS / duration;

        System.out.println("Tx/s: " + (long) tps);
    }

    private void fillStore(final Pipeline pipeline){
        for(long i = 0; i < MAX_UNITS; i++) {
            pipeline.process(new UnitEvent(EventType.ADD, i, 1 + random.nextInt(30)));
        }
    }

    private UnitEvent nextEvent(){
        return randomReservation();
    }

    private UnitEvent randomReservation() {
        long unitId = (long) random.nextInt(MAX_UNITS);
        int quantity = random.nextInt(10);
        return new UnitEvent(EventType.RESERVE,unitId,quantity);
    }

    private static class LatchCountingHandler implements EventHandler<UnitEvent> {

        private final CountDownLatch latch;

        public LatchCountingHandler(final CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onEvent(final UnitEvent event, final long sequence, final boolean endOfBatch) {
            latch.countDown();
        }
    }
}
