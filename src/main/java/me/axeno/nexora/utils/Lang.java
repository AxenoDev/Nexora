package me.axeno.nexora.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.axeno.nexora.Nexora;

public class Lang {

    private static final Map<String, String> translations = new HashMap<>();

    public static void load() {
        translations.clear();

        String langCode = Nexora.getInstance().getConfig().getString("lang", "en_US");
        File file = new File(Nexora.getInstance().getDataFolder(), langCode + ".yml");

        if (!file.exists()) {
            Nexora.getInstance().saveResource(langCode + ".yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(true)) {
            translations.put(key, config.getString(key));
        }
    }

    public static String get(String key) {
        return translations.getOrDefault(key, key).replace("&", "§");
    }

    public static int getInt(String key) {
        if (!translations.containsKey(key))
            return 0;

        String value = translations.get(key).trim();

        try {
            if (value.startsWith("0x") || value.startsWith("0X")) {
                return Integer.parseInt(value.substring(2), 16);
            } else {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
