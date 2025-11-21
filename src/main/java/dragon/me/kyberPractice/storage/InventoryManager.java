package dragon.me.kyberPractice.storage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryManager {

    private final Map<UUID, SavedInventory> inventories = new HashMap<>();

    public InventoryManager() {

    }

    public void addInventory(UUID playerId, Inventory inventory) {
        if (!(inventory instanceof PlayerInventory)) return;
        PlayerInventory inv = (PlayerInventory) inventory;
        ItemStack[] contents = cloneItems(inv.getContents());
        ItemStack[] armor = cloneItems(inv.getArmorContents());
        ItemStack offhand = inv.getItemInOffHand() != null ? inv.getItemInOffHand().clone() : null;
        inventories.put(playerId, new SavedInventory(contents, armor, offhand));
    }

    public void removeInventory(UUID player) {
        inventories.remove(player);
    }

    public SavedInventory getInventory(UUID player) {
        return inventories.get(player);
    }

    public void restorePlayerInventory(UUID playerId) {
        SavedInventory saved = inventories.get(playerId);
        if (saved == null) return;
        Player p = Bukkit.getPlayer(playerId);
        if (p == null) return;
        PlayerInventory inv = p.getInventory();
        inv.setContents(cloneItems(saved.contents));
        inv.setArmorContents(cloneItems(saved.armor));
        inv.setItemInOffHand(saved.offhand != null ? saved.offhand.clone() : null);
        inventories.remove(playerId);
    }

    private static ItemStack[] cloneItems(ItemStack[] src) {
        if (src == null) return null;
        ItemStack[] out = new ItemStack[src.length];
        for (int i = 0; i < src.length; i++) {
            out[i] = src[i] != null ? src[i].clone() : null;
        }
        return out;
    }

    public static class SavedInventory {
        private final ItemStack[] contents;
        private final ItemStack[] armor;
        private final ItemStack offhand;

        public SavedInventory(ItemStack[] contents, ItemStack[] armor, ItemStack offhand) {
            this.contents = contents;
            this.armor = armor;
            this.offhand = offhand;
        }

        public ItemStack[] getContents() { return contents; }
        public ItemStack[] getArmor() { return armor; }
        public ItemStack getOffhand() { return offhand; }
    }
}
