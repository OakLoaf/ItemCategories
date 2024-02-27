package me.dave.itemcategories.command;

import me.dave.itemcategories.ItemCategories;
import me.dave.itemcategories.util.SimplePlaceholder;
import me.dave.platyutils.command.Command;
import me.dave.platyutils.command.SubCommand;
import me.dave.platyutils.libraries.chatcolor.ChatColorHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class CategoryCommand extends Command {
    private static List<String> MATERIAL_NAMES_CACHE = null;

    public CategoryCommand() {
        super("categories");
        addRequiredPermission("itemcategories.command.categories");
        addSubCommand(new ReloadCommand());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Material material;
        if (args.length == 1) {
            try {
                material = Material.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                ChatColorHandler.sendMessage(sender, "");
                return true;
            }
        } else {
            if (!(sender instanceof Player player)) {
                ChatColorHandler.sendMessage(sender, "Console cannot run this command!");
                return true;
            }

            PlayerInventory playerInventory = player.getInventory();
            ItemStack heldItem = playerInventory.getItemInMainHand();
            if (heldItem.getType() == Material.AIR) heldItem = playerInventory.getItemInOffHand();
            material = heldItem.getType();
        }

        if (material == Material.AIR) {
            ChatColorHandler.sendMessage(sender, ItemCategories.getInstance().getConfigManager().getMessage("no-item", "&cYou are not holding an item"));
            return true;
        }

        List<String> categories = ItemCategories.getInstance().getConfigManager().getMaterialCategories(material);
        if (categories.size() == 0) {
            ChatColorHandler.sendMessage(sender, ItemCategories.getInstance().getConfigManager().getMessage("no-category", "&b%material% §7is not in any categories", new SimplePlaceholder("%material%", material.name())));
            return  true;
        }

        ChatColorHandler.sendMessage(sender, ItemCategories.getInstance().getConfigManager().getMessage("message-format", "&b%material% &7is in the following categories: &6%categories%", new SimplePlaceholder("%material%", material.name()), new SimplePlaceholder("%categories%", categories.toString())));
        return true;
    }

    @Override
    public @Nullable List<String> tabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("itemcategories.command.categories")) {
            if (MATERIAL_NAMES_CACHE == null) {
                MATERIAL_NAMES_CACHE = Arrays.stream(Material.values()).map(material -> material.toString().toLowerCase()).toList();
                Bukkit.getScheduler().runTaskLaterAsynchronously(ItemCategories.getInstance(), () -> MATERIAL_NAMES_CACHE = null, 1200);
            }

            return MATERIAL_NAMES_CACHE;
        }
        
        return null;
    }

    private static class ReloadCommand extends SubCommand {

        public ReloadCommand() {
            super("reload");
            addRequiredPermission("itemcategories.admin.reload");
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            ItemCategories.getInstance().getConfigManager().reloadConfig();
            ChatColorHandler.sendMessage(sender, ItemCategories.getInstance().getConfigManager().getMessage("reload", "&aItemCategories has been reloaded"));
            return true;
        }
    }
}
