package dragon.me.kyberPractice.listener;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.events.DuelEndEvent;
import dragon.me.kyberPractice.events.DuelWinEvent;
import dragon.me.kyberPractice.hooks.SqliteHook;
import dragon.me.kyberPractice.managers.GameSessionManager;
import dragon.me.kyberPractice.managers.objects.Session;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


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

        if (KyberPractice.instance.getConfig().getBoolean("broadcast-results", true)) {

            String raw = KyberPractice.messageSupplier.getRawString("result-message");

            String replaced = raw
                    .replace("{player}", player.getName())
                    .replace("{killer}", killer != null ? killer.getName() : "Unknown")
                    .replace("{kit}", session.getKit() != null ? session.getKit() : "unknown")
                    .replace("{map}", session.getMap() != null ? session.getMap() : "unknown");

            Component msg = KyberPractice.messageSupplier.serializeString(replaced, player);

            event.setDeathMessage("");
            Bukkit.broadcast(msg);
        }

        event.getDrops().clear();
        player.showTitle(Title.title(KyberPractice.messageSupplier.getStringAndSerializeIfPossible("lose.title",player), Component.text("")));

        if (killer != null) {
            Bukkit.getPluginManager().callEvent(new DuelWinEvent(killer, session.getKit() != null ? session.getKit() : "unknown"));
        }

        Player requesterPlayer = Bukkit.getPlayer(session.getRequester());
        Player targetPlayer = Bukkit.getPlayer(session.getTarget());

        String opponentName = player.getName().equalsIgnoreCase(session.getRequester())
                ? session.getTarget()
                : session.getRequester();
        String winnerName = killer != null ? killer.getName() : opponentName;


        Player safeP1 = requesterPlayer != null ? requesterPlayer : player;
        Player safeP2 = targetPlayer != null ? targetPlayer : (killer != null ? killer : player);

        Bukkit.getPluginManager().callEvent(new DuelEndEvent(safeP1, safeP2, winnerName, session));

        GameSessionManager.removeGameSession(session);
        SqliteHook.incrementLosses(player.getUniqueId().toString(), session.getKit() != null ? session.getKit() : "unknown");

        Bukkit.getScheduler().runTaskLater(KyberPractice.instance, () -> {
            player.spigot().respawn();
            Bukkit.getScheduler().runTaskLater(KyberPractice.instance, () -> {
                player.teleport(deathLoc);
            }, 1L);

        }, 1L);




        Bukkit.getScheduler().runTaskLater(KyberPractice.instance, ()-> {
            if (KyberPractice.instance.getConfig().getLocation("lobby-location") == null) {
                KyberPractice.instance.getLogger().severe("[INCORRECT SETUP] Hey, we noticed the lobby location hasn't been set please do it via /kpractice setlobby");
            }else {
                if (killer != null) {
                    killer.teleport(KyberPractice.instance.getConfig().getLocation("lobby-location"));
                }
                player.teleport(KyberPractice.instance.getConfig().getLocation("lobby-location"));
            }



        },20 * 5);
    }
}
