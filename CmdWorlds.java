package fr.syfalogryl.mcbasics.global.commands;

import fr.syfalogryl.mcbasics.Main;
import fr.syfalogryl.mcbasics.global.managers.PermsManager;
import fr.syfalogryl.mcbasics.global.managers.WorldsManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class CmdWorlds implements CommandExecutor {

    static Main instance = Main.getInstance();
    static Set<String> worldRemoveConfirm = new HashSet<>();


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;

            if(!PermsManager.hasPermission(p, "mcbasics.mod")){
                p.sendMessage(instance.noPerm());
                return false;
            }

        }

        if(label.equalsIgnoreCase("setspawn") || label.equalsIgnoreCase("setworldspawn")){

            if(!(sender instanceof Player)){
                sender.sendMessage(instance.playerCmd());
                return true;
            }

            Player p = (Player) sender;

            World world = p.getWorld();
            Location loc = p.getLocation();
            String worldName = world.getName().toString();

            world.setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

            WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Name", worldName);
            WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Type", world.getWorldType().toString());
            WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Size", world.getWorldBorder().getSize());
            WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Creator", sender.getName().toString());
            WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Spawnpoint", world.getSpawnLocation().getX() + "," + world.getSpawnLocation().getY() + "," + world.getSpawnLocation().getZ());
            WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Status", WorldsManager.isWorldLoaded(world));
            WorldsManager.saveWorldsFile();
            p.sendMessage("§cLe point de spawn du monde §6" + worldName + " §cà été modifié à votre position actuelle.");                return true;
        }

        if(args.length < 1){
            sender.sendMessage("§cSyntaxe incorrecte ! Usage :");
            sender.sendMessage("§7/worlds create [§aWORLDNAME§7] [§aWORLDTYPE§7] [§aWORLDSIZE§7]");
            sender.sendMessage("§7/worlds remove [§aWORLDNAME§7]");
            sender.sendMessage("§7/worlds teleport [§aWORLDNAME§7]");
            sender.sendMessage("§7/worlds setspawn [§aWORLDNAME§7]");
            sender.sendMessage("§7/worlds list");
            return true;
        }

        if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
            sender.sendMessage("§cLa configuration viens d'être rechargée.");
            WorldsManager.reloadWorldsFile();
            return true;
        }

        if (args[0].equalsIgnoreCase("load")) {
            if (args.length != 2) {
                sender.sendMessage("§cSyntaxe incorrecte ! Usage : §7/worlds load [§aWORLDNAME§7]");
                return true;
            }

            String worldName = args[1];
            File worldFolder = new File(Bukkit.getWorldContainer(), worldName);

            // Vérifier si le dossier du monde existe
            if (!worldFolder.exists() || !worldFolder.isDirectory()) {
                sender.sendMessage("§cLe dossier du monde §6" + worldName + " §cn'existe pas. Impossible de charger ce monde.");
                return true;
            }

            // Vérifier si le monde est déjà chargé
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                sender.sendMessage("§cLe monde §6" + worldName + " §cest déjà chargé.");
                return true;
            }

            // Charger un monde depuis le disque
            world = Bukkit.createWorld(new WorldCreator(worldName));
            if (world == null) {
                sender.sendMessage("§cUne erreur s'est produite lors du chargement du monde §6" + worldName + "§c.");
                return false;
            }

            // Gestion de la configuration
            FileConfiguration config = WorldsManager.getWorldsConfig();
            ConfigurationSection worldsSection = config.getConfigurationSection("Worlds");

            if (worldsSection == null) {
                worldsSection = config.createSection("Worlds");
            }

            ConfigurationSection worldSection = worldsSection.getConfigurationSection(worldName);
            if (worldSection == null) {
                worldSection = worldsSection.createSection(worldName);
            }

            // Sauvegarder les informations du monde
            worldSection.set("Name", worldName);
            worldSection.set("Type", world.getWorldType().toString());
            worldSection.set("Size", world.getWorldBorder().getSize());
            worldSection.set("Creator", sender.getName());
            worldSection.set("Spawnpoint",
                    world.getSpawnLocation().getX() + "," +
                            world.getSpawnLocation().getY() + "," +
                            world.getSpawnLocation().getZ());
            worldSection.set("Status", "LOADED");

            WorldsManager.saveWorldsFile();

            sender.sendMessage("§cLe monde §6" + world.getName() + " §ca été chargé avec succès.");
            return true;
        }

        if (args[0].equalsIgnoreCase("unload")) {
            if (args.length != 2) {
                sender.sendMessage("§cSyntaxe incorrecte ! Usage : §7/worlds unload [§aWORLDNAME§7]");
                return true;
            }

            if (args[0].equalsIgnoreCase("unload")) {

                World world = Bukkit.getWorld(args[1]);

                if (world == null) {
                    sender.sendMessage("§cLe monde §6" + args[1] + " §cn'existe pas ou n'est pas chargé.");
                    return true;
                }

                // Décharger le monde
                boolean succes = Bukkit.unloadWorld(world, true);

                if (!succes) {
                    sender.sendMessage("§cImpossible de décharger le monde §6" + world.getName());
                    return false;
                }

                // Gestion de la configuration
                FileConfiguration config = WorldsManager.getWorldsConfig();
                ConfigurationSection worldsSection = config.getConfigurationSection("Worlds");

                if (worldsSection == null) {
                    worldsSection = config.createSection("Worlds");
                }

                ConfigurationSection worldSection = worldsSection.getConfigurationSection(world.getName());
                if (worldSection == null) {
                    worldSection = worldsSection.createSection(world.getName());
                }

                worldSection.set("Status", "UNLOAD");

                // Sauvegarder la configuration
                WorldsManager.saveWorldsFile();

                sender.sendMessage("§cLe monde §6" + world.getName() + " §ca été déchargé.");
                return true;
            }
        }
        if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("add")){

            if(args.length == 2){

                String worldName = args[1];

                if(Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))){
                    sender.sendMessage("§cLe monde §6" + worldName + " §cexiste déja.");
                    return false;
                }

                WorldCreator creator = new WorldCreator(worldName);
                creator.type(WorldType.NORMAL);
                sender.sendMessage("§cCréation du monde §6" + worldName + " §cen cours...");

                // WORLD BORDER
                World world = Bukkit.createWorld(creator);
                WorldBorder border = world.getWorldBorder();
                border.setCenter(world.getSpawnLocation()); // Centre au spawn du monde
                border.setSize(10000 * 2); // Taille totale : diamètre = taille spécifiée * 2
                border.setDamageAmount(2.0); // Dommages par seconde en dehors de la bordure
                border.setWarningDistance(5); // Distance d'avertissement

                sender.sendMessage("§cCréation du monde §6" + worldName + " §cterminée.");

                if (world != null) {
                    // Save world data
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Name", worldName);
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Type", world.getWorldType().toString());
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Size", world.getWorldBorder().getSize());
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Creator", sender.getName().toString());
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Spawnpoint",
                            world.getSpawnLocation().getX() + "," +
                                    world.getSpawnLocation().getY() + "," +
                                    world.getSpawnLocation().getZ());
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Status", WorldsManager.isWorldLoaded(world));
                    WorldsManager.saveWorldsFile();
                    return true;
                }

            }

            if(args.length == 3){

                String worldName = args[1];
                String worldType = args[2];

                WorldType type;

                try {
                    type = WorldType.valueOf(worldType.toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("§cLe type de monde spécifié n'existe pas.");
                    return false;
                }

                if(Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))){
                    sender.sendMessage("§cLe monde §6" + worldName + " §cexiste déja.");
                    return false;
                }

                WorldCreator creator = new WorldCreator(worldName);
                creator.type(type);
                sender.sendMessage("§cCréation du monde §6" + worldName + " §cen cours...");

                // WORLD BORDER
                World world = Bukkit.createWorld(creator);
                WorldBorder border = world.getWorldBorder();
                border.setCenter(world.getSpawnLocation()); // Centre au spawn du monde
                border.setSize(10000 * 2); // Taille totale : diamètre = taille spécifiée * 2
                border.setDamageAmount(2.0); // Dommages par seconde en dehors de la bordure
                border.setWarningDistance(5); // Distance d'avertissement

                sender.sendMessage("§cCréation du monde §6" + worldName + " §cterminée.");

                if (world != null) {
                    // Save world data
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Name", worldName);
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Type", worldType);
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Size", world.getWorldBorder().getSize());
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Creator", sender.getName().toString());
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Spawnpoint",
                            world.getSpawnLocation().getX() + "," +
                                    world.getSpawnLocation().getY() + "," +
                                    world.getSpawnLocation().getZ());
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Status", WorldsManager.isWorldLoaded(world));

                    WorldsManager.saveWorldsFile();
                    return true;
                }

            }

            if(args.length == 4){
                String worldName = args[1];
                String worldType = args[2];
                int worldSize;

                try {
                    worldSize = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cLa taille du monde doit être un nombre entier.");
                    return true;
                }

                WorldType type;

                try {
                    type = WorldType.valueOf(worldType.toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("§cLe type de monde spécifié n'existe pas.");
                    return false;
                }

                if(Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))){
                    sender.sendMessage("§cLe monde §6" + worldName + " §cexiste déja.");
                    return false;
                }

                WorldCreator creator = new WorldCreator(worldName);
                creator.type(type);
                sender.sendMessage("§cCréation du monde §6" + worldName + " §cen cours...");

                // WORLD BORDER
                World world = Bukkit.createWorld(creator);
                WorldBorder border = world.getWorldBorder();
                border.setCenter(world.getSpawnLocation()); // Centre au spawn du monde
                border.setSize(worldSize * 2); // Taille totale : diamètre = taille spécifiée * 2
                border.setDamageAmount(2.0); // Dommages par seconde en dehors de la bordure
                border.setWarningDistance(5); // Distance d'avertissement

                sender.sendMessage("§cCréation du monde §6" + worldName + " §cterminée.");

                if (world != null) {
                    // Save world data
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Name", worldName);
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Type", worldType);
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Size", worldSize);
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Creator", sender.getName().toString());
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Spawnpoint",
                            world.getSpawnLocation().getX() + "," +
                                    world.getSpawnLocation().getY() + "," +
                                    world.getSpawnLocation().getZ());
                    WorldsManager.getWorldsConfig().set("Worlds." + worldName + ".Status", WorldsManager.isWorldLoaded(world));

                    WorldsManager.saveWorldsFile();
                    return true;
                }
            }
        }

        if (args[0].equalsIgnoreCase("list")) {

            FileConfiguration config = WorldsManager.getWorldsConfig();

            if (config.contains("Worlds")) {
                Set<String> worldNames = config.getConfigurationSection("Worlds").getKeys(false);

                if (worldNames.isEmpty()) {
                    sender.sendMessage("§cAucun monde existant sur le serveur.");
                } else {
                    sender.sendMessage("§cListe des mondes existants :");
                    for (String worldName : worldNames) {
                        String status = config.getString("Worlds." + worldName + ".Status", "UNLOAD"); // Récupère le statut
                        String color = status.equalsIgnoreCase("LOADED") ? "§a" : "§c"; // Couleur en fonction du statut
                        sender.sendMessage("§7- " + worldName + " : " + color + status.toUpperCase());
                    }
                }
            } else {
                sender.sendMessage("§cAucun monde existant sur le serveur.");
            }

        }

        if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(instance.playerCmd());
                return false;
            }

            Player p = (Player) sender;

            if (args.length < 2) {
                p.sendMessage("§cSyntaxe incorrecte ! Usage :");
                p.sendMessage("§7/world teleport [§aWORLDNAME§7]");
                return true;
            }

            String worldName = args[1];

            World world = Bukkit.getWorld(worldName);

            // Vérification si le monde est chargé
            if (world != null) {
                p.teleport(world.getSpawnLocation());
                p.sendMessage("§cTéléportation dans le monde §6" + worldName + " §c.");
            } else {
                // Vérification si le monde est listé dans worlds.yml
                FileConfiguration config = WorldsManager.getWorldsConfig();
                ConfigurationSection worldsSection = config.getConfigurationSection("Worlds");

                if (worldsSection != null && worldsSection.contains(worldName)) {
                    // Le monde est listé mais déchargé
                    p.sendMessage("§cLe monde §6" + worldName + " §cest actuellement déchargé.");
                } else {
                    // Le monde n'existe pas
                    p.sendMessage("§cLe monde §6" + worldName + " §cn'existe pas ou est erroné.");
                }
            }
        }

        if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")) {

            if (args.length < 2) {
                sender.sendMessage("§cSyntaxe incorrecte ! Usage :");
                sender.sendMessage("§7/world remove [§aWORLDNAME§7]");
                return true;
            }

            String worldName = args[1];

            // Vérifier si le monde est déjà en attente de suppression
            if (worldRemoveConfirm.contains(worldName)) {
                sender.sendMessage("§cLe monde §6" + worldName + " §cest déjà en attente de suppression.");
                return true;
            }

            // Ajouter le monde dans la liste d'attente
            worldRemoveConfirm.add(worldName);
            sender.sendMessage("§cTentative de suppression du monde §6" + worldName + " §c. Faites /worlds confirm §6" + worldName + " §cpour confirmer la suppression.");
            return true;
        }

        if (args[0].equalsIgnoreCase("confirm")) {

            if (args.length < 2) {
                sender.sendMessage("§cSyntaxe incorrecte ! Usage :");
                sender.sendMessage("§7/world confirm [§aWORLDNAME§7]");
                return true;
            }

            String worldName = args[1];

            if (!worldRemoveConfirm.contains(worldName)) {
                sender.sendMessage("§cLe monde §6" + worldName + " §cn'est pas en attente de suppression.");
                return true;
            }

            // Tenter de décharger le monde s'il est chargé
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                boolean success = Bukkit.unloadWorld(world, false); // 'false' pour éviter la sauvegarde
                if (!success) {
                    sender.sendMessage("§cImpossible de décharger le monde §6" + worldName + " §c.");
                    return true;
                }
            }

            // Supprimer l'entrée dans le fichier worlds.yml
            WorldsManager.getWorldsConfig().set("Worlds." + worldName, null);
            WorldsManager.saveWorldsFile();

            // Supprimer les fichiers du monde
            File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
            if (deleteDirectory(worldFolder)) {
                sender.sendMessage("§cLe monde §6" + worldName + " §ca été supprimé avec succès.");
            } else {
                sender.sendMessage("§cImpossible de supprimer les fichiers du monde §6" + worldName + "§c.");
            }

            // Retirer le monde de la liste temporaire
            worldRemoveConfirm.remove(worldName);
            return true;
        }

        return false;
    }

    private boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (!deleteDirectory(child)) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

}
