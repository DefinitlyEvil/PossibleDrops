package org.dragonet.possibledrops;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.dragonet.possibledrops.commands.PossibleDropsCommand;
import org.dragonet.possibledrops.events.DeathEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public final class PossibleDrops extends JavaPlugin implements Listener {
    // LobbyMenu compatibility
    public final static String LOBBY_MENU_PREFIX_MENU = "\u00a70menu:";
    private static PossibleDrops instance;
    public YamlConfiguration config;

    public static PossibleDrops getInstance() {
        return instance;
    }

    @NotNull
    @Override
    public YamlConfiguration getConfig() {
        return config;
    }

    public void reloadConfiguration() {
        getLogger().info("Loading configuration... ");
        saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
    }

    @Override
    public void onEnable() {
        instance = this;
        reloadConfiguration();

        getLogger().info("Registering events... ");
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);

        Objects.requireNonNull(getCommand("possibledrops")).setExecutor(new PossibleDropsCommand());
        getLogger().info("Plugin enabled! ");
    }
}
