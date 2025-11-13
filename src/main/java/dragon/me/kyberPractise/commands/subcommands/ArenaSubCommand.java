package dragon.me.kyberPractise.commands.subcommands;

import dragon.me.kyberPractise.KyberPractice;
import dragon.me.kyberPractise.hooks.WorldEditHook;
import dragon.me.kyberPractise.storage.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

public final class ArenaSubCommand {

    private ArenaSubCommand() {}

    // Helper function to format coordinates
    private static String asXYZ(Location location) {
        return "(" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")";
    }

    public static void create(Player player, String name) {
        Arena arena = new Arena(
                name,
                player.getLocation(),
                player.getLocation(),
                player.getWorld().getName(),
                0,
                true,
                player.getLocation(),
                player.getLocation(),
                player.getLocation()
        );
        KyberPractice.arenaDataManager.saveData(arena);
        player.sendMessage("§bDuels §8» §bYou have created an arena named §3'" + name + "'.");
    }

    public static void delete(Player player, String name) {
        Optional<Arena> arenaOptional = KyberPractice.arenaDataManager.getArenas().stream()
                .filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst();

        if (!arenaOptional.isPresent()) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.deleteData(arenaOptional.get());
        player.sendMessage("§bDuels §8» §bYou have deleted the arena named §3'" + name + "'.");
    }

    public static void setPos1(Player player, Location location, String name) {
        Optional<Arena> arenaOptional = KyberPractice.arenaDataManager.getArenas().stream()
                .filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst();

        if (!arenaOptional.isPresent()) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.setPos1(arenaOptional.get(), location);
        player.sendMessage("§bDuels §8» §bSet §31st position§b of '" + name + "' to §3" + asXYZ(location) + "§b.");
    }

    public static void setPos2(Player player, Location location, String name) {
        Optional<Arena> arenaOptional = KyberPractice.arenaDataManager.getArenas().stream()
                .filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst();

        if (!arenaOptional.isPresent()) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.setPos2(arenaOptional.get(), location);
        player.sendMessage("§bDuels §8» §bSet §32nd position§b of '" + name + "' to §3" + asXYZ(location) + "§b.");
    }

    public static void setSpawnPos1(Player player, Location location, String name) {
        Optional<Arena> arenaOptional = KyberPractice.arenaDataManager.getArenas().stream()
                .filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst();

        if (!arenaOptional.isPresent()) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.setSpawnPos1(arenaOptional.get(), location);
        player.sendMessage("§bDuels §8» §bSet §31st spawn§b of '" + name + "' to §3" + asXYZ(location) + "§b.");
    }

    public static void setSpawnPos2(Player player, Location location, String name) {
        Optional<Arena> arenaOptional = KyberPractice.arenaDataManager.getArenas().stream()
                .filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst();

        if (!arenaOptional.isPresent()) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.setSpawnPos2(arenaOptional.get(), location);
        player.sendMessage("§bDuels §8» §bSet §32nd spawn§b of '" + name + "' to §3" + asXYZ(location) + "§b.");
    }

    public static void setSpectatorPos(Player player, Location location, String name) {
        Optional<Arena> arenaOptional = KyberPractice.arenaDataManager.getArenas().stream()
                .filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst();

        if (!arenaOptional.isPresent()) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.setSpectatorPos(arenaOptional.get(), location);
        player.sendMessage("§bDuels §8» §bSet §3spectator position§b of '" + name + "' to §3" + asXYZ(location) + "§b.");
    }

    public static void teleport(Player player, String name) {
        Optional<Arena> arenaOptional = KyberPractice.arenaDataManager.getArenas().stream()
                .filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst();

        if (arenaOptional.isPresent()) {
            player.teleport(arenaOptional.get().getSpectatorPos());
        } else {
            player.teleport(player.getLocation().clone().add(0, 1, 0));
        }
    }
    public static void saveSchematic(Arena arena, Player player){
        WorldEditHook.saveSchematic(
                Bukkit.getWorld(arena.getWorld()),
                arena.getPos1(),
                arena.getPos2(),
                arena.getName()
        );
        KyberPractice.instance.getLogger().info("The arena '" + arena.getName() + "' has been saved in the world");
        player.sendMessage("§bDuels §8» §bThe arena '" + arena.getName() + "' has been saved as a schematic now it will be restored in the world if possible.");
    }
    public static  void restoreArena(Arena arena, Player player){
        WorldEditHook.pasteSchematic(null, arena.getName());
        player.sendMessage("§bDuels §8» §bRestored the arena '" + arena.getName() + "' in the world.");
    }
}
