package fr.syfalogryl.mcbasics.global.managers;

import fr.syfalogryl.mcbasics.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportsManager {

    private static File reportsFile;
    private static FileConfiguration reportsConfig;

    public static void setupReportsFile(){
        reportsFile = new File(Main.getInstance().getDataFolder(), "worlds.yml");
        if(!reportsFile.exists()){
            reportsFile.getParentFile().mkdirs();
            Main.getInstance().saveResource("worlds.yml", false);
        }
        reportsConfig = YamlConfiguration.loadConfiguration(reportsFile);
    }
    public static void saveReportsFile(){
        try{
            reportsConfig.save(reportsFile);
        } catch (IOException e){
            Main.getInstance().getLogger().severe("Impossible de sauvegarder le fichier permissions.yml.");
            e.printStackTrace();
        }
    }
    public static FileConfiguration getReportsConfig(){
        return reportsConfig;
    }

    public static void addReport(String reportTarget, String reportSender, String reason){

        String path = "reports." + reportTarget;

        int totalReports = reportsConfig.getInt(path + ".total", 0) + 1;
        reportsConfig.set(path + ".total", totalReports);

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        reportsConfig.createSection(path + ".details." + totalReports);
        reportsConfig.set(path + ".details." + totalReports + ".reported_by", reportSender);
        reportsConfig.set(path + ".details." + totalReports + ".time", time);
        reportsConfig.set(path + ".details." + totalReports + ".reason", reason);

        saveReportsFile();

    }

}
