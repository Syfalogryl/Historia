package fr.syfalogryl.mcbasics.global.commands;

import fr.syfalogryl.mcbasics.Main;
import fr.syfalogryl.mcbasics.global.utils.ChatManager;
import fr.syfalogryl.mcbasics.global.managers.PermsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdChat implements CommandExecutor {

    private final Main instance = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            // Vérification des permissions
            if (!PermsManager.hasPermission(p, "mcbasics.mod")) {
                p.sendMessage(instance.noPerm());
                return true;
            }

            String worldName = p.getWorld().getName();

            if (args.length != 1) {
                p.sendMessage("§cSyntaxe incorrecte ! Usage : §7/chat [§aON§7/§cOFF§7/§6toggle§7/§estatus§7] | /chat clear");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "on":
                    if (!ChatManager.getChatStatus(worldName)) {
                        ChatManager.setChatStatus(worldName, true);
                        p.getWorld().getPlayers().forEach(player ->
                                player.sendMessage("§aLe chat a été ré-activé."));
                    } else {
                        p.sendMessage("§cLe chat est déjà activé.");
                    }
                    break;

                case "off":
                    if (ChatManager.getChatStatus(worldName)) {
                        ChatManager.setChatStatus(worldName, false);
                        p.getWorld().getPlayers().forEach(player ->
                                player.sendMessage("§cLe chat a été désactivé."));
                    } else {
                        p.sendMessage("§cLe chat est déjà désactivé.");
                    }
                    break;

                case "toggle":
                    if (ChatManager.getChatStatus(worldName)) {
                        ChatManager.setChatStatus(worldName, false);
                        p.getWorld().getPlayers().forEach(player ->
                                player.sendMessage("§cLe chat a été désactivé."));
                    } else {
                        ChatManager.setChatStatus(worldName, true);
                        p.getWorld().getPlayers().forEach(player ->
                                player.sendMessage("§aLe chat a été ré-activé."));
                    }
                    break;

                case "status":
                    if (ChatManager.getChatStatus(worldName)) {
                        p.sendMessage("§7Le chat est §aactivé§7.");
                    } else {
                        p.sendMessage("§7Le chat est §cdésactivé§7.");
                    }
                    break;

                case "clear":
                    for (int i = 0; i < 200; i++) {
                        p.getWorld().getPlayers().forEach(player -> player.sendMessage(" "));
                    }
                    p.getWorld().getPlayers().forEach(player ->
                            player.sendMessage("§7Le chat a été nettoyé par §c" + p.getName()));
                    break;

                default:
                    p.sendMessage("§cSyntaxe incorrecte ! Usage : §7/chat [§aON§7/§cOFF§7/§6toggle§7/§estatus§7] | /chat clear");
                    break;
            }

        } else {
            sender.sendMessage("§cSeuls les joueurs peuvent utiliser cette commande.");
        }

        return true;
    }
}
