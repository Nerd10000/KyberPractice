package dragon.me.kyberPractise.listener;

import dragon.me.kyberPractise.KyberPractice;
import dragon.me.kyberPractise.events.DuelEndEvent;
import dragon.me.kyberPractise.events.DuelWinEvent;
import dragon.me.kyberPractise.managers.GameSessionManager;
import dragon.me.kyberPractise.managers.objects.Session;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Optional;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (GameSessionManager.getGameSessions().isEmpty()) return;

        Player player = event.getEntity();
        Player killer = player.getKiller();

        Optional<Session> sessionOptional = GameSessionManager.getGameSessions().stream()
                .filter(s -> s.getRequester().equalsIgnoreCase(player.getName()) || s.getTarget().equalsIgnoreCase(player.getName()))
                .findFirst();

        if (!sessionOptional.isPresent()) return;

        event.setDeathMessage("§b⚔ §3" + (killer != null ? killer.getName() : "Unknown") + " §8(" + 0 + ") §bdefeated §3" + player.getName() + " §8(" + 0 + ") §bin a duel!");

        event.getDrops().clear();
        player.sendTitle("§c§lYOU LOST", "", 0, 20, 0);

        Session session = sessionOptional.get();
        if (killer != null) {
            Bukkit.getPluginManager().callEvent(new DuelWinEvent(killer, session.getKit() != null ? session.getKit() : "unknown"));
        }

        Bukkit.getPluginManager().callEvent(new DuelEndEvent(player,killer, killer.getName(), sessionOptional.orElseThrow()));

        GameSessionManager.removeGameSession(session);

        // Schedule immediate respawn (1 tick later)
        Bukkit.getScheduler().runTaskLater(KyberPractice.instance, () -> {
            player.spigot().respawn(); // This respawns the player instantly
        }, 1L);
    }
}
