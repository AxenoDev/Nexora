package me.axeno.nexora.commands;

import org.bukkit.entity.Player;

import me.axeno.nexora.Nexora;
import me.axeno.nexora.utils.Lang;
import me.axeno.nexora.utils.message.MessageType;
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
}
