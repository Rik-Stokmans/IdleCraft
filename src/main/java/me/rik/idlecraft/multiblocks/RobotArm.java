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

public class RobotArm extends IMultiBlock
{

    public RobotArm(Location location, UUID uuid)
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

        for (int x = -xSize / 2; x < xSize -xSize/2; x++)
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

        for (int i = 0; i < displayArrayList.size(); i++)
        {
            displayArrayList.get(i).setBlock(Material.REDSTONE_BLOCK.createBlockData());
            displayArrayList.get(i).setTransformation(transformation);
            displays.add(displayArrayList.get(i));
        }
    }

    @Override
    public int getType()
    {
        return 2;
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
