package dragon.me.kyberPractise.listener;

import dragon.me.kyberPractise.KyberPractise;
import dragon.me.kyberPractise.events.DuelStartEvent;
import dragon.me.kyberPractise.storage.Arena;
import dragon.me.kyberPractise.storage.Kit;
import org.bukkit.Bukkit;
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

        Optional<Kit> kitOptional = KyberPractise.kitDataManager.getKits().stream()
                .filter(it -> it.getName().equalsIgnoreCase(kitName))
                .findFirst();

        if (!kitOptional.isPresent()) {
            player1.sendMessage("§cKit '" + kitName + "' not found!");
            player2.sendMessage("§cKit '" + kitName + "' not found!");
            return;
        }
        Kit kit = kitOptional.get();

        player1.setHealth(player1.getMaxHealth());
        player2.setHealth(player2.getMaxHealth());

        player1.getInventory().setArmorContents(kit.getArmor());
        player1.getInventory().setContents(kit.getInventory());
        player1.getInventory().setItemInOffHand(kit.getOffhand());

        player2.getInventory().setArmorContents(kit.getArmor());
        player2.getInventory().setContents(kit.getInventory());
        player2.getInventory().setItemInOffHand(kit.getOffhand());

        Optional<Arena> arenaOptional = KyberPractise.arenaDataManager.getArenas().stream()
                .filter(it -> it.getName().equals(map))
                .findFirst();

        if (!arenaOptional.isPresent()) {
            player1.sendMessage("§cArena '" + map + "' not found!");
            player2.sendMessage("§cArena '" + map + "' not found!");
            return;
        }
        Arena arena = arenaOptional.get();

        if (arena.getSpawnPos1() != null) {
            player1.teleport(arena.getSpawnPos1());
        }
        if (arena.getSpawnPos2() != null) {
            player2.teleport(arena.getSpawnPos2());
        }
        player1.setGameMode(GameMode.SURVIVAL);
        player2.setGameMode(GameMode.SURVIVAL);

        new BukkitRunnable() {
            int count = 3;

            @Override
            public void run() {
                switch (count) {
                    case 3:
                        player1.sendTitle("§f§l3", "", 0, 20, 0);
                        player2.sendTitle("§f§l3", "", 0, 20, 0);
                        break;
                    case 2:
                        player1.sendTitle("§7§l2", "", 0, 20, 0);
                        player2.sendTitle("§7§l2", "", 0, 20, 0);
                        break;
                    case 1:
                        player1.sendTitle("§b§l1", "", 0, 20, 0);
                        player2.sendTitle("§b§l1", "", 0, 20, 0);
                        break;
                    case 0:
                        player1.sendTitle("§3§lGO", "", 0, 20, 0);
                        player2.sendTitle("§3§lGO", "", 0, 20, 0);
                        player1.playSound(player1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                        player2.playSound(player2.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                        cancel();
                        break;
                }
                count--;
            }
        }.runTaskTimer(KyberPractise.instance, 0L, 20L);
    }
}
