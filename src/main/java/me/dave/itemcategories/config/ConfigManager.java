package me.dave.itemcategories.config;

import me.dave.itemcategories.ItemCategories;
import me.dave.itemcategories.util.SimplePlaceholder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ConfigManager {
    private final ItemCategories plugin = ItemCategories.getInstance();
    private final HashMap<String, List<Material>> categoryMap = new HashMap<>();
    private final HashMap<String, String> messages = new HashMap<>();

    public ConfigManager() {
        plugin.saveDefaultConfig();
        reloadConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        categoryMap.clear();
        messages.clear();

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

        ConfigurationSection messagesSection = config.getConfigurationSection("language");
        if (messagesSection != null) {
            messagesSection.getValues(false).forEach((name, message) -> messages.put(name, (String) message));
        }
    }

    public String getMessage(String name, SimplePlaceholder... placeholders) {
        return getMessage(name, null, placeholders);
    }

    public String getMessage(String name, String def, SimplePlaceholder... placeholders) {
        String message = messages.getOrDefault(name, def);

        for (SimplePlaceholder placeholder : placeholders) {
            message = placeholder.parse(message);
        }

        return messages.get(name);
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

