package me.rik.idlecraft.multiblocks;

import me.rik.idlecraft.interfaces.IMultiBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.UUID;

public class StorageInterface extends IMultiBlock
{
    public StorageInterface(Location location, UUID uuid)
    {
        super(location, uuid);
    }


    @Override
    public void placeDisplays()
    {
        int xSize = getXSize();
        int ySize = getYSize();
        int zSize = getZSize();

        ArrayList<BlockDisplay> displayArrayList = new ArrayList<>();

        for (int x = -xSize / 2; x < xSize - xSize / 2; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                for (int z = -zSize / 2; z < zSize - zSize / 2; z++)
                {
                    displayArrayList.add(location.getWorld().spawn(location.clone().add(new Vector(x, y, z)), BlockDisplay.class));
                }
            }
        }

        Transformation transformation = new Transformation
                (
                        new Vector3f(),
                        new AxisAngle4f(),
                        new Vector3f(1, 1, 1),
                        new AxisAngle4f()
                );

        for (BlockDisplay blockDisplay : displayArrayList)
        {
            blockDisplay.setBlock(Material.BARREL.createBlockData());
            blockDisplay.setTransformation(transformation);
            displays.add(blockDisplay);
        }
    }

    @Override
    public void initGuiActions() {
        guiActions.put(new ItemStack(Material.BARRIER, 1), inventoryClickEvent -> () -> {
            inventoryClickEvent.getWhoClicked().sendMessage("You clicked the storage interface!");
        });
    }

    @Override
    public Inventory populateInventory(Inventory inventory)
    {
        inventory.setItem(13, guiActions.keySet().stream().filter(itemStack -> itemStack.getType() == Material.BARRIER).findFirst().orElse(new ItemStack(Material.RED_STAINED_GLASS_PANE, 1)));

        return inventory;
    }

    @Override
    public int getType()
    {
        return 3;
    }

    @Override
    public int getInventorySize()
    {
        return 27;
    }

    @Override
    public int getXSize()
    {
        return 2;
    }

    @Override
    public int getYSize()
    {
        return 2;
    }

    @Override
    public int getZSize()
    {
        return 2;
    }
}
