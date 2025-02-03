package de.chbya.modernblocking;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModernSwordBlockingCommand implements CommandExecutor, TabCompleter {
    private final ModernSwordBlockingConfig config;

    public ModernSwordBlockingCommand(ModernSwordBlockingConfig config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("msb")) {
            if (args.length == 1 && sender instanceof Player player) {
                return handlePlayerCommand(player, args[0]);
            } else if (args.length == 2 && sender instanceof Player player) {
                return handleOperatorCommand(player, args[0], args[1]);
            } else if (args.length == 2 && sender.hasPermission("msb.admin")) {
                return handleOperatorCommand(sender, args[0], args[1]);
            }
            sender.sendMessage("Usage: /msb <on|off> or /msb <player> <on|off>");
            return true;
        }
        return false;
    }

    private boolean handlePlayerCommand(Player player, String arg) {
        if (arg.equalsIgnoreCase("on")) {
            config.setSwordBlockingEnabled(player.getUniqueId(), true);
            player.sendMessage("Sword blocking enabled.");
            return true;
        } else if (arg.equalsIgnoreCase("off")) {
            config.setSwordBlockingEnabled(player.getUniqueId(), false);
            player.sendMessage("Sword blocking disabled.");
            return true;
        }
        return false;
    }

    private boolean handleOperatorCommand(CommandSender sender, String playerName, String arg) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }
        UUID playerUUID = target.getUniqueId();
        if (arg.equalsIgnoreCase("on")) {
            config.setSwordBlockingEnabled(playerUUID, true);
            sender.sendMessage("Sword blocking enabled for " + playerName + ".");
            target.sendMessage("Sword blocking enabled by an operator.");
            return true;
        } else if (arg.equalsIgnoreCase("off")) {
            config.setSwordBlockingEnabled(playerUUID, false);
            sender.sendMessage("Sword blocking disabled for " + playerName + ".");
            target.sendMessage("Sword blocking disabled by an operator.");
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("msb")) {
            if (args.length == 1) {
                List<String> completions = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
                completions.add("on");
                completions.add("off");
                return completions;
            } else if (args.length == 2) {
                List<String> completions = new ArrayList<>();
                completions.add("on");
                completions.add("off");
                return completions;
            }
        }
        return null;
    }
}