package me.rik.idlecraft.events;

import me.rik.idlecraft.IdleCraft;
import me.rik.idlecraft.database.BackpackService;
import me.rik.idlecraft.database.ResourceService;
import me.rik.idlecraft.events.eventmanger.Event;
import me.rik.idlecraft.events.eventmanger.PlayerEventManager;
import me.rik.idlecraft.models.Backpack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class GatherEvent implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        final Material blockMaterial = e.getBlock().getType();
        final UUID uuid = e.getPlayer().getUniqueId();


        int gatherableResourcesId = ResourceService.inGatherableResources(blockMaterial);
        if (gatherableResourcesId == -1 ) return;

        Event event = new Event(() ->
        {
            Backpack backpack = BackpackService.getBackpack(uuid);

            int newAmount = backpack.items.getOrDefault(gatherableResourcesId, 0) + 1;

            backpack.items.put(gatherableResourcesId, newAmount);
            BackpackService.updateBackpack(uuid, gatherableResourcesId, newAmount);

            e.getPlayer().sendMessage("You now have " + newAmount + " " + blockMaterial + " in your backpack.");
        });

        PlayerEventManager.handleEvent(uuid, event);

        e.getBlock().setType(Material.STONE);

        Bukkit.getScheduler().runTaskLater(IdleCraft.plugin, () -> e.getBlock().setType(blockMaterial), 50);
    }

    @EventHandler
    public void onBlockBreakCancel(BlockBreakEvent e)
    {
        //if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;

        e.setCancelled(true);
    }
}
