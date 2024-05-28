package me.rik.idlecraft.models;

import java.util.UUID;

public class User
{

    public User(UUID uuid, double money) {
        this.uuid = uuid;
        this.money = money;
    }

    public UUID uuid;
    public double money;
}
