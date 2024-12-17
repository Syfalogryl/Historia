package fr.syfalogryl.mcbasics.global.events;

import fr.syfalogryl.mcbasics.global.utils.ChatManager;
import fr.syfalogryl.mcbasics.global.managers.PermsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatStatus implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        // Si le chat est désactivé pour le monde du joueur
        if (!ChatManager.getChatStatus(worldName)) {
            if (!PermsManager.hasPermission(player, "mcbasics.mod")) {
                event.setCancelled(true);
                player.sendMessage("§cLe chat est désactivé.");
            }
        }
    }

}
