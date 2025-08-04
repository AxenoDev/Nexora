package me.axeno.nexora.warp.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.axeno.nexora.api.menulib.Menu;
import me.axeno.nexora.api.menulib.utils.InventorySize;
import me.axeno.nexora.api.menulib.utils.ItemBuilder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.exception.SignGUIVersionException;
import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.Lang;
import me.axeno.nexora.utils.message.MessageType;
import me.axeno.nexora.warp.Warp;
import net.kyori.adventure.text.Component;
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
            meta.displayName(LegacyComponentSerializer.legacySection().deserialize(warp.getName())
                    .color(TextColor.color(0xF8C44D))
                    .decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.coordinates.name")));
            lore.add(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.coordinates.x")
                    .replace("{x}", String.format("%.0f", warp.getLocation().getX()))));
            lore.add(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.coordinates.y")
                    .replace("{y}", String.format("%.0f", warp.getLocation().getY()))));
            lore.add(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.coordinates.z")
                    .replace("{z}", String.format("%.0f", warp.getLocation().getZ()))));
            lore.add(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.coordinates.world")
                    .replace("{world}", warp.getLocation().getWorld().getName())));
            meta.lore(lore);
        }));

        contents.put(11, new ItemBuilder(this, Material.ITEM_FRAME, meta -> {
            meta.displayName(
                    LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.edit.change.icon.text"))
                            .color(TextColor.color(Lang.getInt("menu.warp.edit.change.icon.color")))
                            .decoration(TextDecoration.ITALIC, false));
        }).setNextMenu(new WarpBlockMenu(getOwner(), warp)));

        contents.put(13, new ItemBuilder(this, Material.NAME_TAG, meta -> {
            meta.displayName(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.edit.change.name.text"))
                    .color(TextColor.color(Lang.getInt("menu.warp.edit.change.name.color")))
                    .decoration(TextDecoration.ITALIC, false));
        }).setOnClick(e -> {
            getOwner().closeInventory();
            String[] lines = new String[4];
            lines[0] = warp.getName();
            lines[1] = Lang.get("menu.warp.edit.change.name.sign.line2");
            lines[2] = Lang.get("menu.warp.edit.change.name.sign.line3");
            lines[3] = Lang.get("menu.warp.edit.change.name.sign.line4");

            SignGUI signGUI;
            try {
                signGUI = SignGUI.builder()
                        .setLines(lines[0], lines[1], lines[2], lines[3])
                        .setType(Material.OAK_SIGN)
                        .setHandler((p, result) -> {
                            String input = result.getLine(0);

                            if (Nexora.getInstance().getWarpManager().renameWarp(warp.getName(), input)) {
                                Nexora.sendMessage(getOwner(), MessageType.SUCCESS,
                                        Lang.get("menu.warp.edit.change.name.success")
                                                .replace("{warp.name}", input));
                            } else {
                                Nexora.sendMessage(getOwner(), MessageType.ERROR,
                                        Lang.get("menu.warp.edit.change.name.error"));
                            }

                            return Collections.emptyList();
                        }).build();
            } catch (SignGUIVersionException ex) {
                throw new RuntimeException(ex);
            }

            signGUI.open(getOwner());
        }));

        contents.put(15, new ItemBuilder(this, Material.REDSTONE_BLOCK, meta -> {
            meta.displayName(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.edit.delete.text"))
                    .color(TextColor.color(Lang.getInt("menu.warp.edit.delete.color")))
                    .decoration(TextDecoration.ITALIC, false));
        }).setNextMenu(new WarpConfirmDelete(getOwner(), warp)));

        contents.put(18, new ItemBuilder(this, Material.ARROW, meta -> {
            meta.displayName(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.back")));
        }).setOnClick(e -> new WarpMenu(getOwner()).open()));

        contents.put(22, new ItemBuilder(this, Material.BARRIER, meta -> {
            meta.displayName(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.close")));
        }).setCloseButton());

        return contents;
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.NORMAL;
    }

    @Override
    public @NotNull String getName() {
        return Lang.get("menu.warp.edit.title")
                .replace("{warp.name}", warp.getName());
    }

    @Override
    public void onInventoryClick(InventoryClickEvent arg0) {
    }
}