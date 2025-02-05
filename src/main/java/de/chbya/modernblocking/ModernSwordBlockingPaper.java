package de.chbya.modernblocking;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModernSwordBlockingPaper extends JavaPlugin implements Listener {
    private static final long UPDATE_INTERVAL = 5L; // 20 ticks = 1 second
    private ModernSwordBlockingConfig config;

    @Override
    public void onEnable() {
        config = new ModernSwordBlockingConfig(this);
        getServer().getPluginManager().registerEvents(this, this);
        startInventoryUpdater();
        PluginCommand msbCommand = getCommand("msb");
        if (msbCommand != null) {
            ModernSwordBlockingCommand commandExecutor = new ModernSwordBlockingCommand(config);
            msbCommand.setExecutor(commandExecutor);
            msbCommand.setTabCompleter(commandExecutor);
        }
    }

    public ModernSwordBlockingConfig getConfigManager() {
        return config;
    }

    private static void updateAllItems(@NotNull Inventory inventory, boolean add) {
        for (@Nullable ItemStack stack : inventory.getContents()) {
            try {
                if (stack == null || !(stack instanceof CraftItemStack craftStack)) continue;
                if (add) {
                    ModernSwordBlocking.addSwordComponents(craftStack.handle);
                } else {
                    ModernSwordBlocking.removeSwordComponents(craftStack.handle);
                }
            } catch (Exception ignored) {
                // Ignore all exceptions to prevent console errors
            }
        }
    }

    private void startInventoryUpdater() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (config.isSwordBlockingEnabled(player.getUniqueId())) {
                    updateAllItems(player.getInventory(), true);
                } else {
                    updateAllItems(player.getInventory(), false);
                }
            }
        }, 0L, UPDATE_INTERVAL);
    }

    @EventHandler
    public void onInventoryUpdate(@NotNull InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        boolean isPlayerInventory = event.getClickedInventory().getHolder() instanceof Player;
        if (isPlayerInventory) {
            Player player = (Player) event.getClickedInventory().getHolder();
            if (config.isSwordBlockingEnabled(player.getUniqueId())) {
                updateAllItems(event.getClickedInventory(), true);
            } else {
                updateAllItems(event.getClickedInventory(), false);
            }
        }
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (config.isSwordBlockingEnabled(player.getUniqueId())) {
            updateAllItems(player.getInventory(), true);
        } else {
            updateAllItems(player.getInventory(), false);
        }
    }

    @EventHandler
    public void onDamagePlayer(@NotNull EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (config.isSwordBlockingEnabled(player.getUniqueId())) {
                event.setDamage(event.getDamage() * ModernSwordBlocking.damageMultiplier(((CraftPlayer) player).getHandle()));
            }
        }
    }
}