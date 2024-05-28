package me.rik.idlecraft.database;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseConnection
{
    public static HashMap<UUID, Connection> databaseConnectionHashMap = new HashMap<>();
    public static Connection serverConnection;

    private static final String url = "jdbc:mariadb://localhost:3306/MinecraftServer";
    private static final String password = "java";
    private static final String user = "java";

    public static void init()
    {
        try
        {
            Class.forName("org.mariadb.jdbc.Driver");

            serverConnection = DriverManager.getConnection(url, user, password);

            Bukkit.getOnlinePlayers().forEach(player -> {
                try
                {
                    databaseConnectionHashMap.put(player.getUniqueId(), DriverManager.getConnection(url, user, password));
                }
                catch (SQLException e)
                {
                    throw new RuntimeException(e);
                }
            });
        }
        catch (ClassNotFoundException | SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(UUID uuid) {

        if (databaseConnectionHashMap.containsKey(uuid)) return databaseConnectionHashMap.get(uuid);

        else openConnection(uuid);

        return databaseConnectionHashMap.get(uuid);

    }

    public static void openConnection(UUID uuid) {
        if (databaseConnectionHashMap.containsKey(uuid)) return;

        try
        {
            databaseConnectionHashMap.put(uuid, DriverManager.getConnection(url, user, password));
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(UUID uuid)
    {
        Connection connection = databaseConnectionHashMap.getOrDefault(uuid, null);

        if (connection == null) return;

        try
        {
            connection.close();
            databaseConnectionHashMap.remove(uuid);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void closeAllConnections() {
        databaseConnectionHashMap.keySet().forEach(uuid -> {

            try
            {
                databaseConnectionHashMap.get(uuid).close();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }

            databaseConnectionHashMap = new HashMap<>();
        });
    }
}
