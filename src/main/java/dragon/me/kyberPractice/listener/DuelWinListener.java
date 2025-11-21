package dragon.me.kyberPractice.listener;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.events.DuelWinEvent;
import dragon.me.kyberPractice.hooks.SqliteHook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DuelWinListener implements Listener {

    @EventHandler
    public void onDuelWin(DuelWinEvent event) {
        SqliteHook.incrementWins(event.getPlayer().getUniqueId().toString(), event.getKit());
        event.getPlayer().showTitle(Title.title(KyberPractice.messageSupplier.getStringAndSerializeIfPossible("win.title", event.getPlayer()), Component.text("")));

        event.getPlayer().setInvulnerable(true);

        Bukkit.getScheduler().runTaskLater(KyberPractice.instance, () -> {
                    event.getPlayer().setInvulnerable(false);
        },5* 20);
        event.getPlayer().getInventory().clear();
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_GOAT_HORN_SOUND_1, 1f, 1f);

    }
}
