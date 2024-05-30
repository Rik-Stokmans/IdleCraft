package me.rik.idlecraft.multiblocks;

import me.rik.idlecraft.interfaces.IMultiBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.UUID;

public class CraftingTable extends IMultiBlock
{
    public CraftingTable(Location location, UUID uuid)
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
            for (int z = -zSize / 2; z < zSize - zSize / 2; z++)
            {
                displayArrayList.add(location.getWorld().spawn(location.clone().add(new Vector(x, 0, z)), BlockDisplay.class));
            }
        }

        System.out.println("Placed " + displayArrayList.size() + " displays");

        Transformation transformation = new Transformation
                (
                        new Vector3f(),
                        new AxisAngle4f(),
                        new Vector3f(1, 1, 1),
                        new AxisAngle4f()
                );

        for (int i = 0; i < displayArrayList.size(); i++)
        {
            displayArrayList.get(i).setBlock((i % 2 == 0) ? Material.BROWN_CONCRETE.createBlockData() : Material.BROWN_TERRACOTTA.createBlockData());
            displayArrayList.get(i).setTransformation(transformation);
            displays.add(displayArrayList.get(i));
        }
    }

    @Override
    public Inventory initInventory()
    {
        Inventory inventory = Bukkit.createInventory(null, 27);

        return inventory;
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent e)
    {
        if (inventory == e.getClickedInventory())
        {
            e.setCancelled(true);
            System.out.println("Clicked on crafting table interface");
        }
    }

    @Override
    public int getType()
    {
        return 1;
    }

    @Override
    public int getXSize()
    {
        return 3;
    }

    @Override
    public int getYSize()
    {
        return 1;
    }

    @Override
    public int getZSize()
    {
        return 3;
    }


}
