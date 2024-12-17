package fr.syfalogryl.mcbasics.global.events;

import fr.syfalogryl.mcbasics.Main;
import fr.syfalogryl.mcbasics.global.utils.ChatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    static Main instance = Main.getInstance();

    @EventHandler
    public void join(PlayerJoinEvent e){

        Player p = e.getPlayer();

        ChatManager.setTabRank(p);

    }

}
