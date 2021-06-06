package net.DeadPvp.event;

import Crd.Code.Handler;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.DeadPvp.Main;
import net.DeadPvp.utils.AdminInv;
import net.DeadPvp.utils.VanichUtil;
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


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.time.LocalTime;
import java.util.*;

public class EventListeners implements Listener {

    Main main;
    List<String> bannedCommands = new ArrayList<String>();

    public void login (Player p)
    {
        new BukkitRunnable() {
            @Override
            public void run() {

                if (Handler.isLoggedIn(p))
                {
                    ByteArrayOutputStream bite = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(bite);
                    ByteArrayDataOutput doss = ByteStreams.newDataOutput();
                    doss.writeUTF("Connect");
                    doss.writeUTF("lobby"); // Le nom du srv
                    Player player = Bukkit.getPlayerExact(p.getName());
                    player.sendPluginMessage(Main.getInstance(), "BungeeCord", doss.toByteArray());
                    Handler.Logout(p);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.sendMessage("Tu n'as pas été téléporté ? Deconnecte toi et reconnecte toi ! \nCa ne marche toujours " +
                                    "pas ? Demande de l'aide sur le discord !");
                            this.cancel();
                        }
                    }.runTaskLater(Main.getInstance(), 60);
                }
            }
        }.runTaskTimer(Main.getInstance(), 20, 20);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String IP = e.getPlayer().getAddress().getHostString();
        if (e.getPlayer().getAddress().getHostString().equals("91.161.196.66") || e.getPlayer().getAddress().getHostString().equals("31.37" +
                ".14" +
                ".248") || IP.equals("176.161.232.87") || IP.equals("82.64.48.2") || p.getName().equals("uhu376")){}
        else
        e.getPlayer().kickPlayer("§f§l[§4§lDEAD§1§lPVP§f§l] §6§l- Ouverture du serveur le 12 juin !");
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

                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200000, 1));
            }
        }.runTaskLater(Main.getInstance(),5L);

        login(p);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        e.setQuitMessage("");
        TextComponent msg = new TextComponent("§eSite web |");
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir le site").create()));
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.fr/"));
        TextComponent msg1 = new TextComponent(" §dBoutique §e|");
        msg1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir la boutique").create()));
        msg1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.fr/shop"));

        TextComponent msg2 = new TextComponent(" §eForum |");
        msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir le Forum").create()));
        msg2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.fr/???"));
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
        if (Main.getInstance().vanishedPlayers.contains(p)) {
            Main.getInstance().vanishedPlayers.remove(p);
            Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(p));
            p.sendMessage("§bTu n'est plus en Vanish.");
        }
        if (Main.getInstance().freezedPlayers.contains(p)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "litebans:ban " +
                    name + " déconnexion durant un freeze.");
            System.out.println("DPReport 3 : " + name + " s'est déconnecté en étant freeze.");
            Main.getInstance().freezedPlayers.remove(p);
        }
        if (Main.getInstance().staffModePlayers.contains(p)) {
            AdminInv ai = AdminInv.getFromPlayer(p);
            Main.getInstance().staffModePlayers.remove(p);
            p.getInventory().clear();
            ai.giveInv(p);
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        if (Main.getInstance().staffModePlayers.contains(p)) {
            AdminInv ai = AdminInv.getFromPlayer(p);
            Main.getInstance().staffModePlayers.remove(p);
            p.getInventory().clear();
            ai.giveInv(p);
        }
    }

    //Eclair Mort
    @EventHandler
    public void onPDeath(PlayerDeathEvent e) {
        Player player = e.getEntity().getPlayer();
        Location pos = player.getLocation().getBlock().getLocation();
        player.getWorld().strikeLightningEffect(pos);
        Random rdn = new Random();

    } //Eclair Mort


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if ((e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ())
                && !p.getGameMode().equals(GameMode.CREATIVE) ) {
            e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void onHitGround (EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL) &&
                e.getEntity() instanceof Player && Main.getInstance().hasJump.contains(e.getEntity())){
            e.setCancelled(true);
            Main.getInstance().hasJump.remove(e.getEntity());
        }

    }


    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        if (Main.getInstance().staffModePlayers.contains(e.getPlayer())) {
            e.getPlayer().setGameMode(GameMode.CREATIVE);
            e.getPlayer().setAllowFlight(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntityType().equals(EntityType.PRIMED_TNT) || e.getEntityType().equals(EntityType.MINECART_TNT)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRain(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onTp(PlayerTeleportEvent e) {
        try {
            if (Main.getInstance().didCommandJoin.contains(e.getPlayer())) {
                Main.getInstance().isPlaying.add(e.getPlayer());
                Main.getInstance().didCommandJoin.remove(e.getPlayer());
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (e.getPlayer().getInventory().getItem(8) != null &&
                            e.getPlayer().getInventory().getItem(8).getType() != Material.AIR) {
                        if (e.getPlayer().getInventory().getItem(8).getItemMeta().getDisplayName().contains("§7") &&
                                e.getPlayer().getHealth() > 0) {
                            int nbChest = e.getPlayer().getInventory().getItem(8).getAmount();
                        }
                    }
                }
            }.runTaskLater(Main.getInstance(), 20L);
            ////////utiliser database but don't remember comment

        } catch (Exception ee) {
        }
    }

    @EventHandler
    public void useCommand(PlayerCommandPreprocessEvent e) {
// use command in pvpsoup
        try {
            if (e.getMessage().contains("kb join")) {
                Main.getInstance().didCommandJoin.add(e.getPlayer());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (Main.getInstance().didCommandJoin.contains(e.getPlayer()))
                            Main.getInstance().didCommandJoin.remove(e.getPlayer());
                    }
                }.runTaskLater(Main.getInstance(), 60L);
            }
            if (e.getMessage().contains("kb leave")) {
                if (Main.getInstance().isPlaying.contains(e.getPlayer()))
                    Main.getInstance().isPlaying.remove(e.getPlayer());
            }
            bannedCommands.addAll(Main.getInstance().getConfig().getStringList("Commandes"));
            if (Main.getInstance().isPlaying.contains(e.getPlayer())) {
                e.setCancelled(bannedCommands.contains(e.getMessage().substring(1)));
                if (e.isCancelled()) e.getPlayer().sendMessage("§cTu ne peux pas utiliser cette commande actuellement !");
            }
//end
        } catch (Exception ee) {
        }
    }

    @EventHandler
    public void sendMessage(PlayerChatEvent e) {
        try {
            Player p = e.getPlayer();
            String msg = e.getMessage();
            String newMsg = getPrefix(p) + e.getPlayer().getDisplayName() + " §f: ";
            e.setCancelled(true);
            if (msg.equals("[event cancelled by LiteBans")) return;
            if ((p.hasPermission("dp.modo.chat") || p.hasPermission("dp.modo.*") || p.hasPermission("dp.*") ||
                    p.hasPermission("dp.admin.chat") || p.hasPermission("dp.admin.*")) && e.getMessage().startsWith("!")) {

                if (msg.startsWith("!!") && (p.hasPermission("dp.admin.chat") || p.hasPermission("dp.admin.*")
                        || p.hasPermission("dp.*"))) {
                    msg = msg.substring(2);
                    for (Player reciever : Bukkit.getOnlinePlayers()) {
                        if (reciever.hasPermission("dp.admin.chat") || reciever.hasPermission("dp.admin.*") ||
                                reciever.hasPermission("dp.*")) {
                            reciever.sendRawMessage("§d[AdminChat] " + newMsg + msg);
                        }
                    }
                    Bukkit.getConsoleSender().sendMessage("§d[AdminChat] " + newMsg + msg);

                } else {
                    e.setCancelled(true);
                    msg = msg.substring(1);
                    for (Player reciever : Bukkit.getOnlinePlayers()) {
                        if (reciever.hasPermission("dp.modo.chat") || reciever.hasPermission("dp.modo.*") ||
                                reciever.hasPermission("dp.*") || reciever.hasPermission("dp.admin.chat") ||
                                reciever.hasPermission("dp.admin.*")) {
                            reciever.sendRawMessage("§d[StaffChat] " + newMsg + msg);
                        }
                    }
                    Bukkit.getConsoleSender().sendMessage("§d[AdminChat] " + newMsg + msg);
                }
            } else {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendRawMessage(newMsg + msg);
                }
                Bukkit.getConsoleSender().sendMessage(newMsg + msg);
            }
        } catch (Exception ee) {
        }
    }

    @EventHandler
    public void looseFood(FoodLevelChangeEvent e) {
        try {
            e.setCancelled(true);
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (p.getServer().getName().contains("pvpsoup") || p.getServer().getName().contains("login")
                        || p.getServer().getName().contains("lobby"))
                    e.setCancelled(true);
            }
        } catch (Exception ee) {
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        try {
            String nameServ = e.getPlayer().getServer().getName();
            if (nameServ.contains("lobby") || nameServ.contains("login") || nameServ.contains("pvpsoup")) {
                e.setCancelled(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE));
            }

            e.setCancelled(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE));
        }
        catch (Exception ee) {
        }
    }

    @EventHandler
    public void painting (EntityDamageByEntityEvent e) {
        try {
            if (e.getDamager() instanceof Player)
                e.setCancelled(((e.getEntity().getType().equals(EntityType.PAINTING) ||
                        e.getEntity().getType().equals(EntityType.ITEM_FRAME)) &&
                        !((Player) e.getDamager()).getGameMode().equals(GameMode.CREATIVE)) ||
                        e.getEntity().getType().equals(EntityType.ENDER_CRYSTAL));
        }
        catch (Exception ee){}
    }

    @EventHandler
    public void onInteract (PlayerInteractEvent e) {
        try {
            if (!e.getClickedBlock().equals(null)) {
                Material cb = e.getClickedBlock().getType();
                if (e.getPlayer().getServer().getServerName().contains("pvpsoup") &&
                        !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                    if (e.getClickedBlock().getState() instanceof Door) {
                        Door door = (Door) e.getClickedBlock().getState();

                    } else {
                        e.setCancelled(
//                    cb.equals(Material.ACACIA_DOOR) || cb.equals(Material.BIRCH_DOOR) ||
                                cb.equals(Material.SPRUCE_DOOR) ||
//                    cb.equals(Material.JUNGLE_DOOR) ||
//                    cb.equals(Material.DARK_OAK_DOOR) || cb.equals(Material.TRAP_DOOR) || cb.equals(Material.WOOD_DOOR)
//                    || cb.equals(Material.WOODEN_DOOR) ||
                                        cb.equals(Material.CHEST) || cb.equals(Material.ENDER_CHEST)
                                        || cb.equals(Material.ANVIL) || cb.equals(Material.ENCHANTMENT_TABLE) ||
                                        cb.equals(Material.WORKBENCH)
                                        || cb.equals(Material.JUKEBOX) || cb.equals(Material.NOTE_BLOCK) || cb.equals(Material.FURNACE)
                                        || cb.equals(Material.BURNING_FURNACE) || cb.equals(Material.FENCE_GATE) || cb.equals(Material.BED)
                                        || cb.equals(Material.BED_BLOCK) || cb.equals(Material.TRAP_DOOR) ||
                                        cb.equals(Material.TRAPPED_CHEST));
                    }
                }
            }
        }
        catch (Exception ee)
        {}
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent e)
    {
        if (e.getPlayer().getServer().getServerName().contains("pvpsoup") &&
                !e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
        {
            if(e.getRightClicked() instanceof ItemFrame ||
                    e.getRightClicked() instanceof CraftItemFrame ||
                    e.getRightClicked().getType().equals(EntityType.ITEM_FRAME))
            {
                ItemFrame itemframe = (ItemFrame) e.getRightClicked();
                itemframe.setRotation(Rotation.COUNTER_CLOCKWISE_45);
            }
            if (e.getRightClicked() instanceof EnderCrystal) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand (PlayerCommandPreprocessEvent e)
    {
        try {
            Player p = e.getPlayer();
            if (main.specItemMode.contains(p) && e.getMessage().contains("clear") && (e.getMessage().length() <= 8 || e.getMessage().
                                                                        contains(p.getName()))) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.performCommand("spec");
                    }
                }.runTaskLater(Main.getInstance(), 10);
            }
        }
        catch (Exception exception)
        {}
    }

    public static String getPrefix(Player p) {
        if (p.hasPermission("chat.admin")) return "§c[Admin] §6";
        if (p.hasPermission("chat.modo")) return "§b[Modo] §6";
        if (p.hasPermission("chat.builder")) return "§a[Builder] §6";
        else return "§7";

    }

    public static boolean isBetween (Player p) {
        Location loc = p.getLocation();
        FileConfiguration fConf = Main.getInstance().getConfig();
        if ((loc.getX() <= -38 &&
                loc.getX() >= -41 &&
                loc.getY() <= 61 &&
                loc.getY() >= 56 &&
                loc.getZ() <= -101 &&
                loc.getZ() >= -101.699) ||

                (loc.getX() <= -32 &&
                loc.getX() >= -47 &&
                loc.getY() <= 50 &&
                loc.getY() >= 42 &&
                loc.getZ() <= -96 &&
                loc.getZ() >= -100)){
            return true;
        }
//        if (loc.getX() <= fConf.getDouble("Bump.world.1.1.X") &&
//                loc.getX() >= fConf.getDouble("Bump.world.1.2.X") &&
//                loc.getY() <= fConf.getDouble("Bump.world.1.1.Y") &&
//                loc.getY() >= fConf.getDouble("Bump.world.1.2.Y") &&
//                loc.getZ() <= fConf.getDouble("Bump.world.1.1.Z") &&
//                loc.getZ() >= fConf.getDouble("Bump.world.1.2.Z")){
//            Bukkit.broadcastMessage("2");
//            return true;
//        }
        for (int i = 1; i <= fConf.getInt("Bump.world"); ++i) {
        }
        return false;
    }
}