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

public class Speed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("deadpvp.speed")) {
                if (args.length >= 1) {
                    if (p.isFlying()) {
                        p.setFlySpeed(Float.parseFloat(args[0]) / 10);
                        p.sendMessage("Speed en vol : " + args[0]);
                    } else if (p.isOnGround()) {
                        p.setWalkSpeed(Float.parseFloat(args[0]) / 10);
                        p.sendMessage("Speed au sol : " + args[0]);
                    }
                }
            }
            else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");
        }
        else sender.sendMessage("Tu n'es pas un joueur");


        return false;
    }
}