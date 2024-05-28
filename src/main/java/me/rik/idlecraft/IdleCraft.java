package me.rik.idlecraft;

import me.rik.idlecraft.database.BackpackService;
import me.rik.idlecraft.database.DatabaseConnection;
import me.rik.idlecraft.database.ResourceService;
import me.rik.idlecraft.events.GatherEvent;
import me.rik.idlecraft.events.JoinEvent;
import me.rik.idlecraft.events.LeaveEvent;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class IdleCraft extends JavaPlugin
{

    public static Plugin plugin;

    @Override
    public void onEnable()
    {
        // Startup Message

        getLogger().info("Starting IdleCraft");


        // Initializing plugin variable

        plugin = this;


        // Initializing database connections

        DatabaseConnection.init();
        getLogger().info("IdleCraft has connected to database");


        // Initialize gatherable resources

        ResourceService.gatherableResources =  ResourceService.getResources();


        // Adding all events

        ArrayList<Listener> events = new ArrayList<>();
        //list of events
        events.add(new GatherEvent());
        events.add(new JoinEvent());
        events.add(new LeaveEvent());

        for (Listener l : events) {
            getServer().getPluginManager().registerEvents(l, this);
        }
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic

        getLogger().info("Stopping IdleCraft");

        DatabaseConnection.closeAllConnections();

        getLogger().info("IdleCraft has disconnected");
    }
}
