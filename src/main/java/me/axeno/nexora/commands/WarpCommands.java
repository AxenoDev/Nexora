package me.axeno.nexora.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.Lang;
import me.axeno.nexora.utils.message.MessageType;
import me.axeno.nexora.warp.Warp;
import me.axeno.nexora.warp.menus.WarpMenu;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("warp")
@Description("Manage warps.")
@CommandPermission("nexora.warp")
public class WarpCommands implements Listener {

    private final Nexora nexora;

    public WarpCommands() {
        this.nexora = Nexora.getInstance();
    }

    @Subcommand("list")
    @Description("Open the warp list menu.")
    @CommandPermission("nexora.warp.list")
    public void list(@NotNull Player player) {
        new WarpMenu(player).open();
    }

    @DefaultFor("~")
    @Description("Manage warps.")
    @CommandPermission("nexora.warp")
    @AutoComplete("@warps *")
    public void warp(@NotNull Player player, @Optional @Named("Warp") String warpName) {
        if (warpName == null || warpName.isEmpty()) {
            new WarpMenu(player).open();
        } else {
            if (!nexora.getWarpManager().teleportToWarp(player, warpName)) {
                String message = Lang.get("warp.not_found").replace("{warp.name}", warpName);
                Nexora.sendMessage(player, MessageType.ERROR, message);
            }
        }
    }

    @Subcommand("add")
    @Description("Create a warp at your position.")
    @CommandPermission("nexora.warp.admin.add")
    public void set(@NotNull Player player, @Named("Warp") String warpName) {
        ItemStack item = new ItemStack(Material.RED_BED);
        if (nexora.getWarpManager().createWarp(warpName, player.getLocation(), item)) {
            String message = Lang.get("warp.created").replace("{warp.name}", warpName);
            Nexora.sendMessage(player, MessageType.SUCCESS, message);
        } else {
            String message = Lang.get("warp.exists").replace("{warp.name}", warpName);
            Nexora.sendMessage(player, MessageType.ERROR, message);
        }
    }

    @Subcommand("delete")
    @Description("Delete a warp.")
    @CommandPermission("nexora.warp.admin.delete")
    @AutoComplete("@warps *")
    public void delete(@NotNull Player player, @Named("warp") String warpName) {
        Warp warp = nexora.getWarpManager().getWarp(warpName);
        if (warp == null) {
            String message = Lang.get("warp.exists").replace("{warp.name}", warpName);
            Nexora.sendMessage(player, MessageType.ERROR, message);
            return;
        }

        if (nexora.getWarpManager().deleteWarp(warp)) {
            String message = Lang.get("warp.deleted").replace("{warp.name}", warpName);
            Nexora.sendMessage(player, MessageType.SUCCESS, message);
        } else {
            String message = Lang.get("warp.deletion_error");
            Nexora.sendMessage(player, MessageType.ERROR, message);
        }
    }

    @Subcommand("rename")
    @Description("Rename a warp.")
    @CommandPermission("nexora.warp.admin.rename")
    @AutoComplete("@warps *")
    public void rename(@NotNull Player player,
            @Named("warp") String oldName,
            @Named("name") String newName) {
        if (nexora.getWarpManager().renameWarp(oldName, newName)) {
            String message = Lang.get("warp.renamed")
                    .replace("{warp.old_name}", oldName)
                    .replace("{warp.new_name}", newName);
            Nexora.sendMessage(player, MessageType.SUCCESS, message);
        } else {
            String message = Lang.get("warp.not_found")
                    .replace("{warp.name}", oldName);
            Nexora.sendMessage(player, MessageType.ERROR, message);
        }
    }

    @Subcommand("icon")
    @Description("Change warp icon to item in hand.")
    @CommandPermission("nexora.warp.admin.changeicon")
    @AutoComplete("@warps *")
    public void icon(@NotNull Player player, @Named("warp") String warpName) {
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (handItem == null || handItem.getType().equals(Material.AIR)) {
            Nexora.sendMessage(player, MessageType.ERROR, Lang.get("warp.icon.no_item"));
            return;
        }

        if (nexora.getWarpManager().changeWarpIcon(warpName, handItem)) {
            Nexora.sendMessage(player, MessageType.SUCCESS,
                    Lang.get("warp.icon.updated").replace("{warp.name}", warpName));
        } else {
            Nexora.sendMessage(player, MessageType.ERROR, Lang.get("warp.icon.error").replace("{warp.name}", warpName));
        }
    }

    @Subcommand("tp")
    @Description("Teleport a player to a warp.")
    @CommandPermission("nexora.warp.admin.teleport.other")
    @AutoComplete("@players @warps")
    public void teleport(@NotNull Player player, @Named("target") Player target, @Named("warp") String warpName) {
        if (nexora.getWarpManager().teleportToWarp(target, warpName)) {
            Nexora.sendMessage(player, MessageType.SUCCESS, Lang.get("warp.teleport.success.other")
                    .replace("{target}", target.getName())
                    .replace("{warp.name}", warpName));
            Nexora.sendMessage(target, MessageType.INFO, Lang.get("warp.teleport.info.target")
                    .replace("{warp.name}", warpName)
                    .replace("{player}", player.getName()));
        } else {
            Nexora.sendMessage(player, MessageType.ERROR, Lang.get("warp.not_found").replace("{warp.name}", warpName));
        }
    }
}
