package fr.syfalogryl.mcbasics.global.commands;

import fr.syfalogryl.mcbasics.Main;
import fr.syfalogryl.mcbasics.global.managers.PermsManager;
import fr.syfalogryl.mcbasics.global.utils.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdRanks implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // Vérification si l'exécuteur est un joueur
        if (!(sender instanceof Player)) {
            CommandSender p = sender;

            if (args.length != 3 || !args[0].equalsIgnoreCase("set")) {
                p.sendMessage("§cSyntaxe incorrecte ! Usage : §7/rank set [§aJOUEUR§7] [§aRANK§7]");
                return true;
            }


            String targetName = args[1];
            Player t = Bukkit.getPlayer(targetName);

            // Vérification si le joueur cible existe
            if (t == null) {
                p.sendMessage("§cLe joueur §6" + targetName + " §cn'est pas en ligne ou n'existe pas.");
                return true;
            }

            String rank = args[2].toLowerCase(); // Gestion insensible à la casse
            List<String> playerPermissions = PermsManager.getPermissionsConfig().getStringList(t.getName() + ".Permissions");
            String perm;

            // Traitement des différents rangs
            switch (rank) {

                case "admin":
                    perm = "Administrateur";

                    if (!playerPermissions.contains("mcbasics.*")) {
                        playerPermissions.clear();
                        playerPermissions.add("mcbasics.*");
                        updatePermissionsAndRank(t, playerPermissions);
                        p.sendMessage("§7Le grade §6" + perm + " §7a été attribué au joueur §6" + t.getName());
                    } else {
                        p.sendMessage("§7Le joueur possède déjà le grade §6" + perm + "§7!");
                    }

                    break;

                case "modo":
                    perm = "Modérateur";
                    if (!playerPermissions.contains("mcbasics.mod")) {
                        playerPermissions.add("mcbasics.mod");
                        updatePermissionsAndRank(t, playerPermissions);
                        p.sendMessage("§7Le grade §6" + perm + " §7a été attribué au joueur §6" + t.getName());
                    } else {
                        p.sendMessage("§7Le joueur possède déjà le grade §6" + perm + "§7!");
                    }
                    break;

                case "vip":
                    perm = "VIP";

                    if (!playerPermissions.contains("mcbasics.vip")) {
                        if(playerPermissions.contains("mcbasics.mod")){
                            playerPermissions.remove("mcbasics.mod");
                            return true;
                        }
                        playerPermissions.add("mcbasics.vip");
                        updatePermissionsAndRank(t, playerPermissions);
                        p.sendMessage("§7Le grade §6" + perm + " §7a été attribué au joueur §6" + t.getName());
                    } else {
                        p.sendMessage("§7Le joueur possède déjà le grade §6" + perm + "§7!");
                    }
                    break;

                case "joueur":
                    perm = "Joueur";
                    if (!playerPermissions.isEmpty()) {
                        playerPermissions.clear(); // Suppression de toutes les permissions
                        updatePermissionsAndRank(t, playerPermissions);
                        p.sendMessage("§7Le grade §6" + perm + " §7a été attribué au joueur §6" + t.getName());
                    } else {
                        p.sendMessage("§7Le joueur possède déjà le grade §6" + perm + "§7!");
                    }
                    break;

                default:
                    p.sendMessage("§cLe grade spécifié n'existe pas. Grades disponibles : §7modo, vip, joueur.");
                    break;
            }
            return true;
        }

        Player p = (Player) sender;

        if(!PermsManager.hasPermission(p, "mcbasics.mod")){
            p.sendMessage(Main.getInstance().noPerm());
            return false;
        }

        // Vérification de la syntaxe de base
        if (args.length != 3 || !args[0].equalsIgnoreCase("set")) {
            p.sendMessage("§cSyntaxe incorrecte ! Usage : §7/rank set [§aJOUEUR§7] [§aRANK§7]");
            return true;
        }

        String targetName = args[1];
        Player t = Bukkit.getPlayer(targetName);

        // Vérification si le joueur cible existe
        if (t == null) {
            p.sendMessage("§cLe joueur §6" + targetName + " §cn'est pas en ligne ou n'existe pas.");
            return true;
        }

        String rank = args[2].toLowerCase(); // Gestion insensible à la casse
        List<String> playerPermissions = PermsManager.getPermissionsConfig().getStringList(t.getName() + ".Permissions");
        String perm;

        // Traitement des différents rangs
        switch (rank) {

            case "admin":
                perm = "Administrateur";

                if (!playerPermissions.contains("mcbasics.*")) {
                    playerPermissions.clear();
                    playerPermissions.add("mcbasics.*");
                    updatePermissionsAndRank(t, playerPermissions);
                    p.sendMessage("§7Le grade §6" + perm + " §7a été attribué au joueur §6" + t.getName());
                } else {
                    p.sendMessage("§7Le joueur possède déjà le grade §6" + perm + "§7!");
                }

                break;

            case "modo":
                perm = "Modérateur";
                if(playerPermissions.contains("mcbasics.*")){
                    p.sendMessage("§cImpossible d'enlever un grade à ce joueur. Contactez un administrateur.");
                    return false;
                }
                if (!playerPermissions.contains("mcbasics.mod")) {
                    playerPermissions.add("mcbasics.mod");
                    updatePermissionsAndRank(t, playerPermissions);
                    p.sendMessage("§7Le grade §6" + perm + " §7a été attribué au joueur §6" + t.getName());
                } else {
                    p.sendMessage("§7Le joueur possède déjà le grade §6" + perm + "§7!");
                }
                break;

            case "vip":
                perm = "VIP";

                if(playerPermissions.contains("mcbasics.*")){
                    p.sendMessage("§cImpossible d'enlever un grade à ce joueur. Contactez un administrateur.");
                    return false;
                }

                if (!playerPermissions.contains("mcbasics.vip")) {
                    if(playerPermissions.contains("mcbasics.mod")){
                        playerPermissions.remove("mcbasics.mod");
                        return true;
                    }
                    playerPermissions.add("mcbasics.vip");
                    updatePermissionsAndRank(t, playerPermissions);
                    p.sendMessage("§7Le grade §6" + perm + " §7a été attribué au joueur §6" + t.getName());
                } else {
                    p.sendMessage("§7Le joueur possède déjà le grade §6" + perm + "§7!");
                }
                break;

            case "joueur":
                perm = "Joueur";
                if(playerPermissions.contains("mcbasics.*")){
                    p.sendMessage("§cImpossible d'enlever un grade à ce joueur. Contactez un administrateur.");
                    return false;
                }
                if (!playerPermissions.isEmpty()) {
                    if(playerPermissions.contains("mcbasics.mod")){
                        playerPermissions.remove("mcbasics.mod");
                    }
                    playerPermissions.clear(); // Suppression de toutes les permissions
                    updatePermissionsAndRank(t, playerPermissions);
                    p.sendMessage("§7Le grade §6" + perm + " §7a été attribué au joueur §6" + t.getName());
                } else {
                    p.sendMessage("§7Le joueur possède déjà le grade §6" + perm + "§7!");
                }
                break;

            default:
                p.sendMessage("§cLe grade spécifié n'existe pas. Grades disponibles : §7modo, vip, joueur.");
                break;
        }

        return true;
    }
    private void updatePermissionsAndRank(Player player, List<String> permissions) {
        // Mise à jour des permissions dans la configuration
        PermsManager.getPermissionsConfig().set(player.getName() + ".Permissions", permissions);
        PermsManager.savePermissionsFile();

        // Mise à jour du rang dans l'affichage (tab)
        ChatManager.setTabRank(player);
    }
}