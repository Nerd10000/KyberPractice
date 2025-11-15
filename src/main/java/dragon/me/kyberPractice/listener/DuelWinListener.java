package dragon.me.kyberPractice.listener;

import dragon.me.kyberPractice.events.DuelWinEvent;
import dragon.me.kyberPractice.hooks.SqliteHook;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DuelWinListener implements Listener {

    @EventHandler
    public void onDuelWin(DuelWinEvent event) {
        SqliteHook.incrementWins(event.getPlayer().getUniqueId().toString(), event.getKit());
        event.getPlayer().sendTitle("§6§lYOU'VE WON", "", 0, 100, 0);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_GOAT_HORN_SOUND_1, 1f, 1f);

    }
}
