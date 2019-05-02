package org.dragonet.possibledrops;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Random;

public final class PossibleDrops extends JavaPlugin implements Listener {


    // LobbyMenu compatibility
    public final static String LOBBY_MENU_PREFIX_MENU = "\u00a70menu:";

    private YamlConfiguration config;

    @Override
    public YamlConfiguration getConfig() {
        return config;
    }

    public void reloadConfiguration() {
        getLogger().info("Loading configuration... ");
        saveResource("config.yml", false);
        config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
    }

    @Override
    public void onEnable() {
        reloadConfiguration();

        getLogger().info("Registering events... ");
        getServer().getPluginManager().registerEvents(this, this);

        getCommand("possibledrops").setExecutor(new PossibleDropsCommand(this));

        getLogger().info("Plugin enabled! ");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent e) {
        if(e.getKeepLevel() && e.getKeepInventory()) return;

        Random random = new Random();
        // ============ ITEM DROP ============
        if (!e.getKeepInventory()) {
            e.setKeepInventory(true);

            ItemStack[] inv = e.getEntity().getInventory().getContents();
            for (int i = 0; i < inv.length; i++) {
                ItemStack s = inv[i];
                if (inv[i] == null || inv[i].getType().equals(Material.AIR)) continue;

                // check for LobbyMenu item
                if (s.hasItemMeta()) {
                    ItemMeta m = s.getItemMeta();
                    if (m.hasLore()) {
                        for (String l : m.getLore()) {
                            if (l.startsWith(LOBBY_MENU_PREFIX_MENU)) continue; // skip it!
                        }
                    }
                }

                double chance = -1.0d;
                Material mat = s.getType();
                // start of material check
                if (mat.isEdible()) {
                    // food
                    chance = config.getDouble("chances.specific.food");
                } else if (mat.isFuel()) {
                    // burnable blocks
                    chance = config.getDouble("chances.specific.fuels", -1.0d);
                } else if (mat.isBlock()) {
                    // building blocks
                    chance = config.getDouble("chances.specific.blocks", -1.0d);
                } else if (mat.isItem()) {
                    String mat_name = mat.name();
                    // tools
                    if (mat_name.endsWith("_SWORD") || mat_name.endsWith("_PICKAXE") || mat_name.endsWith("_SHOVEL") || mat_name.endsWith("_HOE") || mat_name.endsWith("_AXE")) {
                        if (mat_name.startsWith("DIAMOND_")) {
                            chance = config.getDouble("chances.specific.tools.diamond", -1.0d);
                        } else if (mat_name.startsWith("GOLDEN_")) {
                            chance = config.getDouble("chances.specific.tools.golden", -1.0d);
                        } else if (mat_name.startsWith("IRON_")) {
                            chance = config.getDouble("chances.specific.tools.iron", -1.0d);
                        } else if (mat_name.startsWith("STONE_")) {
                            chance = config.getDouble("chances.specific.tools.stone", -1.0d);
                        } else if (mat_name.startsWith("WOODEN_")) {
                            chance = config.getDouble("chances.specific.tools.wooden", -1.0d);
                        }
                    } else if (mat_name.endsWith("_HELMET") || mat_name.endsWith("_CHESTPLATE") || mat_name.endsWith("_LEGGINGS") || mat_name.endsWith("_BOOTS")) {
                        if (mat_name.startsWith("DIAMOND_")) {
                            chance = config.getDouble("chances.specific.armors.diamond", -1.0d);
                        } else if (mat_name.startsWith("GOLDEN_")) {
                            chance = config.getDouble("chances.specific.armors.golden", -1.0d);
                        } else if (mat_name.startsWith("IRON_")) {
                            chance = config.getDouble("chances.specific.armors.iron", -1.0d);
                        } else if (mat_name.startsWith("LEATHER_")) {
                            chance = config.getDouble("chances.specific.armors.leather", -1.0d);
                        }
                    }
                }
                // end of material check
                if (chance == -1.0d) {
                    chance = config.getDouble("chances.general", 1.0d);
                }
                boolean drop = random.nextDouble() <= chance;
                if (drop) {
                    e.getEntity().getWorld().dropItemNaturally(
                            e.getEntity().getLocation(),
                            s
                    );
                    e.getEntity().getInventory().setItem(i, null);
                }
            }
        }

        // ============ EXP DROP ============
        if (!e.getKeepLevel()) {
            int total_exp = e.getEntity().getTotalExperience();
            if (random.nextDouble() < config.getDouble("chances.exp.chance", 1.0d)) {
                double percentage = random.nextDouble() * config.getDouble("chances.exp.max-percentage", 1.0d);
                int p = (int)(percentage * 100.0d);
                int dropped = total_exp * p / 100;
                e.setDroppedExp(dropped);
            }
        }
    }
}
