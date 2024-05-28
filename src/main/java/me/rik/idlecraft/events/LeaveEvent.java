package me.rik.idlecraft.events;

import me.rik.idlecraft.database.DatabaseConnection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        DatabaseConnection.closeConnection(player.getUniqueId());
    }
}
