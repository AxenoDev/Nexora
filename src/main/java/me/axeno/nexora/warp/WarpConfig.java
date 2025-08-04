package me.axeno.nexora.warp;

import java.io.File;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import me.axeno.nexora.Nexora;

public class WarpConfig {
    private final Nexora nexora;
    public FileConfiguration config;
    public File file;

    public WarpConfig() {
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

    public void saveWarpLocation(UUID id, String name, Location location, ItemStack item) {
        String path = id.toString();
        this.config.set(path + ".name", name);
        this.config.set(path + ".location.x", location.getX());
        this.config.set(path + ".location.y", location.getY());
        this.config.set(path + ".location.z", location.getZ());
        this.config.set(path + ".location.world", location.getWorld().getName());
        this.config.set(path + ".location.yaw", location.getYaw());
        this.config.set(path + ".location.pitch", location.getPitch());
        this.config.set(path + ".menu.item", item.serialize());
        saveConfig();
    }

    public String getWarpName(String id) {
        return config.getString(id + ".name");
    }

    public Location getWarpLocation(String id) {
        double x = config.getDouble(id + ".location.x");
        double y = config.getDouble(id + ".location.y");
        double z = config.getDouble(id + ".location.z");
        String worldName = config.getString(id + ".location.world");
        float yaw = (float) config.getDouble(id + ".location.yaw");
        float pitch = (float) config.getDouble(id + ".location.pitch");

        return new Location(nexora.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

    public boolean isWarpExists(String warp) {
        return config.contains(warp);
    }

    public boolean removeWarpLocation(Warp warp) {
        String warpId = warp.getId().toString();

        if (isWarpExists(warpId)) {
            config.set(warpId, null);
            saveConfig();
            return true;
        }
        return false;
    }

    public Set<String> getWarps() {
        return config.getKeys(false);
    }

    public boolean changeWarpName(String id, String newName) {
        try {
            if (isWarpExists(id)) {
                config.set(id + ".name", newName);
                saveConfig();
                return true;
            }
            return false;
        } catch (Exception e) {
            Nexora.getInstance().getLogger().severe(
                    "Erreur lors du changement de nom du warp " + id + " en " + newName + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeWarpIcon(Warp warp, ItemStack item) {
        if (isWarpExists(warp.getId().toString()) && item != null && item.getType() != null) {
            config.set(warp.getId() + ".menu.item", item.serialize());
            saveConfig();
            return true;
        }
        return false;
    }
}
