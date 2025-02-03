package de.chbya.modernblocking;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class ModernSwordBlockingAPI {
    private static ModernSwordBlockingAPI instance;
    private final ModernSwordBlockingConfig config;

    private ModernSwordBlockingAPI(ModernSwordBlockingConfig config) {
        this.config = config;
    }

    public static ModernSwordBlockingAPI getInstance() {
        if (instance == null) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("ModernSwordBlocking");
            if (plugin instanceof ModernSwordBlockingPaper) {
                instance = new ModernSwordBlockingAPI(((ModernSwordBlockingPaper) plugin).getConfigManager());
            } else {
                throw new IllegalStateException("ModernSwordBlocking plugin not found or not initialized.");
            }
        }
        return instance;
    }

    public boolean isSwordBlockingEnabled(UUID playerUUID) {
        return config.isSwordBlockingEnabled(playerUUID);
    }

    public void setSwordBlockingEnabled(UUID playerUUID, boolean enabled) {
        config.setSwordBlockingEnabled(playerUUID, enabled);
    }
}