package fr.syfalogryl.mcbasics.global.commands;

import fr.syfalogryl.mcbasics.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CmdMsg implements CommandExecutor {

    private final Map<Player, Player> lastMsgSender = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(label.equalsIgnoreCase("msg") || label.equalsIgnoreCase("message")){

            if(args.length < 2){
                sender.sendMessage("§cSyntaxe incorrecte ! Usage : §7/msg [§aJOUEUR§7] [§aMESSAGE§7]");
                return true;
            }

            String target = args[0];
            Player t = Bukkit.getPlayer(target);

            if(t == null || !t.isOnline()){
                sender.sendMessage("§cLe joueur §6" + target + " §cn'est pas connecté ou est erroné.");
                return true;
            }

            if(t.getName().equalsIgnoreCase(sender.getName())){
                sender.sendMessage("§cVous ne pouvez pas vous envoyer un message.");
                return true;
            }

            String msg = String.join(" ", args).substring(args[0].length() + 1);

            if(sender instanceof Player){

                Player p = (Player) sender;

                p.sendMessage("§7[§eEnvoyé à §6" + t.getName() + " §7] -> §b§o" + msg);
                t.sendMessage("§7[§eDe §6" + p.getName() + " §7] -> §b§o" + msg);

                lastMsgSender.put(t, p);
                return true;
            }

            sender.sendMessage("§7[§eEnvoyé à §6" + t.getName() + "§7] -> §b§o" + msg);
            t.sendMessage("§7[§eDe §6CONSOLE§7] -> §b§o" + msg);

        }

        if(label.equalsIgnoreCase("r") || label.equalsIgnoreCase("reply")){

            if (!(sender instanceof Player)) {
                sender.sendMessage(Main.getInstance().playerCmd());
                return true;
            }

            Player player = (Player) sender;
            Player lastSender = lastMsgSender.get(player);

            if (lastSender == null || !lastSender.isOnline()) {
                player.sendMessage("§cVous n'avez personne a qui répondre.");
                return true;
            }

            if (args.length < 1) {
                player.sendMessage("§cSyntaxe incorrecte ! Usage : §7/r [§aMESSAGE§7]");
                return true;
            }

            // Construire le message
            String message = String.join(" ", args);

            // Envoyer le message
            player.sendMessage("§7[§eEnvoyé à §6" + lastSender.getName() + " §7] -> §b§o" + message);
            lastSender.sendMessage("§7[§eDe §6" + player.getName() + " §7] -> §b§o" + message);

            // Mettre à jour le dernier message envoyé
            lastMsgSender.put(lastSender, player);

            return true;

        }

        return false;
    }
}
