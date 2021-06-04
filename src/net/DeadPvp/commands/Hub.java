package net.DeadPvp.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.DeadPvp.Main;
import net.DeadPvp.utils.VanichUtil;
import net.md_5.bungee.api.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Hub implements CommandExecutor {

    private Main main;

    public Hub(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return false;
        if (sender.hasPermission("deadpvp.hub") || sender.hasPermission("deadpvp.*")) {
            Player p = (Player) sender;
//        if (p.getServer().getName().equalsIgnoreCase("pvpsoup")) {
//            Location spawn = p.getLocation();
//            spawn.setWorld(p.getServer().getWorld("world"));
//            spawn = spawn.getWorld().getSpawnLocation();
//            p.teleport(spawn);
//        } else
//            {

            ByteArrayOutputStream bite = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bite);
            ByteArrayDataOutput doss = ByteStreams.newDataOutput();

            doss.writeUTF("Connect");
            doss.writeUTF("lobby"); // Le nom du srv
            Player player = Bukkit.getPlayerExact(p.getName());
            player.sendPluginMessage(main, "BungeeCord", doss.toByteArray());
//        }
        }
        else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");
        return false;
    }
}
