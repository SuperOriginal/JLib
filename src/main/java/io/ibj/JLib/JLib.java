package io.ibj.JLib;

import com.google.gson.Gson;
import io.ibj.JLib.cmd.CmdWrapper;
import io.ibj.JLib.cmd.ICmd;
import io.ibj.JLib.cmd.RootCmdWrapper;
import io.ibj.JLib.db.DatabaseManager;
import io.ibj.JLib.file.YAMLFile;
import io.ibj.JLib.gui.GuiListener;
import io.ibj.JLib.logging.DefaultRavenFactory;
import io.ibj.JLib.logging.Raven;
import io.ibj.JLib.logging.RavenFactory;
import io.ibj.JLib.safe.PlayerMapClearer;
import io.ibj.JLib.tp.EssentialsTPProvider;
import io.ibj.JLib.tp.TPProvider;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * io.ibj.MattLib
 * Created by Joe
 * Project: MattLib
 */
public class JLib extends JPlug {


    public CommandMap getCmdMap() {
        return cmdMap;
    }

    private CommandMap cmdMap = null;

    @Getter
    private Raven raven;

    @Getter
    private PlayerMapClearer clearer;

    @Getter
    private PlayerLookup playerLookup = new PlayerLookup();

    private YAMLFile configFile;

    private DatabaseManager dbManager = new DatabaseManager();

    public DatabaseManager getDatabaseManager(){
        return dbManager;
    }

    @Getter
    private Gson gson = new Gson();

    public void onModuleEnable(){

        i = this;

        isDebug = Bukkit.getPluginManager().isPluginEnabled("DebugServer");

        configFile = new YAMLFile(this,"config.yml");

        RavenFactory.registerFactory(new DefaultRavenFactory());
        String rawDsn = configFile.getConfig().getString("sentryRawDsn");
        raven = RavenFactory.ravenInstance(rawDsn);
        try {
            Field cmdMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            cmdMapField.setAccessible(true);
            cmdMap = (CommandMap) cmdMapField.get(Bukkit.getServer());
        } catch (NoSuchFieldException e) {
            handleError(e);
        } catch (IllegalAccessException e) {
            handleError(e);
        }

        registerEvents(new GuiListener());

        runNow(new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
                    System.out.println("Loaded Essentials TP Hook");
                    setTpProvider(new EssentialsTPProvider(Bukkit.getPluginManager().getPlugin("Essentials")));
                }
            }
        }, ThreadLevel.SYNC);

        clearer = new PlayerMapClearer();

        registerEvents(clearer);
    }

    public void onModuleDisable(){
        dbManager.cleanUpAll();
    }

    public void register(Command c){
        if(c instanceof RootCmdWrapper){
            if(((RootCmdWrapper) c).getWrapper().isAggresive()){
                Command cmd = cmdMap.getCommand(c.getName());
                if(cmd != null){
                    cmd.unregister(cmdMap);
                }
            }
        }
        cmdMap.register(c.getName(),c);
    }

    public void register(Class<? extends ICmd> c){
        register(new RootCmdWrapper(CmdWrapper.wrap(c)));
    }

    public void register(Class<? extends ICmd> c, JPlug host){ register(new RootCmdWrapper(CmdWrapper.wrap(c),host));}

    private static JLib i;

    public static JLib getI(){
        return i;
    }

    @Getter
    private boolean isDebug;

    private TPProvider tpProvider = null;

    public void setTpProvider(TPProvider provider){
        this.tpProvider = provider;
    }

    public void tp(Player p, Location l){
        if(tpProvider != null){
            tpProvider.tp(p,l);
        }
        else
        {
            p.teleport(l);
        }
    }
}
