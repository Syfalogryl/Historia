package fr.syfalogryl.mcbasics.global.managers;

import fr.syfalogryl.mcbasics.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PermsManager {

    private static FileConfiguration permissionsConfig;
    private static File permissionsFile;

    public static void setupPermissionsFile(){
        permissionsFile = new File(Main.getInstance().getDataFolder(), "permissions.yml");
        if(!permissionsFile.exists()){
            permissionsFile.getParentFile().mkdirs();
            Main.getInstance().saveResource("permissions.yml", false);
        }
        permissionsConfig = YamlConfiguration.loadConfiguration(permissionsFile);
    }
    public static void savePermissionsFile(){
        try{
            permissionsConfig.save(permissionsFile);
        } catch (IOException e){
            Main.getInstance().getLogger().severe("Impossible de sauvegarder le fichier permissions.yml.");
            e.printStackTrace();
        }

    }
    public static void reloadPermissionsFile(){
        if(permissionsFile.exists()){
            permissionsConfig = YamlConfiguration.loadConfiguration(permissionsFile);
        } else setupPermissionsFile();
    }
    public static FileConfiguration getPermissionsConfig() {
        return permissionsConfig;
    }
    public static boolean hasPermission(Player p, String permission){
        List<String> perms = getPermissionsConfig().getStringList(p.getName() + ".Permissions");

        return perms.contains(permission) || perms.contains("mcbasics.*");
    }

}
