package me.axeno.nexora.warp;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.Lang;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;

public class WarpManager {

    private final Nexora nexora;
    private final WarpConfig warpConfig;

    @Getter
    private final Map<String, Warp> warps;

    public WarpManager() {
        this.nexora = Nexora.getInstance();
        this.warpConfig = new WarpConfig();
        this.warps = new ConcurrentHashMap<>();
        loadWarpsCache();
    }

    public void loadWarpsCache() {
        warps.clear();

        nexora.getLogger().info("Loading warps from config...");

        for (String warpId : warpConfig.getWarps()) {
            try {
                String name = warpConfig.getWarpName(warpId);
                Location location = warpConfig.getWarpLocation(warpId);
                ItemStack item = null;

                if (warpConfig.config.contains(warpId + ".menu.item")) {
                    item = ItemStack.deserialize(
                            warpConfig.config.getConfigurationSection(warpId + ".menu.item").getValues(false));
                }

                if (item == null || item.getType() == Material.AIR) {
                    nexora.getLogger()
                            .warning("Item invalide pour le warp '" + name + "', utilisation de l'item par défaut");
                    item = new ItemStack(Material.RED_BED);
                }

                UUID uuid;
                try {
                    uuid = UUID.fromString(warpId);
                } catch (IllegalArgumentException e) {
                    uuid = UUID.randomUUID();
                    nexora.getLogger().info("Génération d'un nouveau UUID pour le warp " + name);
                }

                Warp warp = new Warp(uuid, name, location, item);
                warps.put(name.toLowerCase(), warp);
                nexora.getLogger().info("Warp chargé: " + name + " (ID: " + warpId + ")");
            } catch (Exception e) {
                nexora.getLogger()
                        .warning("Erreur lors du chargement du warp avec ID '" + warpId + "': " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Warp getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    public boolean teleportToWarp(Player player, String warpName) {
        Warp warp = getWarp(warpName);
        if (warp != null) {
            Location location = warp.getLocation();
            player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 60, 0.5, 1, 0.5, 0.3);

            player.teleport(location);

            player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, location, 60, 0.5, 1, 0.5, 0.1);
            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 20, 0.5, 1, 0.5);

            player.playSound(Sound.sound(org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, Source.PLAYER, 1.0f, 1.2f));

            Title title = Title.title(
                    Component
                            .text(Lang.get("warp.teleport.message.text"),
                                    TextColor.color(Lang.getInt("warp.teleport.message.color")))
                            .append(Component.text(warp.getName(),
                                    TextColor.color(Lang.getInt("warp.teleport.message.warp.color")))),
                    Component.text(Lang.get("warp.teleport.subtitle.text"),
                            TextColor.color(Lang.getInt("warp.teleport.subtitle.color"))));
            player.showTitle(title);
            return true;
        }
        return false;
    }

    public boolean createWarp(String name, Location location, ItemStack item) {
        if (warps.containsKey(name.toLowerCase()))
            return false;

        if (name.length() < 3 || name.length() > 24 || !name.matches("^[a-zA-Z0-9]+$"))
            return false;

        UUID uuid = UUID.randomUUID();
        warpConfig.saveWarpLocation(uuid, name, location, item);
        Warp warp = new Warp(uuid, name, location, item);
        warps.put(name.toLowerCase(), warp);

        return true;
    }

    public boolean deleteWarp(Warp warp) {
        if (!warps.containsKey(warp.getName().toLowerCase()))
            return false;

        boolean removed = warpConfig.removeWarpLocation(warp);

        if (removed)
            warps.remove(warp.getName().toLowerCase());

        return removed;
    }

    public boolean renameWarp(String oldName, String newName) {
        if (oldName.equalsIgnoreCase(newName))
            return true;
        if (!warps.containsKey(oldName.toLowerCase()) || warps.containsKey(newName.toLowerCase()))
            return false;

        if (newName.length() < 3 || newName.length() > 24 || !newName.matches("^[a-zA-Z0-9]+$"))
            return false;

        Warp warp = getWarp(oldName.toLowerCase());
        if (warp == null)
            return false;
        boolean changed = warpConfig.changeWarpName(warp.getId().toString(), newName);

        if (changed) {
            warp.setName(newName);
            warps.remove(oldName.toLowerCase());
            warps.put(newName.toLowerCase(), warp);
        }

        return changed;
    }

    public boolean changeWarpIcon(String name, ItemStack item) {
        if (!warpExists(name) || item == null || item.getType().equals(Material.AIR))
            return false;

        Warp warp = getWarp(name.toLowerCase());
        if (warpConfig.changeWarpIcon(warp, item)) {
            warp.setItemStack(item);
            return true;
        }

        return false;
    }

    public Collection<Warp> getAllWarps() {
        return Collections.unmodifiableCollection(warps.values());
    }

    public boolean warpExists(String name) {
        return warps.containsKey(name.toLowerCase());
    }
}
