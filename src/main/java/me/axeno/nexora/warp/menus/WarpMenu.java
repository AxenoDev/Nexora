package me.axeno.nexora.warp.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import me.axeno.nexora.Nexora;
import me.axeno.nexora.warp.Warp;
import net.kyori.adventure.text.Component;

public class WarpMenu extends PaginatedMenu {

    final Nexora nexora;

    public WarpMenu(Player player) {
        super(player);
        this.nexora = Nexora.getInstance();
    }

    @Override
    public @NotNull String getName() {
        return "Warp Menu";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
    }

    @Override
    public @Nullable Material getBorderMaterial() {
        return Material.LIGHT_BLUE_STAINED_GLASS_PANE;
    }

    @Override
    public Map<Integer, ItemStack> getButtons() {
        Map<Integer, ItemStack> btns = new HashMap<>();

        btns.put(45, new ItemBuilder(this, Material.ARROW, itemMeta -> {
            itemMeta.displayName(Component.text("§8[§6«§8] §7Page précédente"));
        }).setPreviousPageButton());
        btns.put(49, new ItemBuilder(this, Material.BARRIER).setCloseButton());
        btns.put(53, new ItemBuilder(this, Material.ARROW, itemMeta -> {
            itemMeta.displayName(Component.text("§8[§6»§8] §7Page suivante"));
        }).setNextPageButton());

        return btns;
    }

    @Override
    public @NotNull List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();

        for (Warp warp : nexora.warpManager.getWarps().values()) {
            ItemStack itemStack = new ItemStack(warp.getItemStack().getType());

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Component.text("§6" + warp.getName()));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("  §7Coordonées:"));
            lore.add(Component.text("    §8- §eX: §6" + String.format("%.0f", warp.getLocation().getX())));
            lore.add(Component.text("    §8- §eY: §6" + String.format("%.0f", warp.getLocation().getY())));
            lore.add(Component.text("    §8- §eZ: §6" + String.format("%.0f", warp.getLocation().getZ())));
            lore.add(Component.text("  §7Monde: §6" + warp.getLocation().getWorld().getName()));
            lore.add(Component.text("§7"));
            lore.add(Component.text("§8[§6»§8] §7Clique gauche pour se téléporter"));

            if (getOwner().hasPermission("nexora.warp.admin.edit"))
                lore.add(Component.text("§8[§6»§8] §7Clique droit pour le modifier"));
            itemMeta.lore(lore);
            itemStack.setItemMeta(itemMeta);

            ItemStack finalItem = new ItemBuilder(this, itemStack)
                    .setOnClick(event -> {
                        if (event.isLeftClick() && nexora.warpManager.teleportToWarp(getOwner(), warp.getName()))
                            getOwner().closeInventory();
                        else if (event.isRightClick() && getOwner().hasPermission("nexora.warp.admin.edit"))
                            new WarpMenuConfig(getOwner(), warp).open();
                    });

            items.add(finalItem);
        }

        return items;
    }

    @Override
    public List<Integer> getStaticSlots() {
        return List.of(45, 46, 47, 48, 49, 50, 51, 52, 53);
    }
}
