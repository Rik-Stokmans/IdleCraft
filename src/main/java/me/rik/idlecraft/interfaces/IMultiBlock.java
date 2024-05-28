package me.rik.idlecraft.interfaces;

import me.rik.idlecraft.database.MultiblockService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class IMultiBlock
{
    public UUID uuid;
    public Location location;
    public List<Display> displays = new ArrayList<>();

    public static HashMap<UUID, ArrayList<IMultiBlock>> playerMultiblocks = new HashMap<>();

    public static void addToPlayerMultiblocks(UUID uuid, IMultiBlock multiBlock) {
        if (playerMultiblocks.containsKey(uuid)) {
            playerMultiblocks.get(uuid).add(multiBlock);
        } else {
            playerMultiblocks.put(uuid, new ArrayList<>(List.of(multiBlock)));
        }
    }

    public IMultiBlock(Location location, UUID uuid) {
        this.location = location;
        this.uuid = uuid;
    }
    public abstract void place();

    public void destroy() {
        //get the bounds of each display and remove the barrier blocks within
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

        playerMultiblocks.get(uuid).remove(this);




        MultiblockService.removeMultiblock(uuid, this);
    }

    public void removeFromWorld() {
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

            System.out.println(displays.size());

            Location displayLocation = display.getLocation();

            if (display instanceof BlockDisplay blockDisplay) {
                Vector3f bdScale = blockDisplay.getTransformation().getScale();

                System.out.println("Checking bounds for " + displayLocation + " with scale " + bdScale);

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

    public abstract int getType();
}
