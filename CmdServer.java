package fr.syfalogryl.mcbasics.global.commands;

import fr.syfalogryl.mcbasics.Main;
import fr.syfalogryl.mcbasics.global.managers.PermsManager;
import fr.syfalogryl.mcbasics.global.managers.WorldsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdServer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player){

            Player p = (Player) sender;

            if(PermsManager.hasPermission(p, "mcbasics.mod")){

                if(args.length != 1){
                    p.sendMessage("§cSyntaxe incorrecte ! Usage : §7/server [§aSERVERNAME§7]");
                    return false;

                }

                String serverName = args[0];

                switch(serverName){

                    case "LOBBY":
                        if(WorldsManager.getWorldsConfig().contains("lobby")){

                            World world = Bukkit.getWorld(serverName);

                            if(world != null){

                                Location worldSpawn = world.getSpawnLocation();

                                p.teleport(worldSpawn);

                                p.sendMessage("§cChangement de serveur vers §6LOBBY§c.");

                            } else {
                                p.sendMessage("§cLe monde §6Lobby §cn'est pas chargé.");
                                return true;
                            }

                        } else {
                            p.sendMessage("§cLe serveur §6Lobby §cn'existe pas");
                            return true;
                        }
                        break;

                    case "PVPSOUP":
                        if(WorldsManager.getWorldsConfig().contains("PVPSOUP")){

                            World world = Bukkit.getWorld(serverName);

                            if(world != null){

                                Location worldSpawn = world.getSpawnLocation();

                                p.teleport(worldSpawn);

                                p.sendMessage("§cChangement de serveur vers §6PVPSOUP§c.");

                            } else {
                                p.sendMessage("§cLe monde §6Lobby §cn'est pas chargé.");
                                return true;
                            }

                        } else {
                            p.sendMessage("§cLe serveur §6PVPSOUP §cn'existe pas");
                            return true;
                        }
                        break;

                    case "FFA":
                        if(WorldsManager.getWorldsConfig().contains("FFA")){

                            World world = Bukkit.getWorld(serverName);

                            if(world != null){

                                Location worldSpawn = world.getSpawnLocation();

                                p.teleport(worldSpawn);

                                p.sendMessage("§cChangement de serveur vers §6FFA§c.");

                            } else {
                                p.sendMessage("§cLe monde §6Lobby §cn'est pas chargé.");
                                return true;
                            }

                        } else {
                            p.sendMessage("§cLe serveur §6FFA §cn'existe pas");
                            return true;
                        }
                        break;

                    case "PVPENCHANTS":
                        if(WorldsManager.getWorldsConfig().contains("PVPENCHANTS")){

                            World world = Bukkit.getWorld(serverName);

                            if(world != null){

                                Location worldSpawn = world.getSpawnLocation();

                                p.teleport(worldSpawn);

                                p.sendMessage("§cChangement de serveur vers §6PVPENCHANTS§c.");

                            } else {
                                p.sendMessage("§cLe monde §6Lobby §cn'est pas chargé.");
                                return true;
                            }

                        } else {
                            p.sendMessage("§cLe serveur §6PVPENCHANTS §cn'existe pas");
                            return true;
                        }
                        break;

                    default:
                        p.sendMessage("§cLe serveur spécifié n'existe pas. Faites §7/server list §cpour avoir la liste des serveurs disponibles");
                        break;

                }

            } else {

                p.sendMessage(Main.getInstance().noPerm());
                return false;
            }


        } else sender.sendMessage(Main.getInstance().playerCmd());

        return false;
    }
}
