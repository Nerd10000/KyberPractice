package dragon.me.kyberPractise.listener;

import dragon.me.kyberPractise.KyberPractice;
import dragon.me.kyberPractise.events.DuelEndEvent;
import dragon.me.kyberPractise.hooks.WorldEditHook;
import dragon.me.kyberPractise.storage.Arena;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

public class DuelEndListener implements Listener {

    @EventHandler
    public void onDuelEnd(DuelEndEvent event) {

        Optional<Arena> arena = KyberPractice.arenaDataManager.getArenas().stream()
                .filter(arena1 -> arena1.getName().equals(event.getSession().getMap()))
                .findFirst();

        arena.ifPresent(arena1 -> {

            if (KyberPractice.isWorldEditAvailable){

                // Pasting the schematic if able to
                WorldEditHook.pasteSchematic(
                        WorldEditHook.getMidPoint(arena1.getPos1(), arena1.getPos2()),
                        arena1.getName()
                );
                KyberPractice.instance.getLogger().info("The arena '" + arena1.getName() + "' has been restored in the world");

            }

        });
        arena.orElseThrow();
    }
}
