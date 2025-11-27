package dragon.me.kyberPractice.commands;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.commands.subcommands.DuelSubCommand;
import dragon.me.kyberPractice.managers.InviteManager;
import dragon.me.kyberPractice.managers.objects.Invite;
import dragon.me.kyberPractice.storage.Kit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class DuelCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(KyberPractice.messageSupplier.getStringAndSerializeIfPossible("duel.normal-usage",player));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "accept":
                if (args.length < 2) {
                    player.sendMessage(KyberPractice.messageSupplier.getStringAndSerializeIfPossible("duel.only-accept-usage",player));
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
                    player.sendMessage(KyberPractice.messageSupplier.getStringAndSerializeIfPossible("duel.player-not-found",player));
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(KyberPractice.messageSupplier.getStringAndSerializeIfPossible("duel.only-duel-usage",player));
                    return true;
                }

                Kit kit  = KyberPractice.kitDataManager.getKit(args[1]);

                if (kit == null) {
                    player.sendMessage(KyberPractice.messageSupplier.getStringAndSerializeIfPossible("duel.kit-not-found",player));
                    return true;
                }

                Invite invite = new Invite(player.getUniqueId(), target.getUniqueId(), System.currentTimeMillis(), kit.getName() != null ? kit.getName() : "UHC");
                InviteManager.addInvite(invite);

                player.sendMessage(
                        KyberPractice.messageSupplier
                                .serializeString(KyberPractice.messageSupplier.getRawString("duel.request-sent")
                                        .replace("{target}", target.getName())
                                        .replace("{kit}",kit.getName() != null ? kit.getName() : "unknown")
                                        .replace("{challenger}",player.getName()), player)

                );
                List<String> placeholderedString = new ArrayList<>();
                for (String i : KyberPractice.messageSupplier.getRawStringList("duel.request-notification")){
                    placeholderedString.add(i
                            .replace("{target}", target.getName())
                            .replace("{kit}",kit.getName() != null ? kit.getName() : "unknown")
                            .replace("{challenger}",player.getName()));
                }

                for (String i : placeholderedString){
                    target.sendMessage(KyberPractice.messageSupplier.serializeString(i, player));
                }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            List<String> base = new ArrayList<>();
            base.add("accept");
            base.add("decline");
            // add online player names
            base.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
            return filter(base, args[0]);
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("decline")) {
                // Suggest online players
                return filter(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), args[1]);
            } else {
                // args[0] is target player name -> suggest kit names
                return filter(KyberPractice.kitDataManager.getKits().stream().map(k -> k.getName()).collect(Collectors.toList()), args[1]);
            }
        }
        return Collections.emptyList();
    }

    private List<String> filter(List<String> list, String prefix) {
        String p = prefix == null ? "" : prefix.toLowerCase();
        return list.stream().filter(s -> s != null && s.toLowerCase().startsWith(p)).collect(Collectors.toList());
    }
}
