package me.rik.idlecraft.models;

import org.bukkit.Material;

import java.util.ArrayList;

public class GatherableResource {

    public int id;
    public Material material;

    public GatherableResource(int id, Material material) {
        this.id = id;
        this.material = material;
    }

}
