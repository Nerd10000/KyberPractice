package dragon.me.kyberPractice.commands;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.commands.subcommands.QueueSubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class QueueCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof org.bukkit.entity.Player player) {
            if (strings.length == 0) {
                return true;
            }
            String arg1 = strings[0].toLowerCase();
            switch (arg1){
                case "create":
                    if (strings.length < 3){
                        player.sendMessage(KyberPractice.messageSupplier.getStringAndSerializeIfPossible("queue.create.usage", player));
                        return true;
                    }
                    String name = strings[1];
                    String kit = strings[2];

                    if (KyberPractice.kitDataManager.getKit(kit) == null){
                        player.sendMessage(KyberPractice.messageSupplier.getStringAndSerializeIfPossible("queue.create.kit-not-found", player));
                        return true;
                    }else {
                        QueueSubCommand.create(name,kit,player);

                    }
                    break;
                case "join":
                    if (strings.length < 2){
                        return true;
                    }
                    String queueName = strings[1];
                    if (KyberPractice.queueDataManager.getQueue(queueName) != null){
                        QueueSubCommand.join(queueName,player);
                        List<String> names = new ArrayList<>();

                        for (UUID uuid : KyberPractice.queueDataManager.getQueue(queueName).getPlayers()){
                            if (Bukkit.getPlayer(uuid) != null) {
                                names.add(Bukkit.getPlayer(uuid).getName());
                            }
                        }
                        player.showTitle(Title.title(
                                KyberPractice.messageSupplier.serializeString(
                                        KyberPractice.messageSupplier.getRawString("queue.join.title").replace("{queue}", queueName),
                                        player
                                ),
                                KyberPractice.messageSupplier.serializeString(
                                        KyberPractice.messageSupplier.getRawString("queue.join.subtitle").replace("{players}", String.valueOf(names)),
                                        player
                                )
                        ));
                    }else {
                        player.sendMessage(KyberPractice.messageSupplier.getStringAndSerializeIfPossible("queue.join.not-found", player));
                    }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("create");
            suggestions.add("join");
            return filterByPrefix(suggestions, args[0]);
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("join")) {
                suggestions.addAll(KyberPractice.queueDataManager.getQueues().stream()
                        .map(q -> q.getName())
                        .collect(Collectors.toList()));
            }
            // For create, we don't know the new queue name – no suggestions.
            return filterByPrefix(suggestions, args[1]);
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
            suggestions.addAll(KyberPractice.kitDataManager.getKits().stream()
                    .map(kit -> kit.getName())
                    .collect(Collectors.toList()));
            return filterByPrefix(suggestions, args[2]);
        }
        return Collections.emptyList();
    }

    private List<String> filterByPrefix(List<String> source, String prefix) {
        String p = prefix == null ? "" : prefix.toLowerCase();
        return source.stream()
                .filter(s -> s != null && s.toLowerCase().startsWith(p))
                .collect(Collectors.toList());
    }
}
