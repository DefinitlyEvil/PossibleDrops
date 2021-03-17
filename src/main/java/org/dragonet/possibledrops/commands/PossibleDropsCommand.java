package org.dragonet.possibledrops.commands;

import org.jetbrains.annotations.NotNull;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.dragonet.possibledrops.PossibleDrops;

import java.util.ArrayList;
import java.util.List;

public class PossibleDropsCommand implements TabExecutor {
    PossibleDrops plugin = PossibleDrops.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("reload")) {
                if(sender instanceof Player) {
                    if(sender.hasPermission("possibledrops.reload")) {
                        plugin.reloadConfiguration();
                        sender.sendMessage(ChatColor.GREEN + "The PossibleDrops config has been reloaded.");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command.");
                        return true;
                    }
                } else {
                    plugin.reloadConfiguration();
                    sender.sendMessage(ChatColor.GREEN + "The PossibleDrops config has been reloaded.");
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {

        if (args.length == 1) {

            List<String> arguments = new ArrayList<>();

            arguments.add("reload");
            return arguments;

        }

        return null;

    }
}
