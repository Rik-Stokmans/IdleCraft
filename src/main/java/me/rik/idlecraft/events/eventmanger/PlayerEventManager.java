package me.rik.idlecraft.events.eventmanger;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerEventManager
{
    private static final ConcurrentHashMap<UUID, PlayerWorker> playerWorkers = new ConcurrentHashMap<>();

    public static void handleEvent(UUID playerId, Event event)
    {
        PlayerWorker worker = playerWorkers.computeIfAbsent(playerId, id ->
        {
            PlayerWorker newWorker = new PlayerWorker();
            Thread thread = new Thread(newWorker);
            thread.start();
            return newWorker;
        });
        worker.addEvent(event);
    }
}
