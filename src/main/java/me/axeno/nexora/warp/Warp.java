package me.axeno.nexora.warp;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Warp {
    private UUID id;
    private String name;
    private Location location;
    private ItemStack itemStack;

    public Warp(UUID id, String name, Location location, ItemStack itemStack) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.itemStack = itemStack;
    }

    @Override
    public String toString() {
        return "Warp{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", itemStack=" + itemStack +
                '}';
    }
}
