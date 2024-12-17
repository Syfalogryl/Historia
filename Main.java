package fr.syfalogryl.mcbasics;

import fr.syfalogryl.mcbasics.global.managers.ReportsManager;
import fr.syfalogryl.mcbasics.global.managers.WorldsManager;
import fr.syfalogryl.mcbasics.global.register.Register;
import fr.syfalogryl.mcbasics.global.managers.PermsManager;
import fr.syfalogryl.mcbasics.global.utils.ChatManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        getLogger().info("MCBasics s'active.");
        instance = this;

        PermsManager.setupPermissionsFile();
        WorldsManager.setupWorldsFile();
        WorldsManager.loadWorlds();
        ReportsManager.setupReportsFile();

        ChatManager.initializeWorlds();
        Register.init();
    }

    @Override
    public void onDisable() {
        getLogger().info("MCBasics se desactive.");
    }

    public static Main getInstance() {
        return instance;
    }

    // MESSAGES ENREGISTRES
    // --------------------
    public String noPerm(){
        return "§cVous n'avez pas la permission d'éxecuter cette commande.";
    }
    public String playerCmd(){
        return "Seuls les joueurs peuvent utiliser cette commande.";
    }

}
