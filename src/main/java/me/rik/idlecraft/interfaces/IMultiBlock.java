package me.rik.idlecraft.interfaces;

import me.rik.idlecraft.database.MultiblockService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public abstract class IMultiBlock
{
    public UUID uuid;
    public Location location;
    public List<Display> displays = new ArrayList<>();
    public Map<ItemStack, Function<InventoryClickEvent, Runnable>> guiActions = new HashMap<>();
    public Inventory inventory;

    public static HashMap<UUID, ArrayList<IMultiBlock>> playerMultiblocks = new HashMap<>();


    public IMultiBlock(Location location, UUID uuid) {
        this.location = location;
        this.uuid = uuid;
        initInventory();
    }

    public static void addToPlayerMultiblocks(UUID uuid, IMultiBlock multiBlock) {
        if (playerMultiblocks.containsKey(uuid)) {
            playerMultiblocks.get(uuid).add(multiBlock);
        } else {
            playerMultiblocks.put(uuid, new ArrayList<>(List.of(multiBlock)));
        }
    }

    public void place() {
        int blockX = location.getBlockX();
        int blockY = location.getBlockY();
        int blockZ = location.getBlockZ();

        int xSize = getXSize();
        int ySize = getYSize();
        int zSize = getZSize();

        for (int x = blockX - (xSize/2); x < blockX + xSize - (xSize/2); x++)
        {
            for (int y = blockY; y < blockY + ySize; y++)
            {
                for (int z = blockZ - (zSize/2); z < blockZ + zSize - (zSize/2); z++)
                {
                    location.getWorld().getBlockAt(x, y, z).setType(Material.BARRIER);
                }
            }
        }

        placeDisplays();
    }

    public abstract void placeDisplays();

    public void destroy() {
        hide();

        playerMultiblocks.get(uuid).remove(this);
        MultiblockService.removeMultiblock(uuid, this);
    }

    public void hide() {
        displays.forEach(display -> {
            Location displayLocation = display.getLocation();

            if (display instanceof BlockDisplay blockDisplay) {
                Vector3f bdScale = blockDisplay.getTransformation().getScale();

                for (int x = (int) displayLocation.getX(); x < displayLocation.getX() + bdScale.x; x++) {
                    for (int y = (int) displayLocation.getY(); y < displayLocation.getY() + bdScale.y; y++) {
                        for (int z = (int) displayLocation.getZ(); z < displayLocation.getZ() + bdScale.z; z++) {
                            displayLocation.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                        }
                    }
                }
            }

            display.remove();
        });
    }

    public boolean inBounds(Location location) {
        for (Display display : displays) {

            Location displayLocation = display.getLocation();

            if (display instanceof BlockDisplay blockDisplay) {
                Vector3f bdScale = blockDisplay.getTransformation().getScale();

                if (location.getX() >= displayLocation.getX() && location.getX() < displayLocation.getX() + bdScale.x) {
                    if (location.getY() >= displayLocation.getY() && location.getY() < displayLocation.getY() + bdScale.y) {
                        if (location.getZ() >= displayLocation.getZ() && location.getZ() < displayLocation.getZ() + bdScale.z) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void interact() {
        Player player = requireNonNull(Bukkit.getPlayer(uuid));

        player.openInventory(inventory);
    }

    public abstract void initGuiActions();

    public abstract Inventory populateInventory(Inventory inventory);

    public void handleInventoryClick(InventoryClickEvent e)
    {
        if (inventory == e.getClickedInventory())
        {
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            guiActions.getOrDefault(clickedItem, inventoryClickEvent -> () -> {}).apply(e).run();
        }
    }

    public void initInventory() {
        initGuiActions();

        inventory = populateInventory(Bukkit.createInventory(null, getInventorySize()));
    }

    public abstract int getType();

    public abstract int getInventorySize();

    public abstract int getXSize();

    public abstract int getYSize();

    public abstract int getZSize();
}
