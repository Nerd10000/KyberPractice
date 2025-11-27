package dragon.me.kyberPractice.managers.objects;

import dragon.me.kyberPractice.storage.Kit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Queue {
    private final String name;
    private final Kit kit;
    private final List<UUID> players = new ArrayList<>();
    public Queue(String name, Kit kit) {
        this.name = name;
        this.kit = kit;

    }

    public String getName() {
        return name;
    }

    public Kit getKit() {
        return kit;
    }
    public List<UUID> getPlayers() {
        return players;
    }
    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
    }
    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }
    public boolean containsPlayer(Player player) {
        return players.contains(player.getUniqueId());
    }
    public boolean isEmpty() {
        return players.isEmpty();
    }


}
