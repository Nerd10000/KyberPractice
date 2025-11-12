package dragon.me.kyberPractise.storage;

import dragon.me.kyberPractise.KyberPractice;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArenaDataManager {

    private final File file;
    private final FileConfiguration config;

    private static final List<Arena> arenas = new ArrayList<>();
    private static final List<String> availableArenas = new ArrayList<>();

    public ArenaDataManager() {
        file = new File(KyberPractice.instance.getDataFolder(), "arenas.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            KyberPractice.instance.saveResource("arenas.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void loadData() {
        arenas.clear();

        ConfigurationSection arenasSection = config.getConfigurationSection("arenas");
        if (arenasSection == null) return;

        for (String key : arenasSection.getKeys(false)) {
            ConfigurationSection section = arenasSection.getConfigurationSection(key);
            if (section == null) continue;

            Arena arena = new Arena(
                    section.getString("name"),
                    section.getLocation("pos1"),
                    section.getLocation("pos2"),
                    section.getString("world"),
                    section.getInt("id"),
                    section.getBoolean("enabled"),
                    section.getLocation("spawnPos1"),
                    section.getLocation("spawnPos2"),
                    section.getLocation("spectatorPos")
            );
            arenas.add(arena);
        }
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public void saveData(Arena arena) {
        String base = "arenas." + arena.getName();
        config.set(base + ".name", arena.getName());
        config.set(base + ".pos1", arena.getPos1());
        config.set(base + ".pos2", arena.getPos2());
        config.set(base + ".world", arena.getWorld());
        config.set(base + ".id", arena.getId());
        config.set(base + ".enabled", arena.isEnabled());
        config.set(base + ".spawnPos1", arena.getSpawnPos1());
        config.set(base + ".spawnPos2", arena.getSpawnPos2());
        config.set(base + ".spectatorPos", arena.getSpectatorPos());

        saveAndReload();
    }

    public void deleteData(Arena arena) {
        config.set("arenas." + arena.getName(), null);
        saveAndReload();
    }

    public void setPos1(Arena arena, Location pos1) {
        config.set("arenas." + arena.getName() + ".pos1", pos1);
        saveAndReload();
    }

    public void setPos2(Arena arena, Location pos2) {
        config.set("arenas." + arena.getName() + ".pos2", pos2);
        saveAndReload();
    }

    public void setSpawnPos1(Arena arena, Location spawnPos1) {
        config.set("arenas." + arena.getName() + ".spawnPos1", spawnPos1);
        saveAndReload();
    }

    public void setSpawnPos2(Arena arena, Location spawnPos2) {
        config.set("arenas." + arena.getName() + ".spawnPos2", spawnPos2);
        saveAndReload();
    }

    public void setSpectatorPos(Arena arena, Location spectatorPos) {
        config.set("arenas." + arena.getName() + ".spectatorPos", spectatorPos);
        saveAndReload();
    }

    private void saveAndReload() {
        try {
            config.save(file);
        } catch (IOException ex) {
            KyberPractice.instance.getLogger().severe("Failed to save arenas.yml: " + ex.getMessage());
        }
        YamlConfiguration loaded = YamlConfiguration.loadConfiguration(file);
        ((YamlConfiguration) config).setDefaults(loaded);
        loadData();
    }
}
