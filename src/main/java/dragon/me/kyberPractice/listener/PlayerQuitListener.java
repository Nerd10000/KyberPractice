package dragon.me.kyberPractice.listener;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.events.DuelEndEvent;
import dragon.me.kyberPractice.managers.GameSessionManager;
import dragon.me.kyberPractice.managers.objects.Session;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.text.Component;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        Player quitting = event.getPlayer();
        Session session = GameSessionManager.getGameSession(quitting);

        // If the quitting player isn't in a session, nothing to do.
        if (session == null) {
            return;
        }

        Player player1 = Bukkit.getPlayer(session.getRequester());
        Player player2 = Bukkit.getPlayer(session.getTarget());
        Location lobby = KyberPractice.instance.getConfig().getLocation("lobby-location");

        // Determine opponent (the other participant who didn't quit)
        Player opponent;
        if (player1 != null && player1.getUniqueId().equals(quitting.getUniqueId())) {
            opponent = player2;
        } else if (player2 != null && player2.getUniqueId().equals(quitting.getUniqueId())) {
            opponent = player1;
        } else {
            // Fallback: pick the non-null player as opponent
            opponent = player1 != null ? player1 : player2;
        }

        // Notify and clean up opponent if they are still online
        if (opponent != null) {
            opponent.showTitle(Title.title(
                    KyberPractice.messageSupplier.getStringAndSerializeIfPossible("quit.cancel.title", opponent),
                    Component.text("")
            ));
            for (String line : KyberPractice.messageSupplier.getRawStringList("quit.cancel.message")) {
                opponent.sendMessage(KyberPractice.messageSupplier.serializeString(line, opponent));
            }

            if (lobby != null) {
                opponent.teleportAsync(lobby);
            }
            opponent.getInventory().clear();
        }

        // Clean up quitting player
        if (lobby != null) {
            quitting.teleportAsync(lobby);
        }
        quitting.getInventory().clear();

        // Fire DuelEndEvent if we have a valid opponent to comply with @NotNull in the event
        if (opponent != null) {
            Bukkit.getPluginManager().callEvent(new DuelEndEvent(quitting, opponent, opponent.getName(), session));
        }

        // Finally, remove the session once
        GameSessionManager.removeGameSession(session);
    }
}
