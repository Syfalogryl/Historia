package fr.syfalogryl.mcbasics.global.utils;

import fr.syfalogryl.mcbasics.Main;
import fr.syfalogryl.mcbasics.global.managers.PermsManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class ChatManager implements Listener {


    private static final Map<String, Boolean> worldChatStatus = new HashMap<>();

    public static void initializeWorlds() {
        for (World world : Bukkit.getWorlds()) {
            worldChatStatus.put(world.getName(), true); // Par défaut, activé
        }
    }

    public static boolean getChatStatus(String worldName) {
        return worldChatStatus.getOrDefault(worldName, true);
    }

    public static void setChatStatus(String worldName, boolean status) {
        worldChatStatus.put(worldName, status);
    }

    public static void setTabRank(Player player) {

        if(PermsManager.hasPermission(player, "mcbasics.*")){
            player.setPlayerListName("§e[Administrateur] " + player.getName());
        } else if(PermsManager.hasPermission(player, "mcbasics.mod")){

            player.setPlayerListName("§c[Modérateur] " + player.getName());

        } else if(PermsManager.hasPermission(player, "mcbasics.vip")){

            player.setPlayerListName("§6[VIP] " + player.getName());

        } else {

            player.setPlayerListName("§7[Joueur] " + player.getName());

        }

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        // Si le chat est désactivé pour le monde du joueur
        if (!getChatStatus(worldName)) {
            if (!PermsManager.hasPermission(player, "mcbasics.mod")) {
                event.setCancelled(true);
                player.sendMessage("§cLe chat est désactivé.");
            }
        }
    }

}
