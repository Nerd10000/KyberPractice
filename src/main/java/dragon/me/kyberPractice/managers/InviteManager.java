package dragon.me.kyberPractice.managers;

import dragon.me.kyberPractice.managers.objects.Invite;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class InviteManager {

    private static final Map<UUID, Invite> invites = new HashMap<>();
    // Key = target player's UUID

    private InviteManager() {}

    public static boolean hasRequest(UUID target) {
        return invites.containsKey(target);
    }

    public static void addInvite(Invite invite) {
        invites.put(invite.getTarget(), invite);
    }

    public static void removeInvite(Invite invite) {
        invites.remove(invite.getTarget());
    }

    public static void timeoutRequest(UUID sender, UUID target) {
        invites.remove(target);
    }

    public static Invite getInviteByPlayer(UUID target) {
        return invites.get(target);
    }

    public static Map<UUID, Invite> getAllInvites() {
        return new HashMap<>(invites); // safe copy for iteration
    }

    public static void clearInvites() {
        invites.clear();
    }
}
