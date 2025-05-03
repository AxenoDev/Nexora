package me.axeno.nexora.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.message.MessageType;
import me.axeno.nexora.warp.Warp;
import me.axeno.nexora.warp.menus.WarpMenu;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("warp")
@Description("Gère les warps.")
@CommandPermission("nexora.warp")
public class WarpCommands implements Listener {

    private Nexora nexora;

    public WarpCommands() {
        this.nexora = Nexora.getInstance();
    }

    @Subcommand("list")
    @Description("Ouvre le menu des warps.")
    @CommandPermission("nexora.warp.list")
    public void list(@NotNull Player player) {
        new WarpMenu(player).open();
    }

    @DefaultFor("~")
    @Description("Gère les warps.")
    @CommandPermission("nexora.warp")
    @AutoComplete("@warps *")
    public void warp(@NotNull Player player, @Optional @Named("Warp") String warpName) {
        if (warpName == null || warpName.isEmpty())
            new WarpMenu(player).open();
        else {
            if (nexora.getWarpManager().teleportToWarp(player, warpName)) {
            } else {
                Nexora.sendMessage(player, MessageType.ERROR, "§cLe warp " + warpName + " n'existe pas.");
            }
        }
    }

    @Subcommand("add")
    @Description("Crée un warp à votre position.")
    @CommandPermission("nexora.warp.admin.add")
    public void set(@NotNull Player player, @Named("Warp") String warpName) {
        ItemStack item = new ItemStack(Material.RED_BED);
        if (nexora.getWarpManager().createWarp(warpName, player.getLocation(), item))
            Nexora.sendMessage(player, MessageType.SUCCESS, "§7Le warp §6" + warpName + "§7 a été créé.");
        else
            Nexora.sendMessage(player, MessageType.ERROR, "§cLe warp " + warpName + " existe déjà.");
    }

    @Subcommand("delete")
    @Description("Supprime un warp existant.")
    @CommandPermission("nexora.warp.admin.delete")
    @AutoComplete("@warps *")
    public void delete(@NotNull Player player, @Named("warp") String warpName) {
        Warp warp = nexora.getWarpManager().getWarp(warpName);
        if (warp == null) {
            Nexora.sendMessage(player, MessageType.ERROR, "§cLe warp " + warpName + " n'existe pas.");
            return;
        }

        if (nexora.getWarpManager().deleteWarp(warp)) {
            Nexora.sendMessage(player, MessageType.SUCCESS, "§7Le warp §6" + warpName + "§7 a été supprimé.");
        } else {
            Nexora.sendMessage(player, MessageType.ERROR, "§cErreur lors de la suppression du warp.");
        }
    }

    @Subcommand("rename")
    @Description("Renomme un warp.")
    @CommandPermission("nexora.warp.admin.rename")
    @AutoComplete("@warps *")
    public void rename(@NotNull Player player,
            @Named("warp") String oldName,
            @Named("name") String newName) {
        if (nexora.getWarpManager().renameWarp(oldName, newName)) {
            Nexora.sendMessage(player, MessageType.SUCCESS,
                    "§7Le warp §6" + oldName + "§7 a été renommé en §6" + newName + "§7.");
        } else {
            Nexora.sendMessage(player, MessageType.ERROR, "§cLe warp n'existe pas.");
        }
    }

    @Subcommand("icon")
    @Description("Change l'icône du warp selon l'item dans votre main.")
    @CommandPermission("nexora.warp.admin.changeicon")
    @AutoComplete("@warps *")
    public void icon(@NotNull Player player, @Named("warp") String warpName) {
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (handItem == null || handItem.getType().equals(Material.AIR)) {
            Nexora.sendMessage(player, MessageType.ERROR, "§cVeuillez tenir un item en main.");
            return;
        }

        if (nexora.getWarpManager().changeWarpIcon(warpName, handItem)) {
            Nexora.sendMessage(player, MessageType.SUCCESS, "§7L’icône du warp §6" + warpName + "§7 a été modifiée.");
        } else {
            Nexora.sendMessage(player, MessageType.ERROR,
                    "§cLe warp " + warpName + " n’existe pas ou l’item est invalide.");
        }
    }

    @Subcommand("tp")
    @Description("Téléporte le joueur au warp.")
    @CommandPermission("nexora.warp.admin.teleport.other")
    @AutoComplete("@players @warps")
    public void teleport(@NotNull Player player, @Named("target") Player target, @Named("warp") String warpName) {
        if (nexora.getWarpManager().teleportToWarp(player, warpName)) {
            Nexora.sendMessage(player, MessageType.SUCCESS,
                    "§7Le joueur §6" + target.getName() + "§7 a été téléporté au warp §6" + warpName + "§7.");
            Nexora.sendMessage(target, MessageType.INFO,
                    "§7Vous avez été téléporté au warp §6" + warpName + "§7 par §6" + player.getName() + "§7.");
        } else {
            Nexora.sendMessage(player, MessageType.ERROR, "§cLe warp " + warpName + " n'existe pas.");
        }
    }
}