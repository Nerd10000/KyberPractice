package dragon.me.kyberPractise.hooks;

import dragon.me.kyberPractise.KyberPractice;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public final class SqliteHook {
    private static final File file = new File(KyberPractice.instance.getDataFolder(), "data.db");
    private static Connection connection;

    private static final String kitsQuery =
            "CREATE TABLE IF NOT EXISTS kits (\n" +
            "    kit_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    kit_name TEXT UNIQUE NOT NULL\n" +
            ");";

    private static final String statsQuery =
            "CREATE TABLE IF NOT EXISTS kit_stats (\n" +
            "    player_uuid TEXT NOT NULL,\n" +
            "    kit_id INTEGER NOT NULL,\n" +
            "    wins INTEGER DEFAULT 0,\n" +
            "    losses INTEGER DEFAULT 0,\n" +
            "    PRIMARY KEY(player_uuid, kit_id),\n" +
            "    FOREIGN KEY(kit_id) REFERENCES kits(kit_id) ON DELETE CASCADE\n" +
            ");";

    private SqliteHook() {}

    /** Connects and creates tables if needed */
    public static void setupDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
            try (java.sql.Statement statement = connection.createStatement()) {
                statement.execute(kitsQuery);
                statement.execute(statsQuery);
            }
            KyberPractice.instance.getLogger().info("✅ Database initialized successfully!");
        } catch (Exception ex) {
            KyberPractice.instance.getLogger().severe("❌ Database connection failed: " + ex.getMessage());
        }
    }

    /** Returns the connection (if needed for raw queries) */
    public static Connection getConnection() {
        return connection;
    }

    /** Adds a kit (admin) */
    public static void addKit(String name) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT OR IGNORE INTO kits(kit_name) VALUES(?)")) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Removes a kit (admin) */
    public static void removeKit(String name) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM kits WHERE kit_name = ?")) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Gets kit ID by name */
    public static Integer getKitIdByName(String name) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT kit_id FROM kits WHERE kit_name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("kit_id");
                } else {
                    return null;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /** Ensures a stat row exists for the player + kit */
    private static void ensureStatRow(String playerUuid, int kitId) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT OR IGNORE INTO kit_stats(player_uuid, kit_id) VALUES(?, ?)")) {
            ps.setString(1, playerUuid);
            ps.setInt(2, kitId);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Increment wins for a player in a kit */
    public static void incrementWins(String playerUuid, String kitName) {
        Integer kitId = getKitIdByName(kitName);
        if (kitId == null) return;
        ensureStatRow(playerUuid, kitId);
        try (PreparedStatement ps = connection.prepareStatement("UPDATE kit_stats SET wins = wins + 1 WHERE player_uuid = ? AND kit_id = ?")) {
            ps.setString(1, playerUuid);
            ps.setInt(2, kitId);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Increment losses for a player in a kit */
    public static void incrementLosses(String playerUuid, String kitName) {
        Integer kitId = getKitIdByName(kitName);
        if (kitId == null) return;
        ensureStatRow(playerUuid, kitId);
        try (PreparedStatement ps = connection.prepareStatement("UPDATE kit_stats SET losses = losses + 1 WHERE player_uuid = ? AND kit_id = ?")) {
            ps.setString(1, playerUuid);
            ps.setInt(2, kitId);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Get player stats for a kit */
    public static Pair<Integer, Integer> getPlayerStats(String playerUuid, String kitName) {
        Integer kitId = getKitIdByName(kitName);
        if (kitId == null) return null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT wins, losses FROM kit_stats WHERE player_uuid = ? AND kit_id = ?")) {
            ps.setString(1, playerUuid);
            ps.setInt(2, kitId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pair<>(rs.getInt("wins"), rs.getInt("losses"));
                } else {
                    return new Pair<>(0, 0);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /** Get all kits and stats for a player */
    public static Map<String, Pair<Integer, Integer>> getAllPlayerStats(String playerUuid) {
        Map<String, Pair<Integer, Integer>> stats = new HashMap<>();
        String query = "SELECT k.kit_name, s.wins, s.losses " +
                       "FROM kit_stats s " +
                       "JOIN kits k ON s.kit_id = k.kit_id " +
                       "WHERE s.player_uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, playerUuid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    stats.put(rs.getString("kit_name"), new Pair<>(rs.getInt("wins"), rs.getInt("losses")));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stats;
    }

    public static class Pair<A, B> {
        public final A a;
        public final B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }
}
