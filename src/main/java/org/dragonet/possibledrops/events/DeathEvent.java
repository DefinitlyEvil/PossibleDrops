package org.dragonet.possibledrops.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dragonet.possibledrops.PossibleDrops;

import java.util.Random;

public class DeathEvent implements Listener {
    PossibleDrops plugin = PossibleDrops.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent e) {
        if (e.getKeepLevel() && e.getKeepInventory()) return;

        Random random = new Random();
        // ============ ITEM DROP ============
            e.getDrops().clear();
            ItemStack[] inventoryStack = e.getEntity().getInventory().getContents();
            for (int i = 0; i < inventoryStack.length; i++) {
                ItemStack individualStack = inventoryStack[i];
                if (inventoryStack[i] == null || inventoryStack[i].getType().equals(Material.AIR)) continue;

                // check for LobbyMenu item
                if (individualStack.hasItemMeta()) {
                    ItemMeta itemMeta = individualStack.getItemMeta();
                    if (itemMeta.hasLore()) {
                        for (String l : itemMeta.getLore()) {
                            if (l.startsWith(PossibleDrops.LOBBY_MENU_PREFIX_MENU)) continue; // skip it!
                        }
                    }
                }

                double chance = plugin.config.getDouble("chances.general");
                Material material = individualStack.getType();
                String materialName = material.name();
                // start of material check
                if (material.isEdible()) {
                    // food
                    chance = plugin.config.getDouble("chances.specific.food");
                } else if (material.isFuel()) {
                    // burnable blocks
                    chance = plugin.config.getDouble("chances.specific.fuels", -1.0d);
                } else if (material.isBlock()) {
                    // building blocks
                    chance = plugin.config.getDouble("chances.specific.blocks", -1.0d);
                } else if (material.isItem()) {
                    // tools
                    if (materialName.endsWith("_SWORD") || materialName.endsWith("_PICKAXE") || materialName.endsWith("_SHOVEL") || materialName.endsWith("_HOE") || materialName.endsWith("_AXE")) {
                        if(materialName.startsWith("NETHERITE_")) {
                            chance = plugin.config.getDouble("chances.specific.tools.netherite", -1.0d);
                        } else if (materialName.startsWith("DIAMOND_")) {
                            chance = plugin.config.getDouble("chances.specific.tools.diamond", -1.0d);
                        } else if (materialName.startsWith("GOLDEN_")) {
                            chance = plugin.config.getDouble("chances.specific.tools.golden", -1.0d);
                        } else if (materialName.startsWith("IRON_")) {
                            chance = plugin.config.getDouble("chances.specific.tools.iron", -1.0d);
                        } else if (materialName.startsWith("STONE_")) {
                            chance = plugin.config.getDouble("chances.specific.tools.stone", -1.0d);
                        } else if (materialName.startsWith("WOODEN_")) {
                            chance = plugin.config.getDouble("chances.specific.tools.wooden", -1.0d);
                        }
                    } else if (materialName.endsWith("_HELMET") || materialName.endsWith("_CHESTPLATE") || materialName.endsWith("_LEGGINGS") || materialName.endsWith("_BOOTS")) {
                        if(materialName.startsWith("NETHERITE_")) {
                            chance = plugin.config.getDouble("chances.specific.armors.netherite", -1.0d);
                        } else if (materialName.startsWith("DIAMOND_")) {
                            chance = plugin.config.getDouble("chances.specific.armors.diamond", -1.0d);
                        } else if (materialName.startsWith("GOLDEN_")) {
                            chance = plugin.config.getDouble("chances.specific.armors.golden", -1.0d);
                        } else if (materialName.startsWith("IRON_")) {
                            chance = plugin.config.getDouble("chances.specific.armors.iron", -1.0d);
                        } else if (materialName.startsWith("LEATHER_")) {
                            chance = plugin.config.getDouble("chances.specific.armors.leather", -1.0d);
                        }
                    }
                }

                if (random.nextDouble() <= chance) {
                    e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), individualStack);
                    e.getEntity().getInventory().setItem(i, null);
                }
            }
            // TODO Research and fix exp drop system
/*        // ============ EXP DROP ============
        if (!e.getKeepLevel()) {
            int total_exp = e.getEntity().getTotalExperience();
            if (random.nextDouble() < plugin.config.getDouble("chances.exp.chance", 1.0d)) {
                double percentage = random.nextDouble() * plugin.config.getDouble("chances.exp.max-percentage", 1.0d);
                int p = (int) (percentage * 100.0d);
                int dropped = total_exp * p / 100;
                e.setDroppedExp(dropped);
            }
        }*/
    }
}
