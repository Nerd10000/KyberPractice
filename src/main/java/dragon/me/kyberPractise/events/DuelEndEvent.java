package dragon.me.kyberPractise.events;

import dragon.me.kyberPractise.managers.objects.Session;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DuelEndEvent extends Event {
    private  static final HandlerList handlerList = new HandlerList();
    private Player player1, player2;
    private String winnerName;
    private Session session;

    public DuelEndEvent(@NotNull  Player player1, @NotNull  Player player2, @NotNull  String winnerName, @NotNull  Session session){
        this.player1 = player1;
        this.player2 = player2;
        this.winnerName = winnerName;
        this.session = session;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
    public static @NotNull HandlerList getHandlerList() {
        return handlerList;
    }
}
