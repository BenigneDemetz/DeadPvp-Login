package net.DeadPvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class World implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("deadpvp.world") || sender.hasPermission("deadpvp.*")) {
            Player p = (Player) sender;
            p.sendMessage("Serveur : " + p.getServer().getServerName());
            p.sendMessage("Monde actuel : " + p.getWorld().getName());
            p.sendMessage(" ");
            p.sendMessage("Liste des mondes :");
            for (org.bukkit.World world : p.getServer().getWorlds()) {
                p.sendMessage(world.getName());
            }
        }
        else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");
            
        return false;
    }
}
