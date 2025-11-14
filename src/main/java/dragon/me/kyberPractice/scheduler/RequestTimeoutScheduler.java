package dragon.me.kyberPractice.scheduler;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.managers.InviteManager;
import dragon.me.kyberPractice.managers.objects.Invite;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class RequestTimeoutScheduler {

    private RequestTimeoutScheduler() {}

    public static void startScheduler() {
        Bukkit.getScheduler().runTaskTimer(KyberPractice.instance, () -> {

            // Loop through active invites, not players
            for (Invite invite : InviteManager.getAllInvites().values()) {

                long delta = System.currentTimeMillis() - invite.getTimestamp();

                if (delta >= 120_000) { // 120 seconds
                    Player sender = Bukkit.getPlayer(invite.getPlayer());
                    Player target = Bukkit.getPlayer(invite.getTarget());

                    // Remove invite
                    InviteManager.timeoutRequest(invite.getPlayer(), invite.getTarget());

                    if (target != null) {
                        target.sendMessage(
                                "§bDuel §8» §cThe duel request of §4" + (sender != null ? sender.getName() : "Unknown") + "§c has expired."
                        );
                    }

                    Bukkit.getLogger().info(
                            "Expired duel request: " + (sender != null ? sender.getName() : "Unknown") + " → " + (target != null ? target.getName() : "Unknown")
                    );
                }
            }

        }, 20L, 20L); // delay 20 ticks, run every 20 ticks (1 second)
    }
}
