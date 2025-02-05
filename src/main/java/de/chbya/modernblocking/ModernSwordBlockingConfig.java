package de.chbya.modernblocking;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ModernSwordBlockingConfig {
    private final JavaPlugin plugin;
    private final Map<UUID, Boolean> playerBlockingEnabled = new HashMap<>();
    private List<String> enabledWorlds;

    public ModernSwordBlockingConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        enabledWorlds = config.getStringList("enabled-worlds");
        for (String key : config.getConfigurationSection("players").getKeys(false)) {
            UUID playerUUID = UUID.fromString(key);
            boolean enabled = config.getBoolean("players." + key + ".swordBlockingEnabled", true);
            playerBlockingEnabled.put(playerUUID, enabled);
        }
    }

    public boolean isSwordBlockingEnabled(UUID playerUUID) {
        return playerBlockingEnabled.getOrDefault(playerUUID, true);
    }

    public void setSwordBlockingEnabled(UUID playerUUID, boolean enabled) {
        playerBlockingEnabled.put(playerUUID, enabled);
        FileConfiguration config = plugin.getConfig();
        config.set("players." + playerUUID + ".swordBlockingEnabled", enabled);
        plugin.saveConfig();
    }

    public boolean isWorldEnabled(String worldName) {
        return enabledWorlds.contains(worldName);
    }
}