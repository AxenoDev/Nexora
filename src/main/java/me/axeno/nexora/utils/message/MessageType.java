package me.axeno.nexora.utils.message;

import org.bukkit.Sound;
import net.kyori.adventure.text.format.TextColor;

public enum MessageType {
    SUCCESS("✔", TextColor.color(0x3BFF5A), Sound.BLOCK_NOTE_BLOCK_PLING),
    ERROR("✘", TextColor.color(0xFF4D4D), Sound.BLOCK_NOTE_BLOCK_BASS),
    WARNING("⚠", TextColor.color(0xFFC933), Sound.BLOCK_NOTE_BLOCK_BANJO),
    INFO("ℹ", TextColor.color(0x33D3FF), Sound.UI_BUTTON_CLICK);

    public final String symbol;
    public final TextColor color;
    public final Sound sound;

    MessageType(String symbol, TextColor color, Sound sound) {
        this.symbol = symbol;
        this.color = color;
        this.sound = sound;
    }
}
