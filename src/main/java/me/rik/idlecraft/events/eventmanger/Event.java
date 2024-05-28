package me.rik.idlecraft.events.eventmanger;

public class Event {
    private final Runnable task;

    public Event(Runnable task) {
        this.task = task;
    }

    public void execute() {
        task.run();
    }
}
