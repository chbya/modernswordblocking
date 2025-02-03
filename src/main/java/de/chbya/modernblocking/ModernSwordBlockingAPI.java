package de.chbya.modernblocking;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
                throw new IllegalStateException("ModernSwordBlocking not found or not initialized.");
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

    public boolean isSwordBlockingEnabled(Player player) {
        return isSwordBlockingEnabled(player.getUniqueId());
    }

    public void setSwordBlockingEnabled(Player player, boolean enabled) {
        setSwordBlockingEnabled(player.getUniqueId(), enabled);
    }

    public void enableSwordBlocking(Player player) {
        setSwordBlockingEnabled(player, true);
    }

    public void disableSwordBlocking(Player player) {
        setSwordBlockingEnabled(player, false);
    }

    public void enableSwordBlocking(UUID playerUUID) {
        setSwordBlockingEnabled(playerUUID, true);
    }

    public void disableSwordBlocking(UUID playerUUID) {
        setSwordBlockingEnabled(playerUUID, false);
    }
}