package dragon.me.kyberPractice;

import com.sk89q.worldedit.WorldEdit;
import dragon.me.kyberPractice.commands.DuelCommand;
import dragon.me.kyberPractice.commands.KyberRootCommand;
import dragon.me.kyberPractice.commands.QueueCommand;
import dragon.me.kyberPractice.hooks.PlaceholderApiHook;
import dragon.me.kyberPractice.hooks.SqliteHook;
import dragon.me.kyberPractice.listener.*;
import dragon.me.kyberPractice.listener.optionals.VulcanPunishEventListener;
import dragon.me.kyberPractice.managers.ConfigMessageManager;
import dragon.me.kyberPractice.scheduler.QueueScheduler;
import dragon.me.kyberPractice.scheduler.RequestTimeoutScheduler;
import dragon.me.kyberPractice.storage.ArenaDataManager;
import dragon.me.kyberPractice.storage.InventoryManager;
import dragon.me.kyberPractice.storage.KitDataManager;
import dragon.me.kyberPractice.storage.QueueDataManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static org.fusesource.jansi.Ansi.Color.*;

public class KyberPractice extends JavaPlugin {

    public static Plugin instance;
    public static ArenaDataManager arenaDataManager;
    public static KitDataManager kitDataManager;
    public static boolean isWorldEditAvailable;
    public static WorldEdit worldEdit;
    public static MiniMessage miniMessage = MiniMessage.miniMessage();
    public static ConfigMessageManager messageSupplier;
    public static InventoryManager inventoryManager = new InventoryManager();
    public static QueueDataManager queueDataManager;


    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
        isWorldEditAvailable = getServer().getPluginManager().isPluginEnabled("WorldEdit") || getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit");

        //KyberPractice.instance.getLogger().info(CYAN +"[APIs]" + GREEN +"[ApiForeLoadEvent]" + WHITE + "Installing ANSI extension for console logging...");
        if (isWorldEditAvailable) {
            worldEdit = WorldEdit.getInstance();
            KyberPractice.getInstance().getLogger().info("[APIs] [ApiHookEvent] WorldEdit/FAWE is installed, enjoy the features!");
        }



        DuelCommand duelCommand = new DuelCommand();
        KyberRootCommand rootCommand = new KyberRootCommand();
        QueueCommand queueCommand = new QueueCommand();

        getCommand("duel").setExecutor(duelCommand);
        getCommand("duel").setTabCompleter(duelCommand);

        getCommand("kpractise").setExecutor(rootCommand);
        getCommand("kpractise").setTabCompleter(rootCommand);

        getCommand("queue").setExecutor(queueCommand);
        getCommand("queue").setTabCompleter(queueCommand);

        Bukkit.getLogger().info("Trying to connect to database...Hold on please!");
        SqliteHook.setupDatabase();

        messageSupplier = new ConfigMessageManager(this);

        RequestTimeoutScheduler.startScheduler();
        arenaDataManager = new ArenaDataManager();
        arenaDataManager.loadData();
        kitDataManager = new KitDataManager();
        arenaDataManager.getArenas().stream().forEach(arena ->
                KyberPractice.instance.getLogger().info("[DataSaver] Loaded arena '" + arena.getName() + "' from arena.yml"));
        queueDataManager =  new QueueDataManager();
        queueDataManager.loadData();
        registerListeners();
        autoDetectKits();
        PlaceholderApiHook.registerHook();
        QueueScheduler.startScheduler();
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
        getServer().getPluginManager().registerEvents(new PlayerCombatListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(),this);
        //Optional integrations [Vulcan]
        if (KyberPractice.instance.getServer().getPluginManager().isPluginEnabled("Vulcan")){
            getServer().getPluginManager().registerEvents(new VulcanPunishEventListener(),this);
            KyberPractice.instance.getLogger().info("[APIs] [ApiHookEvent] Vulcan is installed, enjoy the features!");
        }
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
