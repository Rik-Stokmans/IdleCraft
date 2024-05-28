package me.rik.idlecraft.database;

import me.rik.idlecraft.models.Backpack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class BackpackService {


    public static Backpack getBackpack(UUID uuid) {

        HashMap<Integer, Integer> items = new HashMap<>();

        try
        {
            PreparedStatement ps = DatabaseConnection.getConnection(uuid).prepareStatement("SELECT item, amount FROM backpack WHERE uuid = ?");

            ps.setObject(1, uuid);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                items.put(rs.getInt("item"), rs.getInt("amount"));
            }

            return new Backpack(uuid, items);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void updateBackpack(UUID uuid, int item, int amount) {

        try
        {
            PreparedStatement ps = DatabaseConnection.getConnection(uuid).prepareStatement("UPDATE backpack SET amount = ? WHERE uuid = ? AND item = ?");

            ps.setInt(1, amount);
            ps.setString(2, uuid.toString());
            ps.setInt(3, item);

            int rs = ps.executeUpdate();

            System.out.println("Updated backpack " + uuid + " " + item + " " + amount + " changed: " + rs + " rows");

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
