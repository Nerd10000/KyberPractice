package dragon.me.kyberPractise.commands.subcommands;

import dragon.me.kyberPractise.KyberPractise;
import dragon.me.kyberPractise.storage.Kit;
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
        KyberPractise.kitDataManager.saveData(kit);
    }

    public static void loadKit(Player player, String name) {
        Optional<Kit> kitOptional = KyberPractise.kitDataManager.getKits().stream()
                .filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst();

        if (kitOptional.isPresent()) {
            Kit kit = kitOptional.get();
            player.getInventory().setArmorContents(kit.getArmor());
            player.getInventory().setContents(kit.getInventory());
            player.getInventory().setItem(EquipmentSlot.OFF_HAND, kit.getOffhand());
            player.sendMessage("Loaded kit: " + name);
        } else {
            player.sendMessage("Could not find kit: " + name);
        }
    }
}
