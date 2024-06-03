package me.rik.idlecraft.events;

import me.rik.idlecraft.database.MultiblockService;
import me.rik.idlecraft.interfaces.IMultiBlock;
import me.rik.idlecraft.multiblocks.CraftingTable;
import me.rik.idlecraft.multiblocks.RobotArm;
import me.rik.idlecraft.multiblocks.StorageInterface;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class MultiblockListener implements Listener
{

    @EventHandler
    public void onMultiblockPlace(BlockPlaceEvent e)
    {
        Location location = e.getBlock().getLocation();
        UUID uuid = e.getPlayer().getUniqueId();

        IMultiBlock multiBlock = null;

        switch (e.getBlock().getType()) {
            case CRAFTING_TABLE -> multiBlock = new CraftingTable(location, uuid);
            case OBSERVER -> multiBlock = new RobotArm(location, uuid);
            case BARREL -> multiBlock = new StorageInterface(location, uuid);
            default -> {}
        }

        if (multiBlock == null) {
            return;
        }

        e.getPlayer().sendMessage("Placing multiblock: " + multiBlock.getType());

        multiBlock.place();
        MultiblockService.createMultiblock(e.getPlayer().getUniqueId(), multiBlock);
        IMultiBlock.addToPlayerMultiblocks(uuid, multiBlock);

        e.setCancelled(true);
    }


    @EventHandler
    public void onMultiblockBreak(BlockBreakEvent e)
    {
        UUID uuid = e.getPlayer().getUniqueId();

        List<IMultiBlock> multiBlocks = IMultiBlock.playerMultiblocks.getOrDefault(uuid, new ArrayList<>());

        e.getPlayer().sendMessage("Found " + multiBlocks.size() + " multiblocks");

        // Find the first multiBlock that is in bounds using streams
        Optional<IMultiBlock> multiBlockToRemove = multiBlocks.stream()
                .filter(multiBlock -> multiBlock.inBounds(e.getBlock().getLocation()))
                .findFirst();

        // Destroy the multiBlock if found and cancel the event
        multiBlockToRemove.ifPresent(IMultiBlock::destroy);

        e.setCancelled(true);

    }


    @EventHandler
    public void onMultiblockInteract(PlayerInteractEvent e)
    {
        if (!e.getAction().isRightClick()) return;

        if (e.getClickedBlock() == null || e.getClickedBlock().getType() != Material.BARRIER || Objects.requireNonNull(e.getHand()).ordinal() == 1) return;

        Location location = e.getClickedBlock().getLocation();
        UUID uuid = e.getPlayer().getUniqueId();

        List<IMultiBlock> multiBlocks = IMultiBlock.playerMultiblocks.getOrDefault(uuid, new ArrayList<>());

        // Find the first multiBlock that is in bounds using streams
        Optional<IMultiBlock> multiBlockToInteract = multiBlocks.stream()
                .filter(multiBlock -> multiBlock.inBounds(location))
                .findFirst();

        // Destroy the multiBlock if found
        multiBlockToInteract.ifPresent(IMultiBlock::interact);
    }


    @EventHandler
    public void unMultiblockInventoryClick(InventoryClickEvent e)
    {
        UUID uuid = e.getWhoClicked().getUniqueId();

        List<IMultiBlock> multiBlocks = IMultiBlock.playerMultiblocks.getOrDefault(uuid, new ArrayList<>());

        multiBlocks.forEach(multiBlock -> multiBlock.handleInventoryClick(e));
    }


























}
