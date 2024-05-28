package me.rik.idlecraft.models;

import java.util.HashMap;
import java.util.UUID;

public class Backpack {

    public UUID uuid;
    public HashMap<Integer, Integer> items;

    public Backpack(UUID uuid, HashMap<Integer, Integer> items)
    {
        this.uuid = uuid;
        this.items = items;
    }

}
