package dragon.me.kyberPractice.managers;

import dragon.me.kyberPractice.managers.objects.Party;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PartyManager {

    private static final Map<String, Party> partyMap = new HashMap<>();

    public static Party createParty(String name, UUID creator) {
        if (name == null) return null;
        return partyMap.computeIfAbsent(name, k -> new Party(name, creator));
    }

    public static void addParty(String name, UUID creator) {
        createParty(name, creator);
    }

    public static void removeParty(String name) {
        if (name == null) return;
        partyMap.remove(name);
    }

    public static Party getParty(String name) {
        if (name == null) return null;
        return partyMap.get(name);
    }

    public static List<UUID> getPlayers(String partyName) {
        Party party = getParty(partyName);
        return party == null ? new ArrayList<>() : party.getMembers();
    }

    public static void addToParty(String partyName, UUID... uuids) {
        if (partyName == null || uuids == null || uuids.length == 0) return;

        Party party = partyMap.computeIfAbsent(partyName, k -> new Party(partyName));

        for (UUID uuid : uuids) {
            if (uuid == null) continue;
            party.addMember(uuid);
        }
    }

    public static void addPendingToParty(String partyName, UUID uuid) {
        if (partyName == null || uuid == null) return;
        Party party = partyMap.computeIfAbsent(partyName, k -> new Party(partyName));
        party.addPendingMember(uuid);
    }

    public static void acceptPendingMember(String partyName, UUID uuid) {
        Party party = getParty(partyName);
        if (party == null || uuid == null) return;
        party.movePendingToMembers(uuid);
    }

}