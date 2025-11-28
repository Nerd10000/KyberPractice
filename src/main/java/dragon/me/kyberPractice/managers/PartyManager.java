package dragon.me.kyberPractice.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class PartyManager {

    public static final HashMap<String, List<UUID>> partyMap = new HashMap<>();

    public static void addParty(String name, UUID creator) {
        partyMap.put(name, new ArrayList<UUID>());
        if (creator != null) {
            partyMap.get(name).add(creator);
        }
    }

    public static void removeParty(String name) {
        partyMap.remove(name);
    }

    public static List<UUID> getPlayers(String partyName) {
        return partyMap.get(partyName);
    }

    public static void addToParty(String partyName, UUID... uuids) {
        if (partyName == null || uuids == null || uuids.length == 0) return;

        partyMap.computeIfAbsent(partyName, k -> new ArrayList<UUID>());
        List<UUID> players = partyMap.get(partyName);

        for (UUID uuid : uuids) {
            if (uuid == null) continue;
            if (!players.contains(uuid)) {
                players.add(uuid);
            }
        }
    }

}