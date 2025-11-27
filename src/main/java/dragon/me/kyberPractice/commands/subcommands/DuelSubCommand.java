package dragon.me.kyberPractice.commands.subcommands;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.events.DuelStartEvent;
import dragon.me.kyberPractice.managers.GameSessionManager;
import dragon.me.kyberPractice.managers.InviteManager;
import dragon.me.kyberPractice.managers.objects.Invite;
import dragon.me.kyberPractice.managers.objects.Session;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class DuelSubCommand {

    private DuelSubCommand() {}

    public static void acceptDuel(Player player, String name) {
        Player requester = Bukkit.getPlayer(name);
        if (requester == null) {
            player.sendMessage(KyberPractice.messageSupplier.serializeString(
                    KyberPractice.messageSupplier.getRawString("accept.offline-target").replace("{name}", name),
                    player
            ));
            return;
        }

        Invite invite = InviteManager.getInviteByPlayer(player.getUniqueId());
        if (invite == null || !invite.getChallenger().equals(requester.getUniqueId())) {
            player.sendMessage(KyberPractice.messageSupplier.serializeString(
                    KyberPractice.messageSupplier.getRawString("accept.no-pending").replace("{requester}", requester.getName()),
                    player
            ));
            return;
        }

        String randomMap = GameSessionManager.getRandomMap();
        if (randomMap == null) {
            requester.sendMessage(KyberPractice.messageSupplier.serializeString(
                    KyberPractice.messageSupplier.getRawString("accept.arenas-full.requester").replace("{player}", player.getName()),
                    requester
            ));
            player.sendMessage(KyberPractice.messageSupplier.serializeString(
                    KyberPractice.messageSupplier.getRawString("accept.arenas-full.player"),
                    player
            ));
            return;
        }

        GameSessionManager.addGameSession(
                new Session(requester.getName(), player.getName(), randomMap, invite.getKit() != null ? invite.getKit() : "UHC")
        );

        // Save original inventories BEFORE the duel start event applies kits
        KyberPractice.inventoryManager.addInventory(player.getUniqueId(), player.getInventory());
        KyberPractice.inventoryManager.addInventory(requester.getUniqueId(), requester.getInventory());

        Bukkit.getPluginManager().callEvent(new DuelStartEvent(requester, player, invite.getKit() != null ? invite.getKit() : "UHC", randomMap));

        requester.sendMessage(KyberPractice.messageSupplier.serializeString(
                KyberPractice.messageSupplier.getRawString("accept.notify.requester")
                        .replace("{player}", player.getName())
                        .replace("{map}", randomMap),
                requester
        ));
        player.sendMessage(KyberPractice.messageSupplier.serializeString(
                KyberPractice.messageSupplier.getRawString("accept.notify.player")
                        .replace("{requester}", requester.getName())
                        .replace("{map}", randomMap),
                player
        ));

        InviteManager.removeInvite(invite);
    }
}
