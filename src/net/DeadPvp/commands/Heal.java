package net.DeadPvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("deadpvp.heal") || p.hasPermission("deadpvp.*")) {
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                p.setSaturation(20);
            }
        } else sender.sendMessage("Tu dois etre joueur");


        return false;
    }
}