package me.rik.idlecraft.events;

import me.rik.idlecraft.database.DatabaseConnection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        DatabaseConnection.openConnection(player.getUniqueId());
    }

}
