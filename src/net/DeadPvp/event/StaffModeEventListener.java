package net.DeadPvp.event;

import net.DeadPvp.Main;
import net.DeadPvp.commands.Vanich;
import net.DeadPvp.utils.AdminInv;
import net.DeadPvp.utils.GuiMenu;
import net.DeadPvp.utils.ItemBuilder;
import net.DeadPvp.utils.VanichUtil;
import net.minecraft.server.v1_8_R1.EntityEgg;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class StaffModeEventListener implements Listener {

    Main main;

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(Main.getInstance().staffModePlayers.contains(e.getPlayer()));
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        e.setCancelled(Main.getInstance().staffModePlayers.contains(e.getPlayer()));
    }

    @EventHandler
    public void onCastEgg(PlayerEggThrowEvent e) {
        e.setHatching(!Main.getInstance().staffModePlayers.contains(e.getPlayer()));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity damaged = e.getEntity();
        if (damager instanceof Player && damaged instanceof Player) {
            damager = (Player) damager;
            e.setCancelled(Main.getInstance().staffModePlayers.contains(damager));
        }
    }

    @EventHandler
    public void onJoinPvpSoup(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if (e.getMessage().contains("kb") && e.getMessage().contains("join")){
            if (Main.getInstance().staffModePlayers.contains(p)){
                AdminInv ai = AdminInv.getFromPlayer(p);
                ai.destroy();
                Main.getInstance().staffModePlayers.remove(p);
                ai.giveInv(p);
            }
            if (Main.getInstance().vanishedPlayers.contains(p)){
                VanichUtil.Vanich(p);
            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (Main.getInstance().staffModePlayers.contains(p)) {
            e.setCancelled(true);
            if (!Main.getInstance().isWaiting.contains(p)) {
                pause(p);
                Entity target = e.getRightClicked();

                if (p.getItemInHand().getType() == Material.IRON_HOE) {
                    target.setVelocity(p.getLocation().getDirection().multiply(2));
                }

                if (target instanceof Player) {
                    Player targetPlayer = (Player) target;
                    switch (p.getInventory().getItemInHand().getType()) {
                        case PAPER:
                            invSee(p, targetPlayer);
                            break;
                        case ICE:
                            Bukkit.dispatchCommand(p, "freeze " + targetPlayer.getName());
                        default:
                            break;
                    }
                }
            }
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (Main.getInstance().staffModePlayers.contains(p)) {
            e.setCancelled(true);
            if (!Main.getInstance().isWaiting.contains(p)) {
                pause(p);
                switch (e.getItem().getType()) {
                    case EGG:
                        for (Player ponline : Bukkit.getOnlinePlayers()) {
                            Main.getInstance().onlinePlayers.add(ponline);
                        }
                        Main.getInstance().onlinePlayers.remove(p);
                        if (Main.getInstance().onlinePlayers.size() != 0) {
                            Player RandomPlayer;
                            do {
                                RandomPlayer = Main.getInstance().onlinePlayers.get(new Random().nextInt(
                                        Main.getInstance().onlinePlayers.size()));
                            } while (RandomPlayer == p);
                            p.teleport(RandomPlayer);
                            p.sendMessage("§fTu as été téléporté à §b" + RandomPlayer.getName() + "§f.");
                        } else {
                            p.sendMessage("Aucun joueur n'est connecté à part toi.");
                        }
                        break;
                    case WRITTEN_BOOK:
                        break;
                    case BLAZE_POWDER:
                        p.getInventory().getItem(5).getItemMeta().setDisplayName("§aAttends 3 secondes pour utiliser à nouveau l'objet...");
                        p.getInventory().getItem(6).getItemMeta().setDisplayName("§aAttends 3 secondes pour utiliser à nouveau l'objet...");
                        VanichUtil.Vanich(p);

                        if (!Main.getInstance().vanishedPlayers.contains(p)) {
                            if(e.getItem().getItemMeta().getDisplayName().contains("avec")){
                                Bukkit.broadcastMessage("§2[§4+§a] " + p.getName());
                            }
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (main.staffModePlayers.contains(p)) {
                                            ItemBuilder Vanish = new ItemBuilder(Material.BLAZE_POWDER).setName("§aVanish");
                                            p.getInventory().setItem(5, Vanish.toItemStack());
                                            ItemBuilder VanishMsg =
                                                    new ItemBuilder(Material.BLAZE_POWDER).setName("§aVanish avec message de déconnexion").
                                                            addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                                            p.getInventory().setItem(6, VanishMsg.toItemStack());
                                        }
                                    }
                                    catch (Exception ee){}
                                }
                            }.runTaskLater(Main.getInstance(), 60);
                        } else {
                            if (e.getItem().getItemMeta().getDisplayName().contains("avec")){
                                Bukkit.broadcastMessage("§c[§4-§d] " + p.getName());
                            }
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    try {
                                    if (main.staffModePlayers.contains(p)) {
                                        ItemBuilder Vanish = new ItemBuilder(Material.BLAZE_POWDER).setName("§aUnVanish");
                                        p.getInventory().setItem(5, Vanish.toItemStack());
                                        ItemBuilder VanishMsg =
                                                new ItemBuilder(Material.BLAZE_POWDER).setName("§aUnVanish avec message de connexion").
                                                        addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                                        p.getInventory().setItem(6, VanishMsg.toItemStack());
                                    }
                                }
                                    catch (Exception ee){}
                                }
                            }.runTaskLater(Main.getInstance(),60);
                        }
                        break;
                    case COMMAND:
                        p.openInventory(GuiMenu.headMenu());
                        if (p.getGameMode() == GameMode.SPECTATOR) {
                            ItemBuilder crea = new ItemBuilder(Material.WORKBENCH).setName("Créatif");
                            p.getOpenInventory().setItem(5 * 9 + 5, crea.toItemStack());
                        }
                        break;
                }
            }
        }

        if (Main.getInstance().specItemMode.contains(p) && !e.getItem().getType().equals(null) && !e.getItem().getType().equals(Material.AIR)&& e.getItem().getType().equals(Material.GLASS))
        {
            e.setCancelled(true);
            if (e.getItem().getItemMeta().getDisplayName().contains("§dSpec")) {
                p.setGameMode(GameMode.SPECTATOR);
                ItemBuilder crea = new ItemBuilder(Material.WORKBENCH).setName("§dCréatif");
                p.getInventory().setItem(8, crea.toItemStack());
            }
        }
        if (Main.getInstance().specItemMode.contains(p) && !e.getItem().getType().equals(null) && e.getItem().getType().equals(Material.WORKBENCH))
        {
            e.setCancelled(true);
            if (e.getItem().getItemMeta().getDisplayName().contains("§dCréatif"))
            {
                p.setGameMode(GameMode.CREATIVE);
                ItemBuilder spec = new ItemBuilder(Material.GLASS).setName("§dSpectateur");
                p.getInventory().setItem(8, spec.toItemStack());
            }
        }
    }


    @EventHandler
    public void onInventoryEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() != null) {
            ItemStack is = e.getCurrentItem();
            Material m = is.getType();
            if (Main.getInstance().staffModePlayers.contains(p)) {
                e.setCancelled(true);
                pause(p);
                Player targetPlayer = Bukkit.getPlayer(p.getOpenInventory().getTitle().substring(2));
                if (Main.getInstance().isWaiting.contains(p)) {
                    switch (m) {
                        case SKULL_ITEM:
                            p.openInventory(GuiMenu.playerMenu(Bukkit.getPlayer(is.getItemMeta().getDisplayName().substring(2))));
                            break;
                        case STAINED_GLASS_PANE:
                            if (is.getItemMeta().getDisplayName().contains("Retour")) {
                                if (is.getItemMeta().getDisplayName().contains("Joueurs")) {
                                    p.openInventory(GuiMenu.headMenu());
                                }
                                if (is.getItemMeta().getDisplayName().contains("Action")){
                                    p.openInventory(GuiMenu.playerMenu(targetPlayer));
                                }
                                if (is.getItemMeta().getDisplayName().contains("Raison")){
                                    p.openInventory(GuiMenu.Mute(targetPlayer));
                                }
                            }
                            break;
                        case EYE_OF_ENDER:
                            if (is.getItemMeta().getDisplayName().contains("Inventaire")) {
                                invSee(p, targetPlayer);
                            }
                            break;
                        case ENDER_PEARL:
                            if (is.getItemMeta().getDisplayName().contains("Téléporter")) {
                                p.teleport(targetPlayer);
                            }
                            break;
                        case PISTON_BASE:
                            if (Bukkit.getPlayer(e.getClickedInventory().getName().substring(2)) != null) {
                            }
                            break;
                        case INK_SACK:
                            if (Bukkit.getPlayer(e.getClickedInventory().getName().substring(2)) != null) {
                                p.openInventory(GuiMenu.Mute(targetPlayer));
                            }
                            break;
                        case PAPER:
                            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Spam")){
                                p.openInventory(GuiMenu.MuteRaison(targetPlayer, p, "Spam"));
                            }
                            else if (Bukkit.getPlayer(e.getClickedInventory().getName().substring(2)) != null) {
                                Bukkit.dispatchCommand(p, "unmute " + targetPlayer.getName());
                            }
                            else if( (e.getClickedInventory().getTitle().contains(":"))){
                                String text;
                                Bukkit.broadcastMessage(e.getClickedInventory().getTitle());
                                for (text = e.getClickedInventory().getTitle(); text.contains(":"); text = text.substring(1)){}
                            }
                            else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("15 minutes"))
                            break;
                        case FIREBALL:
                            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Insulte")){
                                p.openInventory(GuiMenu.MuteRaison(targetPlayer, p, "Insulte"));
                            }
                            break;
                        case IRON_SWORD:
                            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Provocation")){
                                p.openInventory(GuiMenu.MuteRaison(targetPlayer, p, "Provocation"));
                            }
                            break;
                        case BOOKSHELF:
                            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Pub")){
                                p.openInventory(GuiMenu.MuteRaison(targetPlayer, p, "Pub"));
                            }
                            break;
                        case ICE:
                            if (Bukkit.getPlayer(e.getClickedInventory().getName().substring(2)) != null) {
                                if (is.getItemMeta().getDisplayName().contains("Freeze") || is.getItemMeta().getDisplayName().contains("Unfreeze")) {
                                    Bukkit.dispatchCommand(p, "freeze " + targetPlayer.getName());
                                    ItemBuilder freezeItem = new ItemBuilder(e.getCurrentItem().clone());
                                    if (Main.getInstance().freezedPlayers.contains(targetPlayer)) {
                                        freezeItem.setName("§aUnfreeze " + targetPlayer.getName());
                                    } else {
                                        freezeItem.setName("§aFreeze " + targetPlayer.getName());
                                    }
                                    e.getClickedInventory().setItem(4, freezeItem.toItemStack());
                                }
                            }
                            break;
                        case ARROW:
                            if (is.getItemMeta().getDisplayName().contains("suivante")) {
                                GuiMenu.headMenuPage2();
                                if (p.getGameMode() == GameMode.SPECTATOR) {
                                    ItemBuilder crea = new ItemBuilder(Material.WORKBENCH).setName("Créatif");
                                    p.getOpenInventory().setItem(5 * 9 + 5, crea.toItemStack());
                                }
                            }
                            if (is.getItemMeta().getDisplayName().contains("précédente")) {
                                GuiMenu.headMenu();
                                if (p.getGameMode() == GameMode.SPECTATOR) {
                                    ItemBuilder crea = new ItemBuilder(Material.WORKBENCH).setName("Créatif");
                                    p.getOpenInventory().setItem(5 * 9 + 5, crea.toItemStack());
                                }
                            }
                            break;
                        case GLASS:
                            if (is.getItemMeta().getDisplayName().contains("Spec")) {
                                p.setGameMode(GameMode.SPECTATOR);
                                ItemBuilder crea = new ItemBuilder(Material.WORKBENCH).setName("§dCréatif");
                                p.getOpenInventory().setItem(5 * 9 + 5, crea.toItemStack());
                            }
                            break;
                        case WORKBENCH:
                            p.setGameMode(GameMode.CREATIVE);

                            ItemBuilder spec = new ItemBuilder(Material.GLASS).setName("§dSpec Mode");
                            p.getOpenInventory().setItem(5 * 9 + 5, spec.toItemStack());
                            break;
                    }
                }
            }
        }
    }



    public void invSee(Player p, Player targetPlayer) {

        Inventory inv = Bukkit.createInventory(null, 5 * 9, "§bInventaire de §e" + targetPlayer.getName());

        for (int i = 0; i < 36; i++) {
            if (targetPlayer.getInventory().getItem(i) != null) {
                inv.setItem(i, targetPlayer.getInventory().getItem(i));
            }
        }
        inv.setItem(36, targetPlayer.getInventory().getHelmet());
        inv.setItem(37, targetPlayer.getInventory().getChestplate());
        inv.setItem(38, targetPlayer.getInventory().getLeggings());
        inv.setItem(39, targetPlayer.getInventory().getBoots());
        p.openInventory(inv);

    }

    public void pause(Player p){
      Main.getInstance().isWaiting.add(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                Main.getInstance().isWaiting.remove(p);
            }
        }.runTaskLater(Main.getInstance(), 20L);
    }

}