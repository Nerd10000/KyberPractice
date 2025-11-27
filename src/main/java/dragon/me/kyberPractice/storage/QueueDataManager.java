package dragon.me.kyberPractice.storage;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.managers.objects.Queue;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueueDataManager {

    private final File file;
    private final FileConfiguration config;

    private static final List<Queue> queues = new ArrayList<>();


    public QueueDataManager() {
        file = new File(KyberPractice.instance.getDataFolder(), "queues.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            KyberPractice.instance.saveResource("queues.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }
    public void saveData(Queue queue) {
        String base = "queues." + queue.getName();
        config.set(base + ".name", queue.getName());
        config.set(base + ".kit", queue.getKit().getName());


        saveAndReload();
    }
    private void saveAndReload() {
        try {
            config.save(file);
        } catch (IOException ex) {
            KyberPractice.instance.getLogger().severe("Failed to save queues.yml: " + ex.getMessage());
        }
        YamlConfiguration loaded = YamlConfiguration.loadConfiguration(file);
        ((YamlConfiguration) config).setDefaults(loaded);
        loadData();
    }

    public void loadData() {
        KyberPractice.instance.getLogger().info("[DEBUG] QueueDataManager.loadData() called - reloading queues...");
        queues.clear();

        ConfigurationSection arenasSection = config.getConfigurationSection("queues");
        if (arenasSection == null) {
            KyberPractice.instance.getLogger().info("[DEBUG] QueueDataManager.loadData() found no 'queues' section in config.");
            return;
        }

        for (String key : arenasSection.getKeys(false)) {
            ConfigurationSection section = arenasSection.getConfigurationSection(key);
            if (section == null) continue;

            Queue queue = new Queue(
                    section.getString("name"),
                    KyberPractice.kitDataManager.getKit(section.getString("kit")));
            queues.add(queue);
        }
        KyberPractice.instance.getLogger().info("[DEBUG] QueueDataManager.loadData() completed - loaded " + queues.size() + " queues.");
    }

    public Queue getQueue(String name) {
        for (Queue queue : queues) {
            if (queue.getName().equalsIgnoreCase(name)) {
                return queue;
            }
        }
        return null;
    }
    public List<Queue> getQueues() {
        return new ArrayList<>(queues);
    }
    public void addQueue(Queue queue) {
        queues.add(queue);
    }
    public void removeQueue(Queue queue) {
        queues.remove(queue);
    }


}
