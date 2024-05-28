package me.rik.idlecraft.database;

import me.rik.idlecraft.interfaces.IMultiBlock;
import me.rik.idlecraft.multiblocks.CraftingTable;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MultiblockService
{

    public static List<IMultiBlock> getMultiblocks(UUID uuid) {

        List<IMultiBlock> multiblocks = new ArrayList<>();

        try
        {
            PreparedStatement ps = DatabaseConnection.getConnection(uuid).prepareStatement("SELECT * FROM multiblocks WHERE uuid = ?");

            ps.setObject(1, uuid);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Location location = new Location(Bukkit.getWorld("world"), rs.getInt("location_x"), rs.getInt("location_y"), rs.getInt("location_z"));

                switch (rs.getInt("type")) {
                    case 1:
                        multiblocks.add(new CraftingTable(location, uuid));
                }
            }

            return multiblocks;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static List<IMultiBlock> getAllMultiblocks() {

            List<IMultiBlock> multiblocks = new ArrayList<>();

            try
            {
                PreparedStatement ps = DatabaseConnection.serverConnection.prepareStatement("SELECT * FROM multiblocks");

                ResultSet rs = ps.executeQuery();

                while (rs.next())
                {
                    Location location = new Location(Bukkit.getWorld("world"), rs.getInt("location_x"), rs.getInt("location_y"), rs.getInt("location_z"));

                    switch (rs.getInt("type")) {
                        case 1:
                            multiblocks.add(new CraftingTable(location, UUID.fromString(rs.getString("uuid"))));
                    }
                }

                return multiblocks;
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
    }


    public static void createMultiblock(UUID uuid, IMultiBlock multiBlock) {
        try
        {
            PreparedStatement ps = DatabaseConnection.getConnection(uuid).prepareStatement("INSERT INTO multiblocks (uuid, location_x, location_y, location_z, type) VALUES (?, ?, ?, ?, ?)");

            ps.setString(1, uuid.toString());
            ps.setInt(2, multiBlock.location.getBlockX());
            ps.setInt(3, multiBlock.location.getBlockY());
            ps.setInt(4, multiBlock.location.getBlockZ());
            ps.setInt(5, multiBlock.getType());

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void removeMultiblock(UUID uuid, IMultiBlock multiBlock) {
        try
        {
            PreparedStatement ps = DatabaseConnection.getConnection(uuid).prepareStatement("DELETE FROM multiblocks WHERE uuid = ? AND location_x = ? AND location_y = ? AND location_z = ?");

            ps.setString(1, uuid.toString());
            ps.setInt(2, multiBlock.location.getBlockX());
            ps.setInt(3, multiBlock.location.getBlockY());
            ps.setInt(4, multiBlock.location.getBlockZ());

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


}
