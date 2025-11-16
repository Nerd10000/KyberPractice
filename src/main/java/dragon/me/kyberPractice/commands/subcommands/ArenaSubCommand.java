package dragon.me.kyberPractice.commands.subcommands;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.hooks.WorldEditHook;
import dragon.me.kyberPractice.storage.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;


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
        Arena arena = KyberPractice.arenaDataManager.getArena(name);

        if (arena == null) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.deleteData(arena);
        player.sendMessage("§bDuels §8» §bYou have deleted the arena named §3'" + name + "'.");
    }

    public static void setPos1(Player player, Location location, String name) {
        Arena arena = KyberPractice.arenaDataManager.getArena(name);

        if (arena == null) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.setPos1(arena, location);
        player.sendMessage("§bDuels §8» §bSet §31st position§b of '" + name + "' to §3" + asXYZ(location) + "§b.");
    }

    public static void setPos2(Player player, Location location, String name) {
        Arena arena = KyberPractice.arenaDataManager.getArena(name);

        if (arena == null) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.setPos2(arena, location);
        player.sendMessage("§bDuels §8» §bSet §32nd position§b of '" + name + "' to §3" + asXYZ(location) + "§b.");
    }

    public static void setSpawnPos1(Player player, Location location, String name) {
        Arena arena = KyberPractice.arenaDataManager.getArena(name);

        if (arena == null) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.setSpawnPos1(arena, location);
        player.sendMessage("§bDuels §8» §bSet §31st spawn§b of '" + name + "' to §3" + asXYZ(location) + "§b.");
    }

    public static void setSpawnPos2(Player player, Location location, String name) {
        Arena arena = KyberPractice.arenaDataManager.getArena(name);

        if (arena == null) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.setSpawnPos2(arena, location);
        player.sendMessage("§bDuels §8» §bSet §32nd spawn§b of '" + name + "' to §3" + asXYZ(location) + "§b.");
    }

    public static void setSpectatorPos(Player player, Location location, String name) {
        Arena arena = KyberPractice.arenaDataManager.getArena(name);

        if (arena == null) {
            player.sendMessage("§bDuels §8» §cThere is no such arena named §4'" + name + "'§c.");
            return;
        }

        KyberPractice.arenaDataManager.setSpectatorPos(arena, location);
        player.sendMessage("§bDuels §8» §bSet §3spectator position§b of '" + name + "' to §3" + asXYZ(location) + "§b.");
    }

    public static void teleport(Player player, String name) {
        Arena arena = KyberPractice.arenaDataManager.getArena(name);

        if (arena == null) {
            player.teleport(arena.getSpectatorPos());
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
        WorldEditHook.pasteSchematic(null, arena.getName(), () -> {
            player.sendMessage("§bDuels §8» §bRestored the arena '" + arena.getName() + "' in the world.");
        });
    }
}
