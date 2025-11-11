package dragon.me.kyberPractise.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DuelStartEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private final Player player1;
    private final Player player2;
    private final String kit;
    private final String map;

    public DuelStartEvent(Player player1, Player player2, String kit, String map) {
        this.player1 = player1;
        this.player2 = player2;
        this.kit = kit;
        this.map = map;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String getKit() {
        return kit;
    }

    public String getMap() {
        return map;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
