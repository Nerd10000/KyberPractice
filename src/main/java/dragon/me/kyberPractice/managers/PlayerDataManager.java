package dragon.me.kyberPractice.managers;

import dragon.me.kyberPractice.managers.objects.Session;

import java.util.ArrayList;
import java.util.List;

public final class PlayerDataManager {

    private static final List<Session> sessions = new ArrayList<>();

    private PlayerDataManager() {}

    public static List<Session> getSessions() {
        return new ArrayList<>(sessions);
    }

    public static void addSession(Session session) {
        sessions.add(session);
    }

    public static void removeSession(Session session) {
        sessions.remove(session);
    }

    public static void clearSessions() {
        sessions.clear();
    }
}
