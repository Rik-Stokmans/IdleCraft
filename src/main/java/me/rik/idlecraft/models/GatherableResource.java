package me.rik.idlecraft.models;

import org.bukkit.Material;

public class GatherableResource
{

    public int id;
    public Material material;

    public GatherableResource(int id, Material material)
    {
        this.id = id;
        this.material = material;
    }

}
