package dragon.me.kyberPractice.scheduler;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.events.DuelStartEvent;
import dragon.me.kyberPractice.managers.GameSessionManager;
import dragon.me.kyberPractice.managers.objects.Queue;
import dragon.me.kyberPractice.managers.objects.Session;
import dragon.me.kyberPractice.storage.Arena;
import dragon.me.kyberPractice.storage.ArenaDataManager;
import org.bukkit.Bukkit;

import java.util.UUID;

public class QueueScheduler {

    public static void startScheduler() {
        Bukkit.getScheduler().runTaskTimer(KyberPractice.instance,()->{
            for (Queue q : KyberPractice.queueDataManager.getQueues()){
                if (q.getPlayers().size() < 2){
                    continue;
                }else {
                    UUID player1 = q.getPlayers().getFirst();
                    UUID player2 = q.getPlayers().getLast();
                    Bukkit.getPluginManager().callEvent(new DuelStartEvent(Bukkit.getPlayer(player1),
                            Bukkit.getPlayer(player2),
                            q.getKit().getName(), GameSessionManager.getRandomMap()));

                    GameSessionManager.addGameSession(new Session(
                            Bukkit.getPlayer(player1).getName(),
                            Bukkit.getPlayer(player2).getName(),
                            GameSessionManager.getRandomMap(),
                            q.getKit().getName()));

                    q.removePlayer(Bukkit.getPlayer(player1));
                    q.removePlayer(Bukkit.getPlayer(player2));
                }

            }
        },5,20);
    }
}
