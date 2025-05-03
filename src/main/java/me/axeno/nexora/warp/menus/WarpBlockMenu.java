package me.axeno.nexora.warp.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.message.MessageType;
import me.axeno.nexora.warp.Warp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class WarpBlockMenu extends PaginatedMenu {

    final Warp warp;

    private static final List<ItemStack> CACHED_ITEMS = new ArrayList<>();

    public WarpBlockMenu(Player Player, Warp warp) {
        super(Player);
        this.warp = warp;
        System.out.println("Creating WarpBlockMenu for " + warp.getName());
        System.out.println(warp.toString());
        System.out.println(warp.getItemStack());
        refreshCachedItems();
    }

    private void refreshCachedItems() {
        CACHED_ITEMS.clear();
        for (Material material : Material.values()) {
            if (material.isItem() && !material.isAir()) {
                ItemStack item = new ItemBuilder(null, material, meta -> {
                    meta.displayName(Component.text(material.name().replace("_", " ").toLowerCase())
                            .color(TextColor.color(0x55FFAA))
                            .decoration(TextDecoration.ITALIC, false));
                    meta.lore(List.of(Component.text("§8[§6»§8] §7Clique gauche pour sélectionner")));
                    if (warp.getItemStack().getType().equals(material)) {
                        meta.lore(List.of(Component.text("§8[§a✔§8] §7Icône actuelle")));
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                });
                CACHED_ITEMS.add(item);
            }
        }
    }

    @Override
    public @NotNull String getName() {
        return "§6Icône du warp §f: §e" + warp.getName();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (getStaticSlots().contains(event.getRawSlot())) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        Material material = clickedItem.getType();

        if (Nexora.getInstance().getWarpManager().changeWarpIcon(warp.getName(), new ItemStack(material))) {
            Nexora.sendMessage(getOwner(), MessageType.SUCCESS, "L'icône du warp a été mise à jour avec succès !");
        } else {
            Nexora.sendMessage(getOwner(), MessageType.ERROR, "Erreur lors de la mise à jour de l'icône du warp !");
        }

        new WarpMenuConfig(getOwner(), warp).open();
    }

    @Override
    public @Nullable Material getBorderMaterial() {
        return Material.LIGHT_BLUE_STAINED_GLASS_PANE;
    }

    @Override
    public Map<Integer, ItemStack> getButtons() {
        Map<Integer, ItemStack> btns = new HashMap<>();
        btns.put(45, new ItemBuilder(this, Material.ARROW, meta -> {
            meta.displayName(Component.text("§8[§6«§8] §7Page précédente"));
        }).setPreviousPageButton());

        btns.put(47, new ItemBuilder(this, warp.getItemStack(), meta -> {
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

        btns.put(49, new ItemBuilder(this, Material.BARRIER, meta -> {
            meta.displayName(Component.text("§8[§c✘§8] §7Fermer le menu"));
        }).setCloseButton());

        btns.put(53, new ItemBuilder(this, Material.ARROW, meta -> {
            meta.displayName(Component.text("§8[§6»§8] §7Page suivante"));
        }).setNextPageButton());

        return btns;
    }

    @Override
    public @NotNull List<ItemStack> getItems() {
        return new ArrayList<>(CACHED_ITEMS);
    }

    @Override
    public @NotNull List<Integer> getStaticSlots() {
        return List.of(45, 46, 47, 48, 49, 50, 51, 52, 53);
    }

}
