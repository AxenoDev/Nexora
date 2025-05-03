package me.axeno.nexora;

import lombok.Getter;
import me.axeno.nexora.commands.WarpCommands;
import me.axeno.nexora.utils.message.MessageType;
import me.axeno.nexora.warp.WarpManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.util.Locale;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.xernas.menulib.MenuLib;

public final class Nexora extends JavaPlugin {

    @Getter
    public WarpManager warpManager;
    @Getter
    private static Nexora instance;
    @Getter
    static BukkitCommandHandler handler;

    public static final Component BASE_PREFIX = MiniMessage.miniMessage().deserialize(
            "<gradient:#00F0FF:#00B7FF>@ɴᴇхᴏʀᴀ</gradient> ");

    @Override
    public void onEnable() {
        instance = this;
        this.warpManager = new WarpManager();

        handler = BukkitCommandHandler.create(this);

        handler.getAutoCompleter().registerSuggestion("warps", (args, sender, command) -> {
            return warpManager.getWarps().keySet().stream().toList();
        });

        handler.register(
                new WarpCommands());

        handler.getTranslator().setLocale(Locale.FRENCH);
        MenuLib.init(this);

        getLogger().info("Nexora has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Nexora has been disabled!");

        for (Player player : getServer().getOnlinePlayers()) {
            player.closeInventory();
        }
    }

    public static void sendMessage(Player player, MessageType type, String content) {
        Component badge = Component.text("(")
                .color(TextColor.color(0x3A3A3A))
                .append(Component.text(type.symbol).color(type.color))
                .append(Component.text(")").color(TextColor.color(0x3A3A3A)))
                .append(Component.space());

        Component arrow = Component.text("»")
                .color(TextColor.color(0xAAAAAA))
                .append(Component.space());

        Component message = Component.empty()
                .append(badge)
                .append(BASE_PREFIX)
                .append(Component.space())
                .append(arrow)
                .append(Component.text(content).color(TextColor.color(0xFFFFFF)));

        player.sendMessage(message);
        player.playSound(player.getLocation(), type.sound, 1f, 1f);
    }

}
