package fr.syfalogryl.mcbasics.global.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnInWorlds implements Listener {

    @EventHandler
    public void death(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        World world = p.getWorld();

        if(world.getSpawnLocation() != null){
            Location spawnPoint = world.getSpawnLocation();
            e.setRespawnLocation(spawnPoint);
        }

    }

}
