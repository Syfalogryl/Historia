package fr.syfalogryl.mcbasics.global.commands;

import fr.syfalogryl.mcbasics.global.managers.PermsManager;
import fr.syfalogryl.mcbasics.global.utils.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdPermissions implements CommandExecutor {

    private boolean isValidPermission(String permission){
        return permission.startsWith("mcbasics.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

       if(sender instanceof Player ){
            Player p = (Player) sender;

            if(!PermsManager.hasPermission(p, "mcbasics.mod")){
                p.sendMessage("§cVous n'avez pas la permission d'éxecuter cette commande.");
                return false;
            }

            if(args.length < 1 ||args.length > 3){
                p.sendMessage("§cSyntaxe incorrecte ! Usages :");
                p.sendMessage("§7/permissions add [§aJOUEUR§7] [§aPERMISSION§7]");
                p.sendMessage("§7/permissions remove [§aJOUEUR§7] [§aPERMISSION§7]");
                p.sendMessage("§7/permissions list [§aJOUEUR§7]");
                return false;
            }

            // PERMISSION GIVE

           if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){

               PermsManager.reloadPermissionsFile();
               p.sendMessage("§cLa configuration viens d'être rechargée.");
               return true;
           }

            if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("give")){

                Player t = Bukkit.getPlayer(args[1]);

                if (t != null) {

                    if(args.length != 3){

                        p.sendMessage("§cSyntaxe incorrecte ! Usage : ");
                        p.sendMessage("§7/permissions add [§aJOUEUR§7] [§aPERMISSION§7]");
                        return false;
                    }

                    if (!isValidPermission(args[2])) {
                        p.sendMessage("§cLa permission doit débuter par : mcbasics.§6X §c!");
                        return false;
                    }

                    String permission = args[2];

                    List<String> playerPermissions = PermsManager.getPermissionsConfig().getStringList(t.getName() + ".Permissions");

                    if(permission.equalsIgnoreCase("mcbasics.*")){
                        p.sendMessage("§cLa permission §6mcbasics.* §cne peut être donnée à un joueur.");
                        return false;
                    }

                    if (!playerPermissions.contains(permission)) {

                        playerPermissions.add(permission);
                        PermsManager.getPermissionsConfig().set(t.getName() + ".Permissions", playerPermissions);
                        PermsManager.savePermissionsFile();
                        ChatManager.setTabRank(p);
                        p.sendMessage("§La permission §6" + permission + " §7à été attribuée au joueur §6" + t.getName());

                    } else {
                        p.sendMessage("§cLe joueur possède déja la permission §6" + permission + " §c!");
                        return false;
                    }

                } else p.sendMessage("§cLe joueur §6" + args[1] + " §cn'est pas connecté ou n'existe pas.");

            }

            // PERMISSION REMOVE

            if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("del")) {

                Player target = Bukkit.getPlayer(args[1]);

                if (target != null) {

                    if (args.length == 3) {

                        String permission = args[2];
                        List<String> playerPermissions = PermsManager.getPermissionsConfig().getStringList(target.getName() + ".Permissions");

                        if (playerPermissions.contains(permission)) {

                            if(permission.equalsIgnoreCase("mcbasics.*")){
                                p.sendMessage("§cLa permission §6mcbasics.* §cne peut être retirée d'un un joueur.");
                                return false;
                            }

                            playerPermissions.remove(permission);
                            PermsManager.getPermissionsConfig().set(target.getName() + ".Permissions", playerPermissions);
                            PermsManager.savePermissionsFile();
                            ChatManager.setTabRank(target);
                            p.sendMessage("§cLa permission §6" + permission + " §cà été retirée au joueur §6" + target.getName() + " §c!");
                        } else {
                            p.sendMessage("§cLe joueur §6" + target.getName() + " §cne possède pas la permission §6" + permission + " §c!");
                            return false;
                        }

                    } else {
                        p.sendMessage("§cSyntaxe incorrecte ! Usage : ");
                        p.sendMessage("§7/permissions remove [§aJOUEUR§7] [§aPERMISSION§7]");
                        return false;
                    }

                } else {
                    p.sendMessage("§cLe joueur §6" + args[1] + " §cn'est pas connecté ou n'existe pas.");
                }

            }

            // PERMISSION LIST

            if(args[0].equalsIgnoreCase("list")) {

                if(args.length == 1){
                    List<String> playerPermissions = PermsManager.getPermissionsConfig().getStringList(p.getName() + ".Permissions");

                    if (playerPermissions.isEmpty()) {
                        p.sendMessage("§cVous ne possédez aucune permission.");
                        return false;
                    } else {
                        p.sendMessage("§cListe de vos permissions :");
                        playerPermissions.forEach(permission -> p.sendMessage("§7- " + permission));
                    }
                }
                else if (args.length == 2) {

                    String playerName = args[1];
                    List<String> playerPermissions = PermsManager.getPermissionsConfig().getStringList(playerName + ".Permissions");

                    if (playerPermissions.isEmpty()) {
                        p.sendMessage("§cLe joueur §6" + playerName + " §cne possède aucune permission.");
                        return false;
                    } else {
                        p.sendMessage("§cListe des permissions de §6" + playerName + " §7:");
                        playerPermissions.forEach(permission -> p.sendMessage("§7- " + permission));
                    }

                } else {

                    p.sendMessage("§cSyntaxe incorrecte ! Usage : ");
                    p.sendMessage("§7/permissions list [§aJOUEUR§7]");
                    return false;

                }
            }
            return true;

        } else {

           CommandSender p = sender;

           if(args.length < 1 ||args.length > 3){
               p.sendMessage("§cSyntaxe incorrecte ! Usages :");
               p.sendMessage("§7/permissions add [§aJOUEUR§7] [§aPERMISSION§7]");
               p.sendMessage("§7/permissions remove [§aJOUEUR§7] [§aPERMISSION§7]");
               p.sendMessage("§7/permissions list [§aJOUEUR§7]");
               return false;
           }

           if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
               PermsManager.reloadPermissionsFile();
               p.sendMessage("§cLa configuration viens d'être rechargée.");
               return true;
           }

           // PERMISSION ADD

           if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("give")){

               Player t = Bukkit.getPlayer(args[1]);

               if (t != null) {

                   if(args.length != 3){

                       p.sendMessage("Syntaxe incorrecte ! Usage : ");
                       p.sendMessage("/permissions add [JOUEUR] [PERMISSION]");
                       return false;
                   }

                   if (!isValidPermission(args[2])) {
                       p.sendMessage("La permission doit débuter par : mcbasics.X !");
                       return false;
                   }

                   String permission = args[2];

                   List<String> playerPermissions = PermsManager.getPermissionsConfig().getStringList(t.getName() + ".Permissions");

                   if (!playerPermissions.contains(permission)) {

                       playerPermissions.add(permission);
                       PermsManager.getPermissionsConfig().set(t.getName() + ".Permissions", playerPermissions);
                       PermsManager.savePermissionsFile();
                       ChatManager.setTabRank(t);
                       p.sendMessage("La permission " + permission + " à été attribuée au joueur " + t.getName());

                   } else {
                       p.sendMessage("Le joueur possède déja la permission " + permission + " !");
                       return false;
                   }

               } else p.sendMessage("Le joueur " + args[1] + " n'est pas connecté ou n'existe pas.");

           }

           // PERMISSION REMOVE

           if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("del")) {

               Player target = Bukkit.getPlayer(args[1]);

               if (target != null) {

                   if (args.length == 3) {

                       String permission = args[2];
                       List<String> playerPermissions = PermsManager.getPermissionsConfig().getStringList(target.getName() + ".Permissions");

                       if (playerPermissions.contains(permission)) {

                           playerPermissions.remove(permission);
                           PermsManager.getPermissionsConfig().set(target.getName() + ".Permissions", playerPermissions);
                           PermsManager.savePermissionsFile();
                           ChatManager.setTabRank(target);
                           p.sendMessage("La permission " + permission + " à été retirée au joueur " + target.getName() + " !");
                       } else {
                           p.sendMessage("Le joueur " + target.getName() + " ne possède pas la permission " + permission + " !");
                           return false;
                       }

                   } else {
                       p.sendMessage("Syntaxe incorrecte ! Usage : ");
                       p.sendMessage("/permissions remove [JOUEUR] [PERMISSION]");
                       return false;
                   }

               } else {
                   p.sendMessage("Le joueur " + args[1] + " n'est pas connecté ou n'existe pas.");
               }

           }

           // PERMISSION LIST

           if(args[0].equalsIgnoreCase("list")) {
               if (args.length == 2) {

                   String playerName = args[1];
                   List<String> playerPermissions = PermsManager.getPermissionsConfig().getStringList(playerName + ".Permissions");

                   if (playerPermissions.isEmpty()) {
                       p.sendMessage("Le joueur " + playerName + " ne possède aucune permission.");
                       return false;
                   } else {
                       p.sendMessage("Liste des permissions de " + playerName + " :");
                       playerPermissions.forEach(permission -> p.sendMessage("- " + permission));
                   }

               } else {

                   p.sendMessage("Syntaxe incorrecte ! Usage : ");
                   p.sendMessage("/permissions list [JOUEUR]");
                   return false;

               }
           }
       }

        return false;
    }
}
