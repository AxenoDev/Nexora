package me.axeno.nexora.config;

import java.io.File;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import me.axeno.nexora.Nexora;

public class NexoraConfig {
    private final Nexora nexora;
    public FileConfiguration config;
    public File file;

    public NexoraConfig() {
        this.nexora = Nexora.getInstance();
        this.file = new File(nexora.getDataFolder(), "warps.yml");
        loadConfig();
    }

    public void loadConfig() {
        if (!file.exists()) {
            try {
                if (!nexora.getDataFolder().exists()) {
                    nexora.getDataFolder().mkdirs();
                }
                file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException("Could not create " + file.getName() + " file", e);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (Exception e) {
            throw new RuntimeException("Could not save " + file.getName() + " file", e);
        }
    }

    public void saveWarpLocation(String warp, Location location, ItemStack item) {
        this.config.set(warp + ".location.x", location.getX());
        this.config.set(warp + ".location.y", location.getY());
        this.config.set(warp + ".location.z", location.getZ());
        this.config.set(warp + ".location.world", location.getWorld().getName());
        this.config.set(warp + ".location.yaw", location.getYaw());
        this.config.set(warp + ".location.pitch", location.getPitch());
        this.config.set(warp + ".menu.item", item.serialize());
        saveConfig();
    }

    public Location getWarpLocation(String warp) {
        double x = config.getDouble(warp + ".location.x");
        double y = config.getDouble(warp + ".location.y");
        double z = config.getDouble(warp + ".location.z");
        String worldName = config.getString(warp + ".location.world");
        float yaw = (float) config.getDouble(warp + ".location.yaw");
        float pitch = (float) config.getDouble(warp + ".location.pitch");

        return new Location(nexora.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

    public boolean isWarpExists(String warp) {
        return config.contains(warp);
    }

    public boolean removeWarpLocation(String warp) {
        if (isWarpExists(warp)) {
            config.set(warp, null);
            saveConfig();
            return true;
        }
        return false;
    }

    public Set<String> getWarps() {
        return config.getKeys(false);
    }

    public boolean changeWarpName(String oldName, String newName) {
        if (isWarpExists(oldName) && !isWarpExists(newName)) {
            Location location = getWarpLocation(oldName);
            ItemStack item = config.getItemStack(oldName + ".menu.item");
            removeWarpLocation(oldName);
            saveWarpLocation(newName, location, item);
            return true;
        }
        return false;
    }
}
