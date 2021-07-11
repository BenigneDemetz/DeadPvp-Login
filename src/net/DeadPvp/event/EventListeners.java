package net.DeadPvp.event;

import Crd.Code.Handler;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.xephi.authme.AuthMe;
import net.DeadPvp.Main;
import net.DeadPvp.utils.AdminInv;
import net.DeadPvp.utils.VanichUtil;
import net.DeadPvp.utils.sqlUtilities;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftItemFrame;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.Door;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.permissions.*;
import fr.xephi.authme.api.v3.AuthMeApi;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

public class EventListeners implements Listener {

    Main main;
    List<String> bannedCommands = new ArrayList<String>();
    AuthMeApi api = AuthMeApi.getInstance();

//    public void login (Player p)
//    {
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//
//                if (Handler.isLoggedIn(p))
//                {
//                    ByteArrayOutputStream bite = new ByteArrayOutputStream();
//                    DataOutputStream dos = new DataOutputStream(bite);
//                    ByteArrayDataOutput doss = ByteStreams.newDataOutput();
//                    doss.writeUTF("Connect");
//                    doss.writeUTF("lobby"); // Le nom du srv
//                    Player player = Bukkit.getPlayerExact(p.getName());
//                    player.sendPluginMessage(Main.getInstance(), "BungeeCord", doss.toByteArray());
//                    Handler.Logout(p);
//                    new BukkitRunnable() {
//                        @Override
//                        public void run() {
//                            player.sendMessage("Tu n'as pas été téléporté ? Deconnecte toi et reconnecte toi ! \nCa ne marche toujours " +
//                                    "pas ? Demande de l'aide sur le discord !");
//                            this.cancel();
//                        }
//                    }.runTaskLater(Main.getInstance(), 60);
//                }
//            }
//        }.runTaskTimer(Main.getInstance(), 20, 20);
//    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player p = e.getPlayer();
        String IP = e.getPlayer().getAddress().getHostString();
//        if (e.getPlayer().getAddress().getHostString().equals("91.161.196.66") || e.getPlayer().getAddress().getHostString().equals("31.37" +
//                ".14" +
//                ".248") || IP.equals("176.161.232.87") || IP.equals("82.64.48.2") || p.getName().equals("uhu376") || IP.equals("176.161" +
//                ".231.18") || IP.equals("93.10.217.100") ||IP.equals("83.194.119.124") || IP.equals("82.64.68.203") || IP.equals(
//                        "37.172.167.45") || IP.equals("37.172.189.242") || p.getName().equals("Arnaud013") || p.getName().equals(
//                                "SenpaiClient")){}
//        else
//        e.getPlayer().kickPlayer("§f§l[§4§lDEAD§1§lPVP§f§l] §6§l- Maintenance en cours !\n§9Discord : discord.gg/ssKvuS2");

        if (sqlUtilities.hasData("iplogin", "player", p.getName())) {
            if (IP.equals(sqlUtilities.getData("iplogin", "player", p.getName(), "ip", "String"))) {

                p.getServer().dispatchCommand(Bukkit.getConsoleSender(), "authme forcelogin " + p.getName());

                p.spigot().sendMessage(new TextComponent("§bTon IP est la même que t'as dernière connection, tu es donc directement " +
                        "connecté !"));

            }
            else {
                p.sendMessage("§4Ton IP a changé ! Refais /login pour te connecter !");
            }
        }
            else {
                Connection connection = Main.getInstance().getConnection();
                PreparedStatement insert =
                        connection.prepareStatement(
                                "INSERT INTO "+ "iplogin" + " (" + "player, ip" + ") VALUES ('" + p.getName()  + "', '"+ IP +
                                        "')");
                insert.execute();
                insert.close();
            }


        e.setJoinMessage("");
        new BukkitRunnable() {
            @Override
            public void run() {
                p.setWalkSpeed(0);
                Location loc = p.getLocation();
                loc.setY(50);
                loc.setX(0.5);
                loc.setZ(0.5);
                loc.setYaw(0);
                loc.setPitch(0);
                p.teleport(loc);
                for (PotionEffect effect : p.getActivePotionEffects())
                    p.removePotionEffect(effect.getType());

                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200000, 1));
            }
        }.runTaskLater(Main.getInstance(),5L);

        new BukkitRunnable() {
            @Override
            public void run() {

                if (api.isAuthenticated(p))
                {
                    try {
                        sqlUtilities.updateData("iplogin", "ip", IP, p.getName());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    TextComponent msg = new TextComponent("§eSite web |");
                    msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir le site").create()));
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.com/"));
                    TextComponent msg1 = new TextComponent(" §dBoutique §e|");
                    msg1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir la boutique").create()));
                    msg1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.fr/shop"));

                    TextComponent msg2 = new TextComponent(" §eForum |");
                    msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir le Forum").create()));
                    msg2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.com/???"));
                    TextComponent msg3 = new TextComponent(" §9Discord");
                    msg3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir le Discord").create()));
                    msg3.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/ugfsjrmA5j"));
                    p.sendMessage("§6\n"
                            +"\n§6§l   Bienvenue sur §4§lDEAD§9§lPVP   \n§c "
                            +"\n§b§l            LIEN");
                    TextComponent espace = new TextComponent("   ");
                    TextComponent msgRules = new TextComponent("\n\n§cN'oublie pas de lire les règles sur discord !");
                    p.spigot().sendMessage(msg,msg1,msg2,msg3, msgRules);
                    p.sendMessage("\n");
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 5);

//        login(p);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        Player p = e.getPlayer();
        String name = p.getName();
        e.setQuitMessage("");
        TextComponent msg = new TextComponent("§eSite web |");
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir le site").create()));
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.com/"));
        TextComponent msg1 = new TextComponent(" §dBoutique §e|");
        msg1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir la boutique").create()));
        msg1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.fr/shop"));

        TextComponent msg2 = new TextComponent(" §eForum |");
        msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir le Forum").create()));
        msg2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.com/???"));
        TextComponent msg3 = new TextComponent(" §9Discord");
        msg3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir le Discord").create()));
        msg3.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/ugfsjrmA5j"));
        p.sendMessage("§6\n"
                +"\n§6§l   Bienvenue sur §cDEAD§9PVP   \n§c "
                +"\n§b§l            LIEN");
        TextComponent espace = new TextComponent("   ");
        p.spigot().sendMessage(msg,msg1,msg2,msg3);
        p.sendMessage("\n");

        e.setQuitMessage("");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if ((e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ())
                && !p.getGameMode().equals(GameMode.CREATIVE) ) {
            e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void onRain(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void sendMessage(PlayerChatEvent e) {
    }

    @EventHandler
    public void looseFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        e.setCancelled(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE));
    }

}