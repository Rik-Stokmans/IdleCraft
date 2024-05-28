package me.rik.idlecraft.multiblocks;

import me.rik.idlecraft.interfaces.IMultiBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.UUID;

public class CraftingTable extends IMultiBlock
{
    static int xSize = 3;
    static int ySize = 1;
    static int zSize = 3;

    public CraftingTable(Location location, UUID uuid)
    {
        super(location, uuid);
    }


    @Override
    public void place()
    {
        int blockX = location.getBlockX();
        int blockY = location.getBlockY();
        int blockZ = location.getBlockZ();

        System.out.println("Placing crafting table at " + blockX + ", " + blockY + ", " + blockZ);

        for (int x = blockX - (xSize/2); x < blockX + xSize - (xSize/2); x++)
        {
            for (int y = blockY - (ySize/2); y < blockY + ySize - (ySize/2); y++)
            {
                for (int z = blockZ - (zSize/2); z < blockZ + zSize - (zSize/2); z++)
                {
                    location.getWorld().getBlockAt(x, y, z).setType(Material.BARRIER);
                }
            }
        }

        placeDisplays();
    }

    private void placeDisplays()
    {
        ArrayList<BlockDisplay> displayArrayList = new ArrayList<>();

        for (int x = -xSize / 2; x < xSize -xSize/2; x++)
        {
            for (int z = -zSize / 2; z < zSize - zSize/2; z++)
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
            displayArrayList.get(i).setBlock((i%2 == 0) ? Material.BROWN_CONCRETE.createBlockData() : Material.BROWN_TERRACOTTA.createBlockData());
            displayArrayList.get(i).setTransformation(transformation);
            displays.add(displayArrayList.get(i));
        }
    }


    @Override
    public int getType()
    {
        return 1;
    }


}
