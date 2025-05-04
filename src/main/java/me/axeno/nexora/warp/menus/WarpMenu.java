package me.axeno.nexora.warp.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.Lang;
import me.axeno.nexora.warp.Warp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class WarpMenu extends PaginatedMenu {

    final Nexora nexora;

    public WarpMenu(Player player) {
        super(player);
        this.nexora = Nexora.getInstance();
    }

    @Override
    public @NotNull String getName() {
        return Lang.get("menu.warp.title");
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
            itemMeta.displayName(Component.text(Lang.get("menu.page.previous")));
        }).setPreviousPageButton());
        btns.put(49, new ItemBuilder(this, Material.BARRIER, itemMeta -> {
            itemMeta.displayName(Component.text(Lang.get("menu.close")));
        }).setCloseButton());
        btns.put(53, new ItemBuilder(this, Material.ARROW, itemMeta -> {
            itemMeta.displayName(Component.text(Lang.get("menu.page.next")));
        }).setNextPageButton());

        return btns;
    }

    @Override
    public @NotNull List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();

        for (Warp warp : nexora.warpManager.getWarps().values()) {
            ItemStack itemStack = new ItemStack(warp.getItemStack().getType());

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Component.text(warp.getName())
                    .color(TextColor.color(0xF8C44D))
                    .decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(Lang.get("menu.warp.coordinates.name")));
            lore.add(Component.text(Lang.get("menu.warp.coordinates.x")
                    .replace("{x}", String.format("%.0f", warp.getLocation().getX()))));
            lore.add(Component.text(Lang.get("menu.warp.coordinates.y")
                    .replace("{y}", String.format("%.0f", warp.getLocation().getY()))));
            lore.add(Component.text(Lang.get("menu.warp.coordinates.z")
                    .replace("{z}", String.format("%.0f", warp.getLocation().getZ()))));
            lore.add(Component.text(Lang.get("menu.warp.coordinates.world")
                    .replace("{world}", warp.getLocation().getWorld().getName())));
            lore.add(Component.text("§7"));
            lore.add(Component.text(Lang.get("menu.warp.click_to_teleport")));

            if (getOwner().hasPermission("nexora.warp.admin.edit"))
                lore.add(Component.text(Lang.get("menu.warp.click_to_edit")));
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
