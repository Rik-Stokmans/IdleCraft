package me.rik.idlecraft.events;

import me.rik.idlecraft.database.UserDataService;
import me.rik.idlecraft.events.eventmanger.Event;
import me.rik.idlecraft.events.eventmanger.PlayerEventManager;
import me.rik.idlecraft.models.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class GatherEvent implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        Event event = new Event(() -> {
            Player player = e.getPlayer();
            UUID uuid = player.getUniqueId();

            User user = UserDataService.getUser(uuid);

            user.money += 0.25;
            UserDataService.updateUser(user);


            player.sendMessage("you have " + user.money + " money");
        });

        PlayerEventManager.handleEvent(e.getPlayer().getUniqueId(), event);
    }
}
