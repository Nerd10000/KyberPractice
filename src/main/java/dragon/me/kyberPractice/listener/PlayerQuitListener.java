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

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        Session session = GameSessionManager.getGameSession(event.getPlayer());
        if (session != null) {
            GameSessionManager.removeGameSession(session);
        }
        Player player1 = Bukkit.getPlayer(session.getRequester());
        Player player2 = Bukkit.getPlayer(session.getTarget());
        Location lobby = KyberPractice.instance.getConfig().getLocation("lobby-location");
        Player clean = (player1.equals(event.getPlayer()) ? player2 : player1);
        Player punished = (player1.equals(event.getPlayer()) ? player1 : player2);

        clean.sendTitle("§4§lMATCH WAS CANCELED", "");
        clean.sendMessage("""
            
            §4§lMATCH WAS CANCELED!
            §cThe match has been cancelled because the other participant left the game..
            §cRemember: §nquiting duel is a punishable offense on this server§r§c!
            
            
            """);


        clean.teleportAsync(lobby);
        punished.teleportAsync(lobby);

        clean.getInventory().clear();
        punished.getInventory().clear();
        Bukkit.getPluginManager().callEvent(new DuelEndEvent(punished,clean, "-", session));
        GameSessionManager.removeGameSession(session);
    }
}
