package me.axeno.nexora.warp.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.exception.SignGUIVersionException;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.message.MessageType;
import me.axeno.nexora.warp.Warp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class WarpMenuConfig extends Menu {

    final Warp warp;

    public WarpMenuConfig(Player owner, Warp warp) {
        super(owner);
        this.warp = warp;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> contents = new HashMap<>();

        contents.put(4, new ItemBuilder(this, warp.getItemStack(), meta -> {
            meta.displayName(Component.text(warp.getName())
                    .color(TextColor.color(0xF8C44D))
                    .decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("  §7Coordonées:"));
            lore.add(Component.text("    §8- §eX: §6" + String.format("%.0f", warp.getLocation().getX())));
            lore.add(Component.text("    §8- §eY: §6" + String.format("%.0f", warp.getLocation().getY())));
            lore.add(Component.text("    §8- §eZ: §6" + String.format("%.0f", warp.getLocation().getZ())));
            lore.add(Component.text("  §7Monde: §6" + warp.getLocation().getWorld().getName()));
            meta.lore(lore);
        }));

        contents.put(11, new ItemBuilder(this, Material.ITEM_FRAME, meta -> {
            meta.displayName(Component.text("Changer l’icône").color(TextColor.color(0x55FFAA))
                    .decoration(TextDecoration.ITALIC, false));
        }).setNextMenu(new WarpBlockMenu(getOwner(), warp)));

        contents.put(13, new ItemBuilder(this, Material.NAME_TAG, meta -> {
            meta.displayName(Component.text("Modifier le nom")
                    .color(TextColor.color(0xF8A94D))
                    .decoration(TextDecoration.ITALIC, false));
        }).setOnClick(e -> {
            getOwner().closeInventory();
            String[] lines = new String[4];
            lines[0] = warp.getName();
            lines[1] = "^^^^^^^^";
            lines[2] = "Entrez le nom";
            lines[3] = "du warp ici.";

            SignGUI signGUI;
            try {
                signGUI = SignGUI.builder()
                        .setLines(lines[0], lines[1], lines[2], lines[3])
                        .setType(Material.OAK_SIGN)
                        .setHandler((p, result) -> {
                            String input = result.getLine(0);

                            if (Nexora.getInstance().getWarpManager().renameWarp(warp.getName(), input)) {
                                Nexora.sendMessage(getOwner(), MessageType.SUCCESS,
                                        "Le warp a été renommé en " + input + ".");
                            } else {
                                Nexora.sendMessage(getOwner(), MessageType.ERROR,
                                        "Erreur lors du renommage du warp !");
                            }

                            return Collections.emptyList();
                        }).build();
            } catch (SignGUIVersionException ex) {
                throw new RuntimeException(ex);
            }

            signGUI.open(getOwner());
        }));

        contents.put(15, new ItemBuilder(this, Material.REDSTONE_BLOCK, meta -> {
            meta.displayName(Component.text("Supprimer le warp")
                    .color(TextColor.color(0xF8704D))
                    .decoration(TextDecoration.ITALIC, false));
        }).setNextMenu(new WarpConfirmDelete(getOwner(), warp)));

        // Back button
        contents.put(18, new ItemBuilder(this, Material.ARROW, meta -> {
            meta.displayName(Component.text("§8[§6«§8] §7Retour"));
        }).setOnClick(e -> new WarpMenu(getOwner()).open()));

        // Close inventory
        contents.put(22, new ItemBuilder(this, Material.BARRIER, meta -> {
            meta.displayName(Component.text("§8[§c×§8] §7Fermer le menu"));
        }).setCloseButton());

        return contents;
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.NORMAL;
    }

    @Override
    public @NotNull String getName() {
        return "Warp " + warp.getName() + " Config";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent arg0) {
    }
}