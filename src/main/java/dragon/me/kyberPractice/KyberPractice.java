package dragon.me.kyberPractice;

import com.sk89q.worldedit.WorldEdit;
import dragon.me.kyberPractice.commands.DuelCommand;
import dragon.me.kyberPractice.commands.KyberRootCommand;
import dragon.me.kyberPractice.hooks.SqliteHook;
import dragon.me.kyberPractice.listener.DuelEndListener;
import dragon.me.kyberPractice.listener.DuelStartListener;
import dragon.me.kyberPractice.listener.DuelWinListener;
import dragon.me.kyberPractice.listener.PlayerDeathListener;
import dragon.me.kyberPractice.scheduler.RequestTimeoutScheduler;
import dragon.me.kyberPractice.storage.ArenaDataManager;
import dragon.me.kyberPractice.storage.KitDataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class KyberPractice extends JavaPlugin {

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
            KyberPractice.getInstance().getLogger().info("[API HOOKS] WorldEdit/FAWE is installed, enjoy the features!");
        }



        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("kpractise").setExecutor(new KyberRootCommand());
        Bukkit.getLogger().info("Trying to connect to database...Hold on please!");
        SqliteHook.setupDatabase();

        RequestTimeoutScheduler.startScheduler();
        arenaDataManager = new ArenaDataManager();
        arenaDataManager.loadData();
        kitDataManager = new KitDataManager();
        arenaDataManager.getArenas().stream().forEach(arena ->
                KyberPractice.instance.getLogger().info("[DataSaver] Loaded arena '" + arena.getName() + "' from arena.yml"));

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
        getServer().getPluginManager().registerEvents(new DuelEndListener(), this);
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
            KyberPractice.instance.getLogger().info("Added kit " + kit.getName() + " to database! Skipped if it already in it!");
        });
    }
}
