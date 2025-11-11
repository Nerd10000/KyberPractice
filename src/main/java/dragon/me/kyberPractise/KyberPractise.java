package dragon.me.kyberPractise;

import com.sk89q.worldedit.WorldEdit;
import dragon.me.kyberPractise.commands.DuelCommand;
import dragon.me.kyberPractise.commands.KyberRootCommand;
import dragon.me.kyberPractise.hooks.SqliteHook;
import dragon.me.kyberPractise.listener.DuelStartListener;
import dragon.me.kyberPractise.listener.DuelWinListener;
import dragon.me.kyberPractise.listener.PlayerDeathListener;
import dragon.me.kyberPractise.scheduler.RequestTimeoutScheduler;
import dragon.me.kyberPractise.storage.ArenaDataManager;
import dragon.me.kyberPractise.storage.KitDataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class KyberPractise extends JavaPlugin {

    public static Plugin instance;
    public static ArenaDataManager arenaDataManager;
    public static KitDataManager kitDataManager;
    public static boolean isWorldEditAvailable;
    public static WorldEdit worldEdit;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        isWorldEditAvailable = getServer().getPluginManager().isPluginEnabled("WorldEdit") || getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit");

        if (isWorldEditAvailable) {
            worldEdit = WorldEdit.getInstance();
        }

        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("kpractise").setExecutor(new KyberRootCommand());
        Bukkit.getLogger().info("Trying to connect to database...Hold on please!");
        SqliteHook.setupDatabase();

        RequestTimeoutScheduler.startScheduler();
        arenaDataManager = new ArenaDataManager();
        arenaDataManager.loadData();
        kitDataManager = new KitDataManager();

        registerListeners();
        autoDetectKits();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getInstance() {
        return instance;
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new DuelStartListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new DuelWinListener(), this);
    }

    public static ArenaDataManager getArenaDataManager() {
        return arenaDataManager;
    }

    /*
     * Detects the kits that are available
     * TODO Implement auto detect on reload
     */
    public void autoDetectKits() {
        kitDataManager.getKits().forEach(kit -> {
            SqliteHook.addKit(kit.getName());
            KyberPractise.instance.getLogger().info("Added kit " + kit.getName() + " to database! Skipped if it already in it!");
        });
    }
}
