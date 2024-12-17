package fr.syfalogryl.mcbasics.global.commands;

import fr.syfalogryl.mcbasics.Main;
import fr.syfalogryl.mcbasics.global.managers.PermsManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdGamemodes implements CommandExecutor {

    private static final Main instance = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(instance.playerCmd());
            return false;
        }

        Player player = (Player) sender;

        if (!PermsManager.hasPermission(player, "mcbasics.mod")) {
            player.sendMessage(instance.noPerm());
            return false;
        }

        // Handle specific commands (gmc, gms, gma, gmsp)
        switch (label.toLowerCase()) {
            case "gmc":
                return setGameMode(player, args, GameMode.CREATIVE);
            case "gms":
                return setGameMode(player, args, GameMode.SURVIVAL);
            case "gma":
                return setGameMode(player, args, GameMode.ADVENTURE);
            case "gmsp":
                return setGameMode(player, args, GameMode.SPECTATOR);
            case "gamemode":
            case "gm":
                return handleGamemodeCommand(player, args);
            default:
                player.sendMessage("§cCommande inconnue.");
                return false;
        }
    }

    private boolean setGameMode(Player player, String[] args, GameMode gameMode) {
        Player target = args.length > 0 ? Bukkit.getPlayer(args[0]) : player;

        if (target == null) {
            player.sendMessage("§7Le joueur §6" + args[0] + " §7n'est pas connecté ou n'existe pas.");
            return false;
        }

        target.setGameMode(gameMode);
        target.sendMessage("§7Passage en mode §a" + gameMode.name() + "§7.");

        if (!target.equals(player)) {
            player.sendMessage("§7Passage du joueur §6" + target.getName() + " §7en mode §a" + gameMode.name() + "§7.");
        }

        return true;
    }

    private boolean handleGamemodeCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§cSyntaxe incorrecte ! §7/gamemode [MODE] [JOUEUR]");
            return false;
        }

        String modeInput = args[0].toLowerCase();
        GameMode gameMode = getGameMode(modeInput);

        if (gameMode == null) {
            player.sendMessage("§cMode de jeu invalide : " + modeInput);
            return false;
        }

        Player target = args.length > 1 ? Bukkit.getPlayer(args[1]) : player;

        if (target == null) {
            player.sendMessage("§7Le joueur §6" + args[1] + " §7n'est pas connecté ou n'existe pas.");
            return false;
        }

        target.setGameMode(gameMode);
        target.sendMessage("§7Passage en mode §a" + gameMode.name() + "§7.");

        if (!target.equals(player)) {
            player.sendMessage("§7Passage du joueur §6" + target.getName() + " §7en mode §a" + gameMode.name() + "§7.");
        }

        return true;
    }

    private GameMode getGameMode(String input) {
        switch (input) {
            case "0":
            case "survival":
                return GameMode.SURVIVAL;
            case "1":
            case "creative":
                return GameMode.CREATIVE;
            case "2":
            case "adventure":
                return GameMode.ADVENTURE;
            case "3":
            case "spectator":
                return GameMode.SPECTATOR;
            default:
                return null;
        }
    }
}