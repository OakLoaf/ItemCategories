package me.dave.itemcategories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ConfigManager {
    private FileConfiguration config;
    private final ItemCategories plugin = ItemCategories.getInstance();
    private final HashMap<String, List<Material>> categoryMap = new HashMap<>();

    public ConfigManager() {
        plugin.saveDefaultConfig();
        reloadConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        categoryMap.clear();

        ConfigurationSection categoriesSection = config.getConfigurationSection("categories");
        if (categoriesSection != null) {
            for (String categoryName : categoriesSection.getKeys(false)) {
                List<Material> materials = new ArrayList<>();
                for (String itemName : categoriesSection.getStringList(categoryName)) {
                    materials.add(Material.valueOf(itemName.toUpperCase()));
                }
                categoryMap.put(categoryName, materials);
            }
        }
    }

    public String getCategoriesMessage(Material material, List<String> categories) {
        return config.getString("language.message-format", "&b%material% &7is in the following categories: &6%categories%").replaceAll("%material%", material.name()).replaceAll("%categories%", categories.toString());
    }

    public String getNoCategoryMessage(Material material) {
        return config.getString("language.no-category", "&b%material% §7is not in any categories").replaceAll("%material%", material.name());
    }

    public String getNoItemMessage() {
        return config.getString("language.no-item", "&cYou are not holding an item");
    }

    public String getReloadMessage() {
        return config.getString("language.reload", "&aItemCategories has been reloaded");
    }

    public List<String> getMaterialCategories(Material material) {
        List<String> categoriesIn = new ArrayList<>();
        for (String categoryName : categoryMap.keySet()) {
            if (categoryMap.get(categoryName).contains(material)) {
                categoriesIn.add(categoryName);
            }
        }
        return categoriesIn;
    }
}

