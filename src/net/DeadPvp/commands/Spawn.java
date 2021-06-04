package net.DeadPvp.commands;

import net.DeadPvp.Main;
import net.DeadPvp.utils.VanichUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Spawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("deadpvp.spawn") || p.hasPermission("deadpvp.*")) {

                Location spawn = p.getLocation();
                spawn = spawn.getWorld().getSpawnLocation();
                p.teleport(spawn);

            }
            else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");
        } else sender.sendMessage("Tu dois etre joueur");


        return false;
    }
}