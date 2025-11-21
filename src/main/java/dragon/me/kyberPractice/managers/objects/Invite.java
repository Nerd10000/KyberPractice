package dragon.me.kyberPractice.managers.objects;

import java.util.UUID;

public class Invite {
    private final UUID challenger;
    private final UUID target;
    private final long timestamp;
    private final String kit;

    public Invite(UUID player, UUID target, long timestamp, String kit) {
        this.challenger = player;
        this.target = target;
        this.timestamp = timestamp;
        this.kit = kit;
    }

    public UUID getChallenger() {
        return challenger;
    }

    public UUID getTarget() {
        return target;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getKit() {
        return kit;
    }
}
