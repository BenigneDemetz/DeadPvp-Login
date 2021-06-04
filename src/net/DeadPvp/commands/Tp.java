package net.DeadPvp.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.DeadPvp.Main;
import net.DeadPvp.utils.VanichUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class Tp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = null;

            if (sender.hasPermission("deadpvp.tp") || sender.hasPermission("deadpvp.*")) {

                if (args.length == 1) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("Tu n'es pas un joueur !");
                        return false;
                    }
                        Player p = (Player) sender;

                    for (World world : Bukkit.getServer().getWorlds())
                    {
                        if (args[0].equalsIgnoreCase(world.getName()))
                        {
                            p.teleport(world.getSpawnLocation());
                            return false;
                        }
                    }

                    try{
                        p.teleport(Bukkit.getPlayer(args[0]));}
                    catch(Exception e){
                        p.sendMessage("Ce joueur n'existe pas ou n'est pas connecté");}
                }

                else if (args.length == 2) {
                    Player teleported = Bukkit.getPlayer(args[0]);
                    Player target = Bukkit.getPlayer(args[1]);

                    Bukkit.getPlayer(args[0]).teleport(Bukkit.getPlayer(args[1]));
                    sender.sendMessage("§b" + teleported.getName() + "§f a été téleporté à §b" + target.getName());
                    teleported.sendMessage("Tu as été téléporté à §b" + target.getName());
                    return false;

                }

                else if (!(sender instanceof Player)) return false;
                else if (sender instanceof Player) player = (Player) sender;

                if (args.length == 3)
                {
                    try {
                        int x = Integer.parseInt(args[0]);
                        int y = Integer.parseInt(args[1]);
                        int z = Integer.parseInt(args[2]);
                        Location loc = player.getLocation();
                        loc.setX(x);
                        loc.setY(y);
                        loc.setZ(z);
                        player.teleport(loc);
                    }
                    catch (Exception e)
                    {
                        sender.sendMessage("Mauvaises coordonnées");
                    }
                }



            }
            else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");

        return false;
    }
}
