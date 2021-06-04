package net.DeadPvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Feed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("deadpvp.feed") || p.hasPermission("deadpvp.*"))
                p.setFoodLevel(20);
            else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");
        } else sender.sendMessage("Tu dois etre joueur");


        return false;
    }
}