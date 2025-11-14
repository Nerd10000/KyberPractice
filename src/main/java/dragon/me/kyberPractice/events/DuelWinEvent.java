package dragon.me.kyberPractice.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DuelWinEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private final String kit;

    public DuelWinEvent(Player player, String kit) {
        this.player = player;
        this.kit = kit;
    }

    public Player getPlayer() {
        return player;
    }

    public String getKit() {
        return kit;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
