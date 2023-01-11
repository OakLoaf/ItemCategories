package me.dave.itemcategories;

import me.dave.chatcolorhandler.ChatColorHandler;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class CategoryCmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
        if (!(sender instanceof Player player)) {
            ChatColorHandler.sendMessage(sender, "Console cannot run this command!");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("itemcategories.admin.reload")) {
                    ChatColorHandler.sendMessage(sender, "&cYou have insufficient permissions.");
                    return true;
                }
                ItemCategories.configManager.reloadConfig();
                ChatColorHandler.sendMessage(sender, ItemCategories.configManager.getReloadMessage());
                return true;
            }
        }

        if (!sender.hasPermission("itemcategories.command.categories")) {
            ChatColorHandler.sendMessage(sender, "§cYou have insufficient permissions.");
            return true;
        }
        PlayerInventory playerInventory = player.getInventory();
        ItemStack heldItem = playerInventory.getItemInMainHand();
        if (heldItem.getType() == Material.AIR) heldItem = playerInventory.getItemInOffHand();
        Material itemType = heldItem.getType();
        if (itemType == Material.AIR) {
            ChatColorHandler.sendMessage(sender, ItemCategories.configManager.getNoItemMessage());
            return true;
        }
        List<String> categories = ItemCategories.configManager.getMaterialCategories(itemType);
        if (categories.size() == 0) {
            ChatColorHandler.sendMessage(sender, ItemCategories.configManager.getNoCategoryMessage(itemType));
            return  true;
        }
        ChatColorHandler.sendMessage(sender, ItemCategories.configManager.getCategoriesMessage(itemType, categories));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {

        List<String> tabComplete = new ArrayList<>();
        List<String> wordCompletion = new ArrayList<>();
        boolean wordCompletionSuccess = false;

        if (args.length == 1) {
            if (commandSender.hasPermission("itemcategories.admin.reload")) tabComplete.add("reload");
        }

        for (String currTab : tabComplete) {
            int currArg = args.length - 1;
            if (currTab.startsWith(args[currArg])) {
                wordCompletion.add(currTab);
                wordCompletionSuccess = true;
            }
        }
        if (wordCompletionSuccess) return wordCompletion;
        return tabComplete;
    }
}
