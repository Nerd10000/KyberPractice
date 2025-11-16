package dragon.me.kyberPractice.listener;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.events.DuelEndEvent;
import dragon.me.kyberPractice.events.DuelWinEvent;
import dragon.me.kyberPractice.managers.GameSessionManager;
import dragon.me.kyberPractice.managers.objects.Session;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        Location deathLoc = player.getLocation().clone();
        Session session = null;

        for (Session s : GameSessionManager.getGameSessions()) {
            if (s.getRequester().equalsIgnoreCase(player.getName()) || s.getTarget().equalsIgnoreCase(player.getName())){
                session  = s;
                break;
            }
        }

        if (session == null) return;

        event.setDeathMessage("§b⚔ §3" + (killer != null ? killer.getName() : "Unknown") + " §8(" + 0 + ") §bdefeated §3" + player.getName() + " §8(" + 0 + ") §bin a duel!");

        event.getDrops().clear();
        player.sendTitle("§c§lYOU LOST", "", 0, 20, 0);

        if (killer != null) {
            Bukkit.getPluginManager().callEvent(new DuelWinEvent(killer, session.getKit() != null ? session.getKit() : "unknown"));
        }

        Bukkit.getPluginManager().callEvent(new DuelEndEvent(player,killer, killer.getName(), session));

        GameSessionManager.removeGameSession(session);

        // Schedule immediate respawn (1 tick later)
        Bukkit.getScheduler().runTaskLater(KyberPractice.instance, () -> {
            player.spigot().respawn();
            Bukkit.getScheduler().runTaskLater(KyberPractice.instance, () -> {
                player.teleport(deathLoc);
            }, 1L);

        }, 1L);




        Bukkit.getScheduler().runTaskLater(KyberPractice.instance, ()-> {

            killer.teleport(KyberPractice.instance.getConfig().getLocation("lobby-location"));
            player.teleport(KyberPractice.instance.getConfig().getLocation("lobby-location"));

        },20 * 5);
    }
}
