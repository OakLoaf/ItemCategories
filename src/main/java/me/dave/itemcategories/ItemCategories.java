package me.dave.itemcategories;

import org.bukkit.plugin.java.JavaPlugin;

public final class ItemCategories extends JavaPlugin {
    private static ItemCategories plugin;
    public static ConfigManager configManager;

    @Override
    public void onEnable() {
        plugin = this;
        configManager = new ConfigManager();

        getCommand("categories").setExecutor(new CategoryCmd());
    }

    public static ItemCategories getInstance() { return plugin; }
}
