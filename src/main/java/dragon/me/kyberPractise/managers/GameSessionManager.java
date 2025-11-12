package dragon.me.kyberPractise.managers;

import dragon.me.kyberPractise.KyberPractice;
import dragon.me.kyberPractise.managers.objects.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public final class GameSessionManager {

    private static final List<Session> gameSessions = new ArrayList<>();
    private static final Random random = new Random();

    private GameSessionManager() {}

    public static List<Session> getGameSessions() {
        return new ArrayList<>(gameSessions);
    }

    public static void addGameSession(Session session) {
        gameSessions.add(session);
    }

    public static void removeGameSession(Session session) {
        gameSessions.remove(session);
        // arenaManager.restoreArena(session.getMap());
    }

    public static void clearGameSessions() {
        gameSessions.clear();
    }

    public static String getRandomMap() {
        List<String> availableArenas = KyberPractice.arenaDataManager.getArenas().stream()
                .map(arena -> arena.getName())
                .filter(name -> gameSessions.stream().noneMatch(session -> session.getMap().equals(name)))
                .collect(Collectors.toList());

        if (availableArenas.isEmpty()) {
            return null;
        }
        return availableArenas.get(random.nextInt(availableArenas.size()));
    }
}
