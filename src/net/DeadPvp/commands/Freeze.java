package net.DeadPvp.commands;

import net.DeadPvp.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Freeze implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("deadpvp.freeze") || sender.hasPermission("deadpvp.*")) {
            if (args.length == 0){
                sender.sendMessage("Tu dois cibler un joueur");
            }
            else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (!Main.getInstance().freezedPlayers.contains(target)) {
                        Main.getInstance().freezedPlayers.add(target);
                        sender.sendMessage("§d" + target.getDisplayName() + " §best freeze.");
                        target.sendMessage("§cTu as été freeze par un Moderateur, \nconnecte toi sur le " +
                                "Discord sans te déconnecter.\n discord.gg/ssKvuS2");

                    } else {
                        Main.getInstance().freezedPlayers.remove(target);
                        sender.sendMessage("§d" + target.getDisplayName() + " §bn'est plus freeze.");
                        target.sendMessage("§bTu n'es plus freeze !");
                    }

                }
                else sender.sendMessage("§bCe joueur n'est pas connecté.");
            }
        }
        else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");


        return false;
    }
}