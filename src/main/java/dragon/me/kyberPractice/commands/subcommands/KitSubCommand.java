package dragon.me.kyberPractice.commands.subcommands;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.storage.Arena;
import dragon.me.kyberPractice.storage.Kit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public final class KitSubCommand {

    private KitSubCommand() {}

    public static void create(Player player, String name) {
        ItemStack[] contents = player.getInventory().getContents();
        ItemStack[] armorContents = player.getInventory().getArmorContents();
        ItemStack offhand = player.getInventory().getItemInOffHand();

        Kit kit = new Kit(name, armorContents, contents, offhand);
        System.out.println("Saving kit: " + name);
        KyberPractice.kitDataManager.saveData(kit);
    }

    public static void loadKit(Player player, String name) {
        Kit kit = KyberPractice.kitDataManager.getKit(name);

        if (kit != null) {
            player.getInventory().setArmorContents(kit.getArmor());
            player.getInventory().setContents(kit.getInventory());
            player.getInventory().setItem(EquipmentSlot.OFF_HAND, kit.getOffhand());
            player.sendMessage("Loaded kit: " + name);
        } else {
            player.sendMessage("Could not find kit: " + name);
        }
    }
}
