package fr.syfalogryl.mcbasics.global.events;

import fr.syfalogryl.mcbasics.global.managers.PermsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class IsolateWorlds implements Listener {

    @EventHandler
    public void worldChange(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        for(Player players : Bukkit.getOnlinePlayers()){
            if(!players.getWorld().equals(p.getWorld())){
                players.hidePlayer(p);
                p.hidePlayer(players);
                return;
            } else {
                players.showPlayer(p);
                p.showPlayer(players);
            }
        }
    }

    @EventHandler
    public void isolateChats(AsyncPlayerChatEvent e){

        Player p = e.getPlayer();
        String worldName = p.getWorld().getName();

        e.getRecipients().removeIf(recipient -> !recipient.getWorld().getName().equals(worldName));

        if(PermsManager.hasPermission(p, "mcbasics.*")){
            e.setFormat("§eAdministrateur " + p.getName() + " §6> §7" + e.getMessage());
        }

        else if(PermsManager.hasPermission(p, "mcbasics.mod")){
            e.setFormat("§cModérateur " + p.getName() + " §6> §7" + e.getMessage());
        }
        else if(PermsManager.hasPermission(p, "mcbasics.vip")){
            e.setFormat("§6VIP " + p.getName() + " §6> §7" + e.getMessage());
        }
        else {
            e.setFormat("§7Joueur " + p.getName() + " §6> §7" + e.getMessage());
        }
    }
}
