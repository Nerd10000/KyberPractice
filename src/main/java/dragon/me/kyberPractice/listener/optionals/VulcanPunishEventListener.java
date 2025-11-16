package dragon.me.kyberPractice.listener.optionals;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.events.DuelEndEvent;
import dragon.me.kyberPractice.managers.GameSessionManager;
import dragon.me.kyberPractice.managers.objects.Session;
import me.frep.vulcan.api.event.VulcanPunishEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VulcanPunishEventListener implements Listener {
    @EventHandler
    public void onVulcanPunishEvent(VulcanPunishEvent event) {
        KyberPractice.instance.getLogger().info("VulcanPunishEvent fired!");
        Player punished = event.getPlayer();
        if (punished == null) return;

        Session session = GameSessionManager.getGameSession(punished);
        if (session == null) return;

        Player requester = Bukkit.getPlayer(session.getRequester());
        Player target = Bukkit.getPlayer(session.getTarget());


        if (requester == null || target == null) {
            GameSessionManager.removeGameSession(session);
            return;
        }

        Location lobby = KyberPractice.instance.getConfig().getLocation("lobby-location");
        if (lobby == null) {
            Bukkit.getLogger().severe("[KyberPractice] ERROR: 'lobby-location' missing from config.yml!");
            return;
        }


        Player clean = (punished.equals(target) ? requester : target);

        clean.sendTitle("§4§lMATCH WAS CANCELED", "");
        clean.sendMessage("""
            
            §4§lMATCH WAS CANCELED!
            §cThe match has been cancelled because the other participant was punished by the anticheat.
            §cRemember: §ncheating is a punishable offense on this server§r§c!
            
            
            """);


        requester.teleportAsync(lobby);
        target.teleportAsync(lobby);

        requester.getInventory().clear();
        target.getInventory().clear();
        Bukkit.getPluginManager().callEvent(new DuelEndEvent(punished,clean, "-", session));
        GameSessionManager.removeGameSession(session);
    }
}
