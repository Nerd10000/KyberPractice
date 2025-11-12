package dragon.me.kyberPractise.commands;

import dragon.me.kyberPractise.KyberPractice;
import dragon.me.kyberPractise.commands.subcommands.DuelSubCommand;
import dragon.me.kyberPractise.managers.InviteManager;
import dragon.me.kyberPractise.managers.objects.Invite;
import dragon.me.kyberPractise.storage.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class DuelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§bDuels §8» §7Usage: /duel <player> | accept <player> | decline <player>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "accept":
                if (args.length < 2) {
                    player.sendMessage("§bDuels §8» §cUsage: /duel accept <player>");
                    return true;
                }
                DuelSubCommand.acceptDuel(player, args[1]);
                break;

            case "decline":
                // TODO: decline logic
                break;

            default:
                Player target = player.getServer().getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("§bDuels §8» §cPlayer not found.");
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage("§bDuels §8» §cUsage: /duel <player> <kit>");
                    return true;
                }

                Optional<Kit> optionalKit = KyberPractice.kitDataManager.getKits().stream()
                        .filter(k -> k.getName().equalsIgnoreCase(args[1]))
                        .findFirst();

                if (!optionalKit.isPresent()) {
                    player.sendMessage("§bDuels §8» §cKit not found.");
                    return true;
                }
                Kit kit = optionalKit.get();


                Invite invite = new Invite(player.getUniqueId(), target.getUniqueId(), System.currentTimeMillis(), kit.getName() != null ? kit.getName() : "UHC");
                InviteManager.addInvite(invite);
                player.sendMessage("§bDuels §8» §bYou have sent a duel request to §3" + target.getName() + "§b.");
                target.sendMessage(
                        "§b§lDUEL REQUEST\n" +
                                "\n" +
                                "§3From §b" + player.getName() + "\n" +
                                "§3Kit §b§l" + kit.getName() + "\n" +
                                "\n" +
                                "§bType §3/duel accept " + player.getName() + " §bto accept.\n" +
                                "§cOr §4/duel decline " + player.getName() + " §cto decline."
                );
                break;
        }

        return true;
    }
}
