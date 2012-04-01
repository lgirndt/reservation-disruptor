package de.b4f.reservation.infrastructure;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import com.google.common.collect.Lists;
import com.lmax.disruptor.EventHandler;

import de.b4f.reservation.events.UnitEvent;

public class QueuedPipeline implements Pipeline {

    private BlockingQueue<UnitEvent> firstQueue;

    public QueuedPipeline(
            final EventHandler<UnitEvent> businessHandler,
            final EventHandler<UnitEvent> outputHandler) {

        Executor executor = Executors.newFixedThreadPool(2);

        firstQueue = new LinkedBlockingDeque<UnitEvent>(2 << 12);
        final BlockingQueue<UnitEvent> secondQueue = new LinkedBlockingDeque<UnitEvent>();

        executor.execute(new HandlerRunner(firstQueue, businessHandler, secondQueue));
        executor.execute(new HandlerRunner(secondQueue, outputHandler, null));
    }

    @Override
    public void process(final UnitEvent unitEvent) {
        try {
            firstQueue.put(unitEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class HandlerRunner implements Runnable {
        private final BlockingQueue<UnitEvent> in;
        private final EventHandler<UnitEvent> businessHandler;
        private final BlockingQueue<UnitEvent> out;

        public HandlerRunner(
                final BlockingQueue<UnitEvent> in,
                final EventHandler<UnitEvent> businessHandler,
                final BlockingQueue<UnitEvent> out) {

            this.in = in;
            this.businessHandler = businessHandler;
            this.out = out;
        }

        @Override
        public void run() {

            System.out.println("running thread.");

            try {
                List<UnitEvent> events = Lists.newLinkedList();
                while (true) {
                    in.drainTo(events);
                    for (UnitEvent e : events) {
                        businessHandler.onEvent(e, 0l, false);
                        if (out != null) {
                            out.put(e);
                        }
                    }
                    events.clear();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("stopped queue processing");
            }
        }
    }
}
