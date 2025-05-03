package me.axeno.nexora.warp.menus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.message.MessageType;
import me.axeno.nexora.warp.Warp;
import net.kyori.adventure.text.Component;

public class WarpConfirmDelete extends Menu {

    final Warp warp;

    public WarpConfirmDelete(Player owner, Warp warp) {
        super(owner);
        this.warp = warp;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> contents = new HashMap<>(fill(Material.GRAY_STAINED_GLASS_PANE));

        contents.put(4, warp.getItemStack());

        contents.put(2, new ItemBuilder(this, Material.GREEN_STAINED_GLASS_PANE, meta -> {
            meta.displayName(Component.text("§aConfirmer la suppression"));
        }).setOnClick(e -> {
            Nexora.getInstance().getWarpManager().deleteWarp(warp);
            Nexora.sendMessage(getOwner(), MessageType.SUCCESS, "Le warp " + warp.getName() + " a été supprimé.");
            new WarpMenu(getOwner()).open();
        }));

        contents.put(6, new ItemBuilder(this, Material.RED_STAINED_GLASS_PANE, meta -> {
            meta.displayName(Component.text("§cAnnuler la suppression"));
        }).setBackButton());

        return contents;
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.SMALLEST;
    }

    @Override
    public @NotNull String getName() {
        return "Confirmation de suppression du warp " + warp.getName();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent arg0) {
    }

}
