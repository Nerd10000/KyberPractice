package dragon.me.kyberPractice.storage;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class Kit {
    private final String name;
    private final ItemStack[] armor;
    private final ItemStack[] inventory;
    private final ItemStack offhand;

    public Kit(String name, ItemStack[] armor, ItemStack[] inventory, ItemStack offhand) {
        this.name = name;
        this.armor = armor;
        this.inventory = inventory;
        this.offhand = offhand;
    }

    public String getName() {
        return name;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public ItemStack getOffhand() {
        return offhand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kit kit = (Kit) o;
        return Objects.equals(name, kit.name) &&
                Arrays.equals(armor, kit.armor) &&
                Arrays.equals(inventory, kit.inventory) &&
                Objects.equals(offhand, kit.offhand);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, offhand);
        result = 31 * result + Arrays.hashCode(armor);
        result = 31 * result + Arrays.hashCode(inventory);
        return result;
    }

    @Override
    public String toString() {
        return "Kit{"
                + "name='" + name + '\'' +
                ", armor=" + Arrays.toString(armor) +
                ", inventory=" + Arrays.toString(inventory) +
                ", offhand=" + offhand +
                '}';
    }
}
