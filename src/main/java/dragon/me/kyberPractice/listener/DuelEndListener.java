package dragon.me.kyberPractice.listener;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.events.DuelEndEvent;
import dragon.me.kyberPractice.hooks.WorldEditHook;
import dragon.me.kyberPractice.managers.objects.enums.GameState;
import dragon.me.kyberPractice.storage.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DuelEndListener implements Listener {

    @EventHandler
    public void onDuelEnd(DuelEndEvent event) {

        Arena arena = KyberPractice.arenaDataManager.getArena(event.getSession().getMap());

        if (KyberPractice.isWorldEditAvailable){

            // Pasting the schematic if able to
            WorldEditHook.pasteSchematic(
                    WorldEditHook.getMidPoint(arena.getPos1(), arena.getPos2()),
                    arena.getName(),
                    () -> {
                        KyberPractice.instance.getLogger().info("The arena '" + arena.getName() + "' has been restored in the world");
                        event.getSession().setState(GameState.ENDED);

                    }
            );

        } else {
            event.getSession().setState(GameState.ENDED);
        }
        KyberPractice.inventoryManager.restorePlayerInventory(event.getPlayer1().getUniqueId());
        KyberPractice.inventoryManager.restorePlayerInventory(event.getPlayer2().getUniqueId());
    }
}
