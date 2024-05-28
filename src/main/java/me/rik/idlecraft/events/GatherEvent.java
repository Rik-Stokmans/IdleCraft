package me.rik.idlecraft.events;

import me.rik.idlecraft.IdleCraft;
import me.rik.idlecraft.database.BackpackService;
import me.rik.idlecraft.database.ResourceService;
import me.rik.idlecraft.events.eventmanger.Event;
import me.rik.idlecraft.events.eventmanger.PlayerEventManager;
import me.rik.idlecraft.models.Backpack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class GatherEvent implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {

        final Material blockMaterial = e.getBlock().getType();


        ResourceService.gatherableResources.forEach(gr ->
        {
            if (gr.material == blockMaterial)
            {
                Event event = new Event(() ->
                {
                    Player player = e.getPlayer();
                    UUID uuid = player.getUniqueId();

                    Backpack backpack = BackpackService.getBackpack(uuid);

                    int newAmount = backpack.items.getOrDefault(gr.id, 0) + 1;
                    backpack.items.put(gr.id, newAmount);

                    BackpackService.updateBackpack(uuid, gr.id, newAmount);

                    player.sendMessage("you now have " + newAmount + " of item " + gr.id);
                });

                PlayerEventManager.handleEvent(e.getPlayer().getUniqueId(), event);

                e.getBlock().setType(Material.STONE);

                Bukkit.getScheduler().runTaskLater(IdleCraft.plugin, () -> e.getBlock().setType(blockMaterial), 50);
            }
        });

        e.setCancelled(true);
    }
}
