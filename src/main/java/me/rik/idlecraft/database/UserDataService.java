package me.rik.idlecraft.database;

import me.rik.idlecraft.models.User;
import java.sql.*;
import java.util.UUID;

public class UserDataService
{
    public static long psTime = 0;
    public static long rsTime = 0;

    public static User getUser(UUID uuid)
    {
        try
        {

            PreparedStatement ps = DatabaseConnection.getConnection(uuid).prepareStatement("SELECT uuid, money FROM users WHERE uuid = ?");

            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double money = rs.getDouble("money");
                return new User(uuid, money);
            }

            return createUser(uuid);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static User createUser(UUID uuid)
    {
        double startingMoney = 0.0;

        try
        {
            PreparedStatement ps = DatabaseConnection.getConnection(uuid).prepareStatement("INSERT INTO users (uuid, money) VALUES (?, ?)");

            ps.setString(1, uuid.toString());
            ps.setDouble(2, startingMoney);

            int rs = ps.executeUpdate();

            if (rs > 0) return new User(uuid, startingMoney);

            return null;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void updateUser(User user)
    {
        try
        {
            long psStart = System.currentTimeMillis();

            PreparedStatement ps = DatabaseConnection.getConnection(user.uuid).prepareStatement("UPDATE users SET money = ? WHERE uuid = ?");

            ps.setDouble(1, user.money);
            ps.setString(2, user.uuid.toString());

            long psEnd = System.currentTimeMillis();
            psTime += psEnd - psStart;

            long rsStart = System.currentTimeMillis();

            int rs = ps.executeUpdate();

            long rsEnd = System.currentTimeMillis();
            rsTime += rsEnd - rsStart;

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

}
