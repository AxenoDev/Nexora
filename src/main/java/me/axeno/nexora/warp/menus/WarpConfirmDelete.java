package me.axeno.nexora.warp.menus;

import java.util.ArrayList;
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

import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.Lang;
import me.axeno.nexora.utils.message.MessageType;
import me.axeno.nexora.warp.Warp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class WarpConfirmDelete extends Menu {

        final Warp warp;

        public WarpConfirmDelete(Player owner, Warp warp) {
                super(owner);
                this.warp = warp;
        }

        @Override
        public @NotNull Map<Integer, ItemStack> getContent() {
                Map<Integer, ItemStack> contents = new HashMap<>(fill(Material.GRAY_STAINED_GLASS_PANE));

                contents.put(4, new ItemBuilder(this, warp.getItemStack(), meta -> {
                        meta.displayName(LegacyComponentSerializer.legacySection().deserialize(warp.getName())
                                        .color(TextColor.color(0xF8C44D))
                                        .decoration(TextDecoration.ITALIC, false));
                        List<Component> lore = new ArrayList<>();
                        lore.add(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.coordinates.name")));
                        lore.add(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.coordinates.x")
                                        .replace("{x}", String.format("%.0f", warp.getLocation().getX()))).decoration(TextDecoration.ITALIC, false));
                        lore.add(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.coordinates.y")
                                        .replace("{y}", String.format("%.0f", warp.getLocation().getY()))).decoration(TextDecoration.ITALIC, false));
                        lore.add(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.coordinates.z")
                                        .replace("{z}", String.format("%.0f", warp.getLocation().getZ()))).decoration(TextDecoration.ITALIC, false));
                        lore.add(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.coordinates.world")
                                        .replace("{world}", warp.getLocation().getWorld().getName())).decoration(TextDecoration.ITALIC, false));
                        meta.lore(lore);
                }));

                contents.put(2, new ItemBuilder(this, Material.RED_STAINED_GLASS_PANE, meta -> {
                        meta.displayName(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.delete.confirm.prefix"))
                                        .append(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.delete.confirm.text"))
                                                        .color(TextColor.color(
                                                                        Lang.getInt("menu.warp.delete.confirm.color"))))
                                        .decoration(TextDecoration.ITALIC, false));
                }).setOnClick(e -> {
                        Nexora.getInstance().getWarpManager().deleteWarp(warp);
                        Nexora.sendMessage(getOwner(), MessageType.SUCCESS,
                                        Lang.get("menu.warp.delete.message").replace("{warp.name}", warp.getName()));
                        new WarpMenu(getOwner()).open();
                }));

                contents.put(6, new ItemBuilder(this, Material.GREEN_STAINED_GLASS_PANE, meta -> {
                        meta.displayName(
                                        LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.delete.cancel.prefix"))
                                                        .append(LegacyComponentSerializer.legacySection().deserialize(Lang.get("menu.warp.delete.cancel.text"))
                                                                        .color(TextColor.color(Lang.getInt(
                                                                                        "menu.warp.delete.cancel.color"))))
                                                        .decoration(TextDecoration.ITALIC, false));
                }).setBackButton());

                return contents;
        }

        @Override
        public @NotNull InventorySize getInventorySize() {
                return InventorySize.SMALLEST;
        }

        @Override
        public @NotNull String getName() {
                return Lang.get("menu.warp.delete.title")
                                .replace("{warp.name}", warp.getName());
        }

        @Override
        public void onInventoryClick(InventoryClickEvent arg0) {
        }

}
