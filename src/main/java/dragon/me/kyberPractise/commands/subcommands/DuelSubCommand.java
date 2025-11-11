package dragon.me.kyberPractise.commands.subcommands;

import dragon.me.kyberPractise.events.DuelStartEvent;
import dragon.me.kyberPractise.managers.GameSessionManager;
import dragon.me.kyberPractise.managers.InviteManager;
import dragon.me.kyberPractise.managers.objects.Invite;
import dragon.me.kyberPractise.managers.objects.Session;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class DuelSubCommand {

    private DuelSubCommand() {}

    public static void acceptDuel(Player player, String name) {
        Player requester = Bukkit.getPlayer(name);
        if (requester == null) {
            player.sendMessage("§bDuels §8» §cPlayer §4'" + name + "'§c is not online.");
            return;
        }

        Invite invite = InviteManager.getInviteByPlayer(player.getUniqueId());
        if (invite == null || !invite.getPlayer().equals(requester.getUniqueId())) {
            player.sendMessage("§bDuels §8» §cYou don't have any pending duel requests from §4'" + requester.getName() + "'§c.");
            return;
        }

        String randomMap = GameSessionManager.getRandomMap();
        if (randomMap == null) {
            requester.sendMessage("§bDuels §8» §c" + player.getName() + " accepted the duel, but all arenas are full. Try later!");
            player.sendMessage("§bDuels §8» §cAll arenas full at the moment, try again later.");
            return;
        }

        GameSessionManager.addGameSession(
                new Session(requester.getName(), player.getName(), randomMap, invite.getKit() != null ? invite.getKit() : "UHC")
        );
        Bukkit.getPluginManager().callEvent(new DuelStartEvent(requester, player, invite.getKit() != null ? invite.getKit() : "UHC", randomMap));

        requester.sendMessage("§bDuels §8» §3" + player.getName() + " §baccepted your duel! Starting...");
        player.sendMessage("§bDuels §8» §bDuel starting against §3" + requester.getName() + "§b on map §3" + randomMap + "§b!");

        InviteManager.removeInvite(invite);
    }
}
