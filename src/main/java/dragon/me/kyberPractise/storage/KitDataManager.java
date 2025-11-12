package dragon.me.kyberPractise.storage;

import dragon.me.kyberPractise.KyberPractice;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KitDataManager {

    private final List<Kit> kits = new ArrayList<>();
    private final File file;
    private YamlConfiguration config;

    public KitDataManager() {
        file = new File(KyberPractice.instance.getDataFolder(), "kits.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            KyberPractice.instance.saveResource("kits.yml", false);
        }
        loadData();
    }

    @SuppressWarnings("unchecked")
    public void loadData() {
        kits.clear();
        config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection kitsSection = config.getConfigurationSection("kits");
        if (kitsSection == null) return;

        for (String key : kitsSection.getKeys(false)) {
            ConfigurationSection section = kitsSection.getConfigurationSection(key);
            if (section == null) continue;

            List<ItemStack> armorList = (List<ItemStack>) section.getList("armor");
            ItemStack[] armor = (armorList != null) ? armorList.toArray(new ItemStack[0]) : new ItemStack[0];

            List<ItemStack> inventoryList = (List<ItemStack>) section.getList("inventory");
            ItemStack[] inventory = (inventoryList != null) ? inventoryList.toArray(new ItemStack[0]) : new ItemStack[0];

            ItemStack offhand = section.getItemStack("offhand", new ItemStack(Material.AIR));

            kits.add(new Kit(key, armor, inventory, offhand));
        }
    }

    public void saveData(Kit kit) {
        String base = "kits." + kit.getName();

        config.set(base + ".armor", kit.getArmor());
        config.set(base + ".inventory", kit.getInventory());
        config.set(base + ".offhand", kit.getOffhand());

        saveAndReload();
    }

    private void saveAndReload() {
        try {
            config.save(file);
        } catch (IOException ex) {
            KyberPractice.instance.getLogger().severe("Failed to save kits.yml: " + ex.getMessage());
        }
        loadData();
    }

    public List<Kit> getKits() {
        return new ArrayList<>(kits);
    }

    public void addKit(Kit kit) {
        kits.add(kit);
    }

    public void removeKit(Kit kit) {
        kits.remove(kit);
    }

    public void clearKits() {
        kits.clear();
    }
}
