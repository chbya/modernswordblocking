package de.chbya.modernblocking;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModernSwordBlockingPaper extends JavaPlugin implements Listener {
    private static final long UPDATE_INTERVAL = 5L; // 20 ticks = 1 second

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        startInventoryUpdater();
    }

    /**
     * Updates the necessary components on all swords in an inventory
     *
     * @param inventory the inventory
     * @param add       whether to add or remove the component that allows blocking the sword
     */
    private static void updateAllItems(@NotNull Inventory inventory, boolean add) {
        for (@Nullable ItemStack stack : inventory.getContents()) {
            if (stack == null) continue;
            if (add) ModernSwordBlocking.addSwordComponents(((CraftItemStack) stack).handle);
            else ModernSwordBlocking.removeSwordComponents(((CraftItemStack) stack).handle);
        }
    }

    /**
     * Starts a repeating task that updates all players' inventories to ensure sword components are added
     */
    private void startInventoryUpdater() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                updateAllItems(player.getInventory(), true);
            }
        }, 0L, UPDATE_INTERVAL);
    }

    // When items transferred to player inventory, make swords blockable
    // When items transferred to different inventory, remove component
    @EventHandler
    public void onInventoryUpdate(@NotNull InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        boolean isPlayerInventory = event.getClickedInventory().getHolder() instanceof Player;
        updateAllItems(event.getClickedInventory(), isPlayerInventory);
    }

    // When player joins, make all their swords blockable
    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        updateAllItems(event.getPlayer().getInventory(), true);
    }

    // Reduce damage by 50% when sword is blocked
    @EventHandler
    public void onDamagePlayer(@NotNull EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            event.setDamage(event.getDamage() * ModernSwordBlocking.damageMultiplier(((CraftPlayer) player).getHandle()));
        }
    }
}