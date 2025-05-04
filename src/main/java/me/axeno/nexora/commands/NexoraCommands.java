package me.axeno.nexora.commands;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.Lang;
import me.axeno.nexora.utils.message.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("nexora")
@Description("Nexora commands.")
public class NexoraCommands {

    @Subcommand("reload")
    @Description("Reload the Nexora configuration.")
    @CommandPermission("nexora.admin.reload")
    public void reload(Player player) {
        Nexora.getInstance().reloadConfig();
        Lang.load();
        Nexora.sendMessage(player, MessageType.SUCCESS,
                Lang.get("config.reload.success"));
    }

    @Subcommand("help")
    @Description("Show help for warp commands.")
    public void help(@NotNull Player player) {
        Component title = Component.text("Nexora Warp Commands")
                .color(TextColor.color(0x55FFAA))
                .decorate(TextDecoration.BOLD);
        player.sendMessage(title);

        sendHelpLine(player, "/warp", "Open the warp menu");
        sendHelpLine(player, "/warp <name>", "Teleport to a warp");
        sendHelpLine(player, "/warp list", "Show all available warps");

        if (player.hasPermission("nexora.warp.admin.add")) {
            sendHelpLine(player, "/warp add <name>", "Create a warp at your position");
        }
        if (player.hasPermission("nexora.warp.admin.delete")) {
            sendHelpLine(player, "/warp delete <name>", "Delete a warp");
        }
        if (player.hasPermission("nexora.warp.admin.rename")) {
            sendHelpLine(player, "/warp rename <old> <new>", "Rename a warp");
        }
        if (player.hasPermission("nexora.warp.admin.changeicon")) {
            sendHelpLine(player, "/warp icon <name>", "Change warp icon to item in hand");
        }
        if (player.hasPermission("nexora.warp.admin.teleport.other")) {
            sendHelpLine(player, "/warp tp <player> <warp>", "Teleport a player to a warp");
        }
    }

    private void sendHelpLine(Player player, String command, String description) {
        Component commandText = Component.text(command)
                .color(TextColor.color(0xF8A94D));
        Component descriptionText = Component.text(" - " + description)
                .color(TextColor.color(0xFFFFFF));

        player.sendMessage(commandText.append(descriptionText));
    }
}
