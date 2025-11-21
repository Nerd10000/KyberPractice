package dragon.me.kyberPractice.listener;

import dragon.me.kyberPractice.managers.GameSessionManager;
import dragon.me.kyberPractice.managers.objects.Session;
import dragon.me.kyberPractice.managers.objects.enums.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerCombatListener implements Listener {

    @EventHandler
    public void onPlayerInteract(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player){
            if (event.getDamager() instanceof  Player player2){
                Session session = GameSessionManager.getGameSession(player);
                if (session != null){
                    if (session.getState() == GameState.STARTING){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
