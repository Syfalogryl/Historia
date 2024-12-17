package fr.syfalogryl.mcbasics.global.commands;

import fr.syfalogryl.mcbasics.Main;
import fr.syfalogryl.mcbasics.global.managers.PermsManager;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdWeather implements CommandExecutor {

    static Main instance = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player){

            Player p = (Player) sender;

            if(PermsManager.hasPermission(p, "mcbasics.mod")){

                if(label.equalsIgnoreCase("day")){
                    World world = p.getWorld();
                    world.setTime(1000);
                }

                if(label.equalsIgnoreCase("night")){
                    World world = p.getWorld();
                    world.setTime(13000);
                }

                if(label.equalsIgnoreCase("sun") || label.equalsIgnoreCase("weather")){

                    if(label.equalsIgnoreCase("weather")){
                        if(args.length != 1){
                            p.sendMessage("§cSyntaxe incorrecte ! Usage : §7/weather clear");
                            return true;
                        }

                        if(!args[0].equalsIgnoreCase("clear")){
                            p.sendMessage("§cSyntaxe incorrecte ! Usage : §7/weather clear");
                            return true;
                        }

                        World world = p.getWorld();
                        world.setStorm(false);
                        world.setThundering(false);
                        return true;
                    }

                    World world = p.getWorld();
                    world.setTime(1000);
                    world.setStorm(false);
                    world.setThundering(false);
                }

                if(label.equalsIgnoreCase("rain")){
                    World world = p.getWorld();
                    world.setStorm(true);
                    world.setThundering(false);
                }

                if(label.equalsIgnoreCase("thunder")){
                    World world = p.getWorld();
                    world.setStorm(true);
                    world.setThundering(true);
                }

                if(label.equalsIgnoreCase("time")){

                    World world = p.getWorld();

                    if (args.length < 2 || !args[0].equalsIgnoreCase("set")) {
                        p.sendMessage("§cSyntaxe incorrecte ! Usage : §7/time set [§aTIME§7]");
                        return true;
                    }

                    if(args[1].equalsIgnoreCase("day")){
                        world.setTime(1000);
                        return true;
                    }

                    if(args[1].equalsIgnoreCase("night")){
                        world.setTime(13000);
                        return true;
                    }

                    try {
                        long time = Long.parseLong(args[1]);
                        world.setTime(time);
                    } catch (NumberFormatException e) {
                        p.sendMessage("§cL'heure doit être un nombre valide.");
                    }
                }

            } else {
                p.sendMessage(instance.noPerm());
                return false;
            }

        } else {
            sender.sendMessage(instance.playerCmd());
            return true;
        }

        return false;
    }
}
