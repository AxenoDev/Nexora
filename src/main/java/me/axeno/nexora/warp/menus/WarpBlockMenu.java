package me.axeno.nexora.warp.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.axeno.nexora.api.menulib.PaginatedMenu;
import me.axeno.nexora.api.menulib.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.exception.SignGUIVersionException;
import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.Lang;
import me.axeno.nexora.utils.message.MessageType;
import me.axeno.nexora.warp.Warp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class WarpBlockMenu extends PaginatedMenu {

    final Warp warp;
    private String searchQuery = "";
    private static final List<ItemStack> CACHED_ITEMS = new ArrayList<>();

    public WarpBlockMenu(Player Player, Warp warp) {
        super(Player);
        this.warp = warp;
        this.searchQuery = "";
        refreshCachedItems();
    }

    public WarpBlockMenu(Player Player, Warp warp, String searchQuery) {
        super(Player);
        this.warp = warp;
        this.searchQuery = searchQuery;
        refreshCachedItems();
    }

    private void refreshCachedItems() {
        CACHED_ITEMS.clear();

        for (Material material : Material.values()) {
            if (!material.isItem() || material.isAir())
                continue;

            String name = material.name().replace("_", " ").toLowerCase();

            if (!searchQuery.isEmpty() && !name.contains(searchQuery.toLowerCase())) {
                continue;
            }

            ItemStack item = new ItemBuilder(null, material, meta -> {
                meta.displayName(Component.text(name)
                        .color(TextColor.color(0x55FFAA))
                        .decoration(TextDecoration.ITALIC, false));
                if (warp.getItemStack().getType() == material) {
                    meta.lore(List.of(Component.text(Lang.get("menu.warp.block.selected"))));
                    meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                } else {
                    meta.lore(List.of(Component.text(Lang.get("menu.click_to_select"))));
                }
            });
            CACHED_ITEMS.add(item);
        }
    }

    @Override
    public @NotNull String getName() {
        return Lang.get("menu.warp.block.title")
                .replace("{warp.name}", warp.getName());
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
            Nexora.sendMessage(getOwner(), MessageType.SUCCESS, Lang.get("warp.icon.updated"));
        } else {
            Nexora.sendMessage(getOwner(), MessageType.ERROR, Lang.get("warp.icon.error"));
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
            meta.displayName(Component.text(Lang.get("menu.page.previous")));
        }).setPreviousPageButton());

        btns.put(47, new ItemBuilder(this, warp.getItemStack(), meta -> {
            meta.displayName(Component.text(warp.getName())
                    .color(TextColor.color(0xF8C44D))
                    .decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(Lang.get("menu.warp.coordinates.name")));
            lore.add(Component.text(Lang.get("menu.warp.coordinates.x").replace("{x}",
                    String.format("%.0f", warp.getLocation().getX()))));
            lore.add(Component.text(Lang.get("menu.warp.coordinates.y").replace("{y}",
                    String.format("%.0f", warp.getLocation().getY()))));
            lore.add(Component.text(Lang.get("menu.warp.coordinates.z").replace("{z}",
                    String.format("%.0f", warp.getLocation().getZ()))));
            lore.add(Component
                    .text(Lang.get("menu.warp.coordinates.world").replace("{world}",
                            warp.getLocation().getWorld().getName())));
            meta.lore(lore);
        }));

        btns.put(49, new ItemBuilder(this, Material.BARRIER, meta -> {
            meta.displayName(Component.text(Lang.get("menu.close")));
        }).setCloseButton());

        btns.put(51, new ItemBuilder(this, Material.OAK_SIGN, meta -> {
            meta.displayName(Component.text("§8[§6🔎§8] " +
                    (searchQuery.isEmpty() ? Lang.get("menu.warp.block.search.search")
                            : Lang.get("menu.warp.block.search.searching").replace("{query}", searchQuery))));
            meta.lore(List.of(
                    Component.text(Lang.get("menu.warp.block.click_to_search")),
                    Component.text(Lang.get("menu.warp.block.click_to_reset"))));
        }).setOnClick(e -> {
            if (e.isLeftClick()) {
                SignGUI signGUI;
                try {
                    String[] lines = new String[4];
                    lines[0] = searchQuery;
                    lines[1] = Lang.get("menu.warp.block.sign.line2");
                    lines[2] = Lang.get("menu.warp.block.sign.line3");
                    lines[3] = Lang.get("menu.warp.block.sign.line4");

                    signGUI = SignGUI.builder()
                            .setLines(lines[0], lines[1], lines[2], lines[3])
                            .setType(Material.OAK_SIGN)
                            .setHandler((p, result) -> {
                                String input = result.getLine(0);
                                searchQuery = input.toLowerCase();
                                refreshCachedItems();
                                Bukkit.getScheduler().runTaskLater(Nexora.getInstance(), () -> {
                                    new WarpBlockMenu(p, warp, searchQuery).open();
                                }, 2L); // Burk... a delay, because packets hate smooth workflows

                                return Collections.emptyList();
                            }).build();

                } catch (SignGUIVersionException exception) {
                    throw new RuntimeException(exception);
                }
                signGUI.open(getOwner());
            } else if (e.isRightClick()) {
                searchQuery = "";
                refreshCachedItems();
                Bukkit.getScheduler().runTaskLater(Nexora.getInstance(), () -> {
                    new WarpBlockMenu(getOwner(), warp, searchQuery).open();
                }, 2L); // Burk... a delay, because packets hate smooth workflows
            }
        }));

        btns.put(53, new ItemBuilder(this, Material.ARROW, meta -> {
            meta.displayName(Component.text(Lang.get("menu.page.next")));
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
