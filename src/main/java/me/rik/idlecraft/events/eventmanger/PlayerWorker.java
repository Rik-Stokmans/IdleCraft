package me.rik.idlecraft.events.eventmanger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayerWorker implements Runnable {
    private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();

    public void addEvent(Event event) {
        eventQueue.add(event);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Event event = eventQueue.take();
                event.execute();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
