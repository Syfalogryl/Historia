package fr.syfalogryl.mcbasics.global.managers;

import fr.syfalogryl.mcbasics.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WorldsManager {

    private static File worldsFile;
    private static FileConfiguration worldsConfig;
    private static Main instance = Main.getInstance();

    public static void setupWorldsFile(){
        worldsFile = new File(Main.getInstance().getDataFolder(), "worlds.yml");
        if(!worldsFile.exists()){
            worldsFile.getParentFile().mkdirs();
            Main.getInstance().saveResource("worlds.yml", false);
        }
        worldsConfig = YamlConfiguration.loadConfiguration(worldsFile);
    }
    public static void saveWorldsFile(){
        try{
            worldsConfig.save(worldsFile);
        } catch (IOException e){
            Main.getInstance().getLogger().severe("Impossible de sauvegarder le fichier permissions.yml.");
            e.printStackTrace();
        }
    }

    public static void reloadWorldsFile(){
        if(worldsFile.exists()){
            worldsConfig = YamlConfiguration.loadConfiguration(worldsFile);
        } else setupWorldsFile();
    }

    public static void loadWorlds(){

        FileConfiguration config = WorldsManager.getWorldsConfig();
        ConfigurationSection worldsSection = config.getConfigurationSection("Worlds");

        if (worldsSection != null) {
            for (String worldName : worldsSection.getKeys(false)) {
                try {
                    ConfigurationSection worldSection = worldsSection.getConfigurationSection(worldName);

                    if (worldSection != null && "LOADED".equalsIgnoreCase(worldSection.getString("Status"))) {
                        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);

                        if (worldFolder.exists() && worldFolder.isDirectory()) {
                            instance.getLogger().info("Chargement automatique du monde : " + worldName);

                            World world = Bukkit.createWorld(new WorldCreator(worldName));
                            if (world != null) {
                                instance.getLogger().info("Le monde " + worldName + " a été chargé avec succès !");
                            } else {
                                instance.getLogger().warning("Impossible de charger le monde : " + worldName);
                            }
                        } else {
                            instance.getLogger().warning("Le dossier pour le monde " + worldName + " est introuvable.");
                        }
                    }
                } catch (Exception e) {
                    instance.getLogger().severe("Erreur lors du chargement du monde " + worldName + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    }

    public static String isWorldLoaded(World world){
        if(world.getWorldFolder().exists()){
            return "LOADED";
        } else {
            return"UNLOADED";
        }
    }

    public static FileConfiguration getWorldsConfig(){
        return worldsConfig;
    }

}
