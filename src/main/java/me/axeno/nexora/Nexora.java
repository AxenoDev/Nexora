package me.axeno.nexora;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Nexora extends JavaPlugin {

    @Getter
    private static Nexora instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Nexora has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Nexora has been disabled!");
    }
}
