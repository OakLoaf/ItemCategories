package me.dave.itemcategories;

import me.dave.itemcategories.command.CategoryCommand;
import me.dave.itemcategories.config.ConfigManager;
import me.dave.platyutils.plugin.SpigotPlugin;

public final class ItemCategories extends SpigotPlugin {
    private static ItemCategories plugin;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        plugin = this;
        configManager = new ConfigManager();

        registerCommand(new CategoryCommand());
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static ItemCategories getInstance() {
        return plugin;
    }
}
