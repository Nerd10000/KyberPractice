package dragon.me.kyberPractice.commands;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.commands.subcommands.ArenaSubCommand;
import dragon.me.kyberPractice.commands.subcommands.KitSubCommand;
import dragon.me.kyberPractice.storage.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KyberRootCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;



            if (args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) {
                if (!player.hasPermission("kyberpractice.cmds.help")) {
                    KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                    return false;
                }
                KyberPractice.messageSupplier.getStringListAndSerializeIfPossible("help", player)
                        .forEach(player::sendMessage);
            } else if (args[0].equalsIgnoreCase("arena")) {
                if (args.length > 1) {
                    switch (args[1].toLowerCase()) {
                        case "create":
                            if (!player.hasPermission("kyberpractice.cmds.arena.create")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            if (args.length > 2) ArenaSubCommand.create(player, args[2]);
                            break;
                        case "pos1":
                            if (!player.hasPermission("kyberpractice.cmds.arena.pos1")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            if (args.length > 2) ArenaSubCommand.setPos1(player, player.getLocation(), args[2]);
                            break;
                        case "pos2":
                            if (!player.hasPermission("kyberpractice.cmds.arena.pos2")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            if (args.length > 2) ArenaSubCommand.setPos2(player, player.getLocation(), args[2]);
                            break;
                        case "spawnpos1":
                            if (!player.hasPermission("kyberpractice.cmds.arena.spawnpos1")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            if (args.length > 2) ArenaSubCommand.setSpawnPos1(player, player.getLocation(), args[2]);
                            break;
                        case "spawnpos2":
                            if (!player.hasPermission("kyberpractice.cmds.arena.spawnpos2")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            if (args.length > 2) ArenaSubCommand.setSpawnPos2(player, player.getLocation(), args[2]);
                            break;
                        case "spectatorpos":
                            if (!player.hasPermission("kyberpractice.cmds.arena.spactatorpos")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            if (args.length > 2) ArenaSubCommand.setSpectatorPos(player, player.getLocation(), args[2]);
                            break;
                        case "delete":
                            if (!player.hasPermission("kyberpractice.cmds.arena.delete")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            if (args.length > 2) ArenaSubCommand.delete(player, args[2]);
                            break;
                        case "teleport":
                            if (!player.hasPermission("kyberpractice.cmds.arena.teleport")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            if (args.length > 2) ArenaSubCommand.teleport(player, args[2]);
                            break;

                        case "restorearena":
                            if (!player.hasPermission("kyberpractice.cmds.arena.restorearena")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            if (args.length > 2) {
                                Arena arena = KyberPractice.arenaDataManager.getArena(args[2]);

                                if (arena != null) {
                                    ArenaSubCommand.restoreArena(arena, player);
                                } else {
                                    player.sendMessage(KyberPractice.messageSupplier.serializeString(
                                            KyberPractice.messageSupplier.getRawString("arena.restore.not-exist").replace("{arena}", args[2]),
                                            player
                                    ));
                                }
                            }
                            break;

                        case "savearena":
                            if (!player.hasPermission("kyberpractice.cmds.arena.savearena")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            Arena arena = KyberPractice.arenaDataManager.getArena(args[2]);

                            if (arena != null) {
                                KyberPractice.instance.getLogger().info("The arena '" + args[2] + "' has been saved.");
                                ArenaSubCommand.saveSchematic(arena, player);
                            }else {
                                KyberPractice.instance.getLogger().info("The arena '" + args[2] + "' couldn't  be saved.");
                                player.sendMessage(KyberPractice.messageSupplier.serializeString(
                                        KyberPractice.messageSupplier.getRawString("arena.save.not-exist").replace("{arena}", args[2]),
                                        player
                                ));
                            }
                            break;



                    }
                }
            } else if (args[0].equalsIgnoreCase("kit")) {
                if (!player.hasPermission("kyberpractice.cmds.kit")) {
                    KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                    return false;
                }
                if (args.length > 1) {
                    switch (args[1].toLowerCase()) {
                        case "create":
                            if (!player.hasPermission("kyberpractice.cmds.kit.create")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            if (args.length > 2) KitSubCommand.create(player, args[2]);
                            break;
                        case "load":
                            if (!player.hasPermission("kyberpractice.cmds.kit.load")) {
                                KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                                return false;
                            }
                            if (args.length > 2) KitSubCommand.loadKit(player, args[2]);
                            break;
                    }
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!player.hasPermission("kyberpractice.cmds.reload")) {
                    KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                    return false;
                }
                long start = System.currentTimeMillis();
                KyberPractice.messageSupplier.reloadConfig();
                KyberPractice.instance.reloadConfig();
                player.sendMessage(KyberPractice.messageSupplier.serializeString(
                        KyberPractice.messageSupplier.getRawString("reload.response")
                                .replace("{time}", (System.currentTimeMillis() - start)+""),
                        player
                ));

            } else if (args[0].equalsIgnoreCase("setlobby")) {
                if (!player.hasPermission("kyberpractice.cmds.setlobby")) {
                    KyberPractice.messageSupplier.sendMissingPermissionMessage(player);
                    return false;
                }
                player.sendMessage(KyberPractice.messageSupplier.serializeString(
                        KyberPractice.messageSupplier.getRawString("root.setlobby").
                                replace("{x}", String.valueOf(player.getLocation().getBlockX())).
                                replace("{y}", String.valueOf(player.getLocation().getBlockY())).
                                replace("{z}", String.valueOf(player.getLocation().getBlockZ())),
                        player
                ));
                KyberPractice.instance.getConfig().set("lobby-location", player.getLocation());
                KyberPractice.instance.saveConfig();

            }
        }

        // TODO: Handle other arguments here...

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("arena");
            suggestions.add("kit");
            suggestions.add("reload");
            suggestions.add("setlobby");
            suggestions.add("help");
            suggestions.add("?");
            return filter(suggestions, args[0]);
        }
        if (args.length >= 2) {
            String sub = args[0].toLowerCase();
            switch (sub) {
                case "arena":
                    if (args.length == 2) {
                        suggestions.add("create");
                        suggestions.add("pos1");
                        suggestions.add("pos2");
                        suggestions.add("spawnpos1");
                        suggestions.add("spawnpos2");
                        suggestions.add("spectatorpos");
                        suggestions.add("delete");
                        suggestions.add("teleport");
                        suggestions.add("restorearena");
                        suggestions.add("savearena");
                        return filter(suggestions, args[1]);
                    } else if (args.length == 3) {
                        String action = args[1].toLowerCase();
                        if (!action.equals("create")) {
                            suggestions.addAll(KyberPractice.arenaDataManager.getArenas().stream()
                                    .map(a -> a.getName())
                                    .collect(Collectors.toList()));
                        }
                        return filter(suggestions, args[2]);
                    }
                    break;
                case "kit":
                    if (args.length == 2) {
                        suggestions.add("create");
                        suggestions.add("load");
                        return filter(suggestions, args[1]);
                    } else if (args.length == 3) {
                        if (args[1].equalsIgnoreCase("load")) {
                            suggestions.addAll(KyberPractice.kitDataManager.getKits().stream()
                                    .map(k -> k.getName())
                                    .collect(Collectors.toList()));
                        }
                        return filter(suggestions, args[2]);
                    }
                    break;
                case "reload":
                case "setlobby":
                case "help":
                case "?":
                    return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    private List<String> filter(List<String> source, String prefix) {
        String p = prefix == null ? "" : prefix.toLowerCase();
        return source.stream().filter(s -> s != null && s.toLowerCase().startsWith(p)).collect(Collectors.toList());
    }
}
