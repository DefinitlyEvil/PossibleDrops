package org.dragonet.possibledrops;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class PossibleDropsCommand implements CommandExecutor {

    private final PossibleDrops plugin;

    public PossibleDropsCommand(PossibleDrops plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender == null) return true;
        if(!ConsoleCommandSender.class.isAssignableFrom(sender.getClass()) && !RemoteConsoleCommandSender.class.isAssignableFrom(sender.getClass())) {
            if (Player.class.isAssignableFrom(sender.getClass())) {
                if (!sender.hasPermission("possibledrops.reload")) {
                    sender.sendMessage("\u00a7cno permission");
                    return true;
                }
            }
            sender.sendMessage("\u00a7cno permission");
            return true;
        }

        plugin.reloadConfiguration();

        sender.sendMessage("\u00a7aconfig reloaded! ");
        return true;
    }
}
