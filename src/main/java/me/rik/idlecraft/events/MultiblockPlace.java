package me.rik.idlecraft.events;

import me.rik.idlecraft.database.MultiblockService;
import me.rik.idlecraft.interfaces.IMultiBlock;
import me.rik.idlecraft.multiblocks.CraftingTable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MultiblockPlace implements Listener
{

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        UUID uuid = e.getPlayer().getUniqueId();

        IMultiBlock multiBlock = null;

        switch (e.getBlock().getType()) {
            case CRAFTING_TABLE -> multiBlock = new CraftingTable(e.getBlock().getLocation(), uuid);
            default -> {}
        }

        if (multiBlock == null) {
            return;
        }

        multiBlock.place();
        MultiblockService.createMultiblock(e.getPlayer().getUniqueId(), multiBlock);
        IMultiBlock.addToPlayerMultiblocks(uuid, multiBlock);

        e.setCancelled(true);
    }



    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        List<IMultiBlock> multiBlocks = IMultiBlock.playerMultiblocks.getOrDefault(uuid, new ArrayList<>());

        e.getPlayer().sendMessage("Found " + multiBlocks.size() + " multiblocks");
        e.getPlayer().sendMessage("Block broken at " + e.getBlock().getLocation().getX() + ", " + e.getBlock().getLocation().getY() + ", " + e.getBlock().getLocation().getZ());

        // Find the first multiBlock that is in bounds using streams
        Optional<IMultiBlock> multiBlockToRemove = multiBlocks.stream()
                .filter(multiBlock -> {
                    // Log the message instead of sending it to the player
                    System.out.println("Checking bounds for " + multiBlock.location + " with scale " + multiBlock.displays.size());
                    return multiBlock.inBounds(e.getBlock().getLocation());
                })
                .findFirst();

        // Destroy the multiBlock if found
        multiBlockToRemove.ifPresent(IMultiBlock::destroy);

        e.setCancelled(true);

    }


}
