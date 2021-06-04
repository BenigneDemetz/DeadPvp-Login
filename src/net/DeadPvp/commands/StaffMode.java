package net.DeadPvp.commands;

import net.DeadPvp.Main;
import net.DeadPvp.utils.AdminInv;
import net.DeadPvp.utils.ItemBuilder;
import net.DeadPvp.utils.VanichUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Material.COMMAND;

public class StaffMode implements CommandExecutor {

    Main main;

    public StaffMode(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("deadpvp.staffmode")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                p.sendMessage("Attends 3 secondes...");

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        if (!Main.getInstance().staffModePlayers.contains(p)) {
                            AdminInv ai = new AdminInv(p);
                            ai.init();
                            Main.getInstance().staffModePlayers.add(p);
                            ai.saveInv(p);
                            ItemBuilder kbTester = new ItemBuilder(Material.getMaterial(main.getConfig().getString("KbTester"))).setName("§aKB Tester").addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                            p.getInventory().setItem(0, kbTester.toItemStack());
                            ItemBuilder InvSee = new ItemBuilder(Material.getMaterial(main.getConfig().getString("InvSee"))).setName("§aInvSee");
                            p.getInventory().setItem(1, InvSee.toItemStack());
                            ItemBuilder RandomTp = new ItemBuilder(Material.getMaterial(main.getConfig().getString("RandomTp"))).setName("§aTp à un joueur aléatoire");
                            p.getInventory().setItem(2, RandomTp.toItemStack());
                            ItemBuilder historyblock = new ItemBuilder(Material.getMaterial(main.getConfig().getString("MySQL"))).setName("§aHistorique du block");
                            historyblock.setName("§aInactif pour l'instant."); //mysql hs
                            p.getInventory().setItem(3, historyblock.toItemStack());
                            ItemBuilder boussole = new ItemBuilder(Material.getMaterial(main.getConfig().getString("TpWorldEdit"))).setName("§aTp");
                            p.getInventory().setItem(4, boussole.toItemStack());
                            ItemBuilder Vanish = new ItemBuilder(Material.getMaterial(main.getConfig().getString("Vanish"))).setName("§aVanish");
                            p.getInventory().setItem(5, Vanish.toItemStack());
                            ItemBuilder VanishMsg = new ItemBuilder(Material.getMaterial(main.getConfig().getString("VanishAvecMsg"))).setName("§aVanish avec message de déconnexion").addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                            p.getInventory().setItem(6, VanishMsg.toItemStack());
                            ItemBuilder freeze = new ItemBuilder(Material.getMaterial(main.getConfig().getString("Freeze"))).setName("§aFreeze");
                            p.getInventory().setItem(7, freeze.toItemStack());
                            ItemBuilder command = new ItemBuilder(Material.getMaterial(main.getConfig().getString("Commande"))).setName("§cCommande");
                            p.getInventory().setItem(8, command.toItemStack());


                            p.setGameMode(GameMode.CREATIVE);
                        } else if (Main.getInstance().staffModePlayers.contains(p)) {
                            AdminInv ai = AdminInv.getFromPlayer(p);
                            ai.destroy();
                            Main.getInstance().staffModePlayers.remove(p);
                            ai.giveInv(p);

                            if (Main.getInstance().vanishedPlayers.contains(p)) {
                                Main.getInstance().vanishedPlayers.remove(p);
                                Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(p));
                                p.sendMessage("§bTu n'est plus en Vanish.");
                                System.out.println("DPReport 1 : " + p.getName() + " n'est plus en Vanich.");
                            }
                        }

                    }
                }.runTaskLater(main, 60);
            } else sender.sendMessage("Tu dois etre joueur");
        }
        else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");


        return false;
    }
}