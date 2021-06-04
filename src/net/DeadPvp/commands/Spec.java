package net.DeadPvp.commands;

import net.DeadPvp.Main;
import net.DeadPvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Spec implements CommandExecutor {

    private ItemStack item;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("deadpvp.spectator")) {
            if (sender instanceof Player)
            {
                Player p = (Player) sender;
                if (!Main.getInstance().specItemMode.contains(p))
                {
                    item = p.getInventory().getItem(8);
                    ItemBuilder spec = new ItemBuilder(Material.GLASS).setName("§dSpec mode");
                    p.getInventory().setItem(8, spec.toItemStack());
                    Main.getInstance().specItemMode.add(p);
                }
                else
                {
                    p.getInventory().setItem(8, item);
                    Main.getInstance().specItemMode.remove(p);
                }
            }
        }
        else sender.sendMessage("§cTu n'as pas la permission d'éxecuter cette commande.");


        return false;
    }
}