package dragon.me.kyberPractice.listener;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.events.DuelStartEvent;
import dragon.me.kyberPractice.hooks.WorldEditHook;
import dragon.me.kyberPractice.managers.GameSessionManager;
import dragon.me.kyberPractice.managers.objects.Session;
import dragon.me.kyberPractice.managers.objects.enums.GameState;
import dragon.me.kyberPractice.storage.Arena;
import dragon.me.kyberPractice.storage.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class DuelStartListener implements Listener {

    @EventHandler
    public void onDuelStart(DuelStartEvent event) {
        Player player1 = event.getPlayer1();
        Player player2 = event.getPlayer2();
        String kitName = event.getKit();
        String map = event.getMap();



        Kit kit = KyberPractice.kitDataManager.getKit(kitName);

        if (kit == null) {
            player1.sendMessage("§cKit '" + kitName + "' not found!");
            player2.sendMessage("§cKit '" + kitName + "' not found!");
            return;
        }

        player1.setHealth(player1.getMaxHealth());
        player2.setHealth(player2.getMaxHealth());

        player1.getInventory().setArmorContents(kit.getArmor());
        player1.getInventory().setContents(kit.getInventory());
        player1.getInventory().setItemInOffHand(kit.getOffhand());

        player2.getInventory().setArmorContents(kit.getArmor());
        player2.getInventory().setContents(kit.getInventory());
        player2.getInventory().setItemInOffHand(kit.getOffhand());

        Optional<Arena> arenaOptional = KyberPractice.arenaDataManager.getArenas().stream()
                .filter(it -> it.getName().equals(map))
                .findFirst();

        if (!arenaOptional.isPresent()) {
            player1.sendMessage("§cArena '" + map + "' not found!");
            player2.sendMessage("§cArena '" + map + "' not found!");
            return;
        }
        Arena arena = arenaOptional.get();

        WorldEditHook.pasteSchematic(arena.getSpawnPos1(), arena.getName(), () -> {
            if (arena.getSpawnPos1() != null) {
                player1.teleport(arena.getSpawnPos1());
            }
            if (arena.getSpawnPos2() != null) {
                player2.teleport(arena.getSpawnPos2());
            }
            player1.setGameMode(GameMode.SURVIVAL);
            player2.setGameMode(GameMode.SURVIVAL);
            Session session = GameSessionManager.getGameSession(player1);
            new BukkitRunnable() {
                int count = 3;

                @Override
                public void run() {
                    switch (count) {
                        case 3:
                            player1.showTitle(Title.title(
                                    KyberPractice.messageSupplier.getStringAndSerializeIfPossible("win.cooldown.3",player1),Component.text("")
                            ));

                            player2.showTitle(Title.title(
                                    KyberPractice.messageSupplier.getStringAndSerializeIfPossible("win.cooldown.3",player2),Component.text("")
                            ));
                            break;
                        case 2:
                            player1.showTitle(Title.title(
                                    KyberPractice.messageSupplier.getStringAndSerializeIfPossible("win.cooldown.2",player1),Component.text("")
                            ));
                            player2.showTitle(Title.title(
                                    KyberPractice.messageSupplier.getStringAndSerializeIfPossible("win.cooldown.2",player2),Component.text("")
                            ));
                            break;
                        case 1:
                            player1.showTitle(Title.title(
                                    KyberPractice.messageSupplier.getStringAndSerializeIfPossible("win.cooldown.1",player1),Component.text("")
                            ));
                            player2.showTitle(Title.title(
                                    KyberPractice.messageSupplier.getStringAndSerializeIfPossible("win.cooldown.1",player2),Component.text("")
                            ));
                            break;
                        case 0:
                            player1.showTitle(Title.title(
                                    KyberPractice.messageSupplier.getStringAndSerializeIfPossible("win.cooldown.GO",player1),Component.text("")
                            ));
                            player2.showTitle(Title.title(
                                    KyberPractice.messageSupplier.getStringAndSerializeIfPossible("win.cooldown.GO",player2),Component.text("")
                            ));
                            player1.playSound(player1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                            player2.playSound(player2.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                            cancel();
                            session.setState(GameState.FIGHTING);
                            break;
                    }
                    count--;
                }
            }.runTaskTimer(KyberPractice.instance, 0L, 20L);
        });
    }
}
