package dragon.me.kyberPractise.storage;

import org.bukkit.Location;

public class Arena {
    private final String name;
    private final Location pos1;
    private final Location pos2;
    private final String world;
    private final int id;
    private final boolean enabled;
    private final Location spawnPos1;
    private final Location spawnPos2;
    private final Location spectatorPos;

    public Arena(String name, Location pos1, Location pos2, String world, int id, boolean enabled, Location spawnPos1, Location spawnPos2, Location spectatorPos) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.world = world;
        this.id = id;
        this.enabled = enabled;
        this.spawnPos1 = spawnPos1;
        this.spawnPos2 = spawnPos2;
        this.spectatorPos = spectatorPos;
    }

    public String getName() {
        return name;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public String getWorld() {
        return world;
    }

    public int getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Location getSpawnPos1() {
        return spawnPos1;
    }

    public Location getSpawnPos2() {
        return spawnPos2;
    }

    public Location getSpectatorPos() {
        return spectatorPos;
    }
}
