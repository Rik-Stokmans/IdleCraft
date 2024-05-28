package me.rik.idlecraft.database;

import me.rik.idlecraft.models.GatherableResource;
import org.bukkit.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ResourceService
{
    public static ArrayList<GatherableResource> gatherableResources = new ArrayList<>();

    public static ArrayList<GatherableResource> getResources()
    {
        ArrayList<GatherableResource> gatherableResources = new ArrayList<>();

        try
        {
            PreparedStatement ps = DatabaseConnection.serverConnection.prepareStatement("SELECT * FROM gatherable_resources");

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                gatherableResources.add(new GatherableResource(rs.getInt("id"), Material.getMaterial(rs.getString("material"))));
            }

            return gatherableResources;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static int inGatherableResources(Material material)
    {
        for (GatherableResource gr : gatherableResources)
        {
            if (gr.material == material) return gr.id;
        }

        return -1;
    }
}
