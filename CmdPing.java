package fr.syfalogryl.mcbasics.global.commands;

import fr.syfalogryl.mcbasics.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class CmdPing implements CommandExecutor {

    static Main instance = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player){

            Player p = (Player) sender;

            if(args.length == 0){

                if(getPing(p) < 80){
                    p.sendMessage("§cVotre ping est de : §a" + getPing(p) + " §a(Excellent)");
                    return true;
                }

                if(getPing(p) < 200 && getPing(p) > 80){
                    p.sendMessage("§cVotre ping est de : §e" + getPing(p) + " §e(Acceptable)");
                    return true;
                }

                if(getPing(p) > 200){
                    p.sendMessage("§cVotre ping est de : §4" + getPing(p) + " §4(Elevé)");
                    return true;
                }

            }

            if(args.length <= 1){

                String target = args[0];

                if(Bukkit.getPlayer(target) == null){
                    p.sendMessage("§cLe joueur §6" + target + " §cn'est pas connecté ou n'existe pas.");
                    return false;
                }

                if(Bukkit.getPlayer(target) == p){

                    if(getPing(p) < 80){
                        p.sendMessage("§cVotre ping est de : §a" + getPing(p) + " §a(Excellent)");
                        return true;
                    }

                    if(getPing(p) < 200 && getPing(p) > 80){
                        p.sendMessage("§cVotre ping est de : §e" + getPing(p) + " §e(Acceptable)");
                        return true;
                    }

                    if(getPing(p) > 200){
                        p.sendMessage("§cVotre ping est de : §4" + getPing(p) + " §4(Elevé)");
                        return true;
                    }

                }

                Player t = Bukkit.getPlayer(target);

                if(getPing(p) < 80){
                    p.sendMessage("§cLe ping de §6 " + t.getName() + "est de : §4" + getPing(t) + " §4(Elevé)");
                    return true;
                }

                if(getPing(p) < 200 && getPing(p) > 80){
                    p.sendMessage("§cLe ping de §6 " + t.getName() + "est de : §4" + getPing(t) + " §4(Elevé)");
                    return true;
                }

                if(getPing(p) > 200){
                    p.sendMessage("§cLe ping de §6 " + t.getName() + "est de : §4" + getPing(t) + " §4(Elevé)");
                    return true;
                }

            }

        } else {
            sender.sendMessage(instance.playerCmd());
            return false;
        }

        return false;
    }

    private int getPing(Player player) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Field pingField = handle.getClass().getDeclaredField("ping");
            pingField.setAccessible(true);

            return pingField.getInt(handle);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
