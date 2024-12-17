package fr.syfalogryl.mcbasics.global.register;

import fr.syfalogryl.mcbasics.Main;
import fr.syfalogryl.mcbasics.global.commands.*;
import fr.syfalogryl.mcbasics.global.events.ChatStatus;
import fr.syfalogryl.mcbasics.global.events.IsolateWorlds;
import fr.syfalogryl.mcbasics.global.events.PlayerJoin;
import fr.syfalogryl.mcbasics.global.events.RespawnInWorlds;
import fr.syfalogryl.mcbasics.pvpenchants.PvpEnchants;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Register {

    static PluginManager pm = Bukkit.getPluginManager();
    static Main instance = Main.getInstance();

    public static void init(){

        PvpEnchants.init();

        // EVENTS
        pm.registerEvents(new PlayerJoin(), instance);
        pm.registerEvents(new IsolateWorlds(), instance);
        pm.registerEvents(new ChatStatus(), instance);
        pm.registerEvents(new RespawnInWorlds(), instance);

        // COMMANDS
        instance.getCommand("permissions").setExecutor(new CmdPermissions());
        instance.getCommand("perms").setExecutor(new CmdPermissions());
        instance.getCommand("rank").setExecutor(new CmdRanks());
        instance.getCommand("grade").setExecutor(new CmdRanks());

        instance.getCommand("gm").setExecutor(new CmdGamemodes());
        instance.getCommand("gmc").setExecutor(new CmdGamemodes());
        instance.getCommand("gma").setExecutor(new CmdGamemodes());
        instance.getCommand("gms").setExecutor(new CmdGamemodes());
        instance.getCommand("gmsp").setExecutor(new CmdGamemodes());
        instance.getCommand("gamemode").setExecutor(new CmdGamemodes());

        instance.getCommand("chat").setExecutor(new CmdChat());
        instance.getCommand("cc").setExecutor(new CmdChat());

        instance.getCommand("world").setExecutor(new CmdWorlds());
        instance.getCommand("worlds").setExecutor(new CmdWorlds());
        instance.getCommand("setspawn").setExecutor(new CmdWorlds());
        instance.getCommand("setworldspawn").setExecutor(new CmdWorlds());
        instance.getCommand("server").setExecutor(new CmdServer());

        instance.getCommand("day").setExecutor(new CmdWeather());
        instance.getCommand("night").setExecutor(new CmdWeather());
        instance.getCommand("sun").setExecutor(new CmdWeather());
        instance.getCommand("rain").setExecutor(new CmdWeather());
        instance.getCommand("thunder").setExecutor(new CmdWeather());
        instance.getCommand("time").setExecutor(new CmdWeather());
        instance.getCommand("weather").setExecutor(new CmdWeather());

        instance.getCommand("ping").setExecutor(new CmdPing());
        instance.getCommand("ms").setExecutor(new CmdPing());

        instance.getCommand("msg").setExecutor(new CmdMsg());
        instance.getCommand("message").setExecutor(new CmdMsg());
        instance.getCommand("r").setExecutor(new CmdMsg());
        instance.getCommand("reply").setExecutor(new CmdMsg());

        instance.getCommand("report").setExecutor(new CmdReport());
    }

}
