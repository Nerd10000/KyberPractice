package dragon.me.kyberPractise.commands;

import dragon.me.kyberPractise.KyberPractice;
import dragon.me.kyberPractise.commands.subcommands.ArenaSubCommand;
import dragon.me.kyberPractise.commands.subcommands.KitSubCommand;
import dragon.me.kyberPractise.hooks.WorldEditHook;
import dragon.me.kyberPractise.storage.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class KyberRootCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) {
                player.sendMessage(
                        "§3§lKyber§b§lPractise §fHelp\n" +
                                " §b/kyberpractise arena §3§lArguments\n" +
                                "   §b/kyberpractise arena create §3<arena name> §7Creates a new arena.\n" +
                                "   §b/kyberpractise arena pos1 §3<arena name> §7Sets the first position of the arena region.\n" +
                                "   §b/kyberpractise arena pos2 §3<arena name> §7Sets the second position of the arena region.\n" +
                                "   §b/kyberpractise arena spawnPos2 §3<arena name> §7Sets the 2nd spawn point of an arena.\n" +
                                "   §b/kyberpractise arena spectatorPos §3<arena name> §7Sets the spectator position of an arena.\n" +
                                "   §b/kyberpractise arena delete §3<arena name> §7Deletes an arena.\n" +
                                "   §b/kyberpractise arena teleport §3<arena name> §7Teleports you to the arena.\n" +
                                "   §b/kyberpractise arena savearena §3<arena name> §7Saves the arena as a schematic.\n" +
                                "   §b/kyberpractise arena restorearena §3<arena name> §7Restores the arena from its schematic.\n" +
                                " \n" +
                                " §b/kyberpractise kit §3§lArguments\n" +
                                "   §7§lComing soon!ிகளில்"
                );
            } else if (args[0].equalsIgnoreCase("arena")) {
                if (args.length > 1) {
                    switch (args[1].toLowerCase()) {
                        case "create":
                            if (args.length > 2) ArenaSubCommand.create(player, args[2]);
                            break;
                        case "pos1":
                            if (args.length > 2) ArenaSubCommand.setPos1(player, player.getLocation(), args[2]);
                            break;
                        case "pos2":
                            if (args.length > 2) ArenaSubCommand.setPos2(player, player.getLocation(), args[2]);
                            break;
                        case "spawnpos1":
                            if (args.length > 2) ArenaSubCommand.setSpawnPos1(player, player.getLocation(), args[2]);
                            break;
                        case "spawnpos2":
                            if (args.length > 2) ArenaSubCommand.setSpawnPos2(player, player.getLocation(), args[2]);
                            break;
                        case "spectatorpos":
                            if (args.length > 2) ArenaSubCommand.setSpectatorPos(player, player.getLocation(), args[2]);
                            break;
                        case "delete":
                            if (args.length > 2) ArenaSubCommand.delete(player, args[2]);
                            break;
                        case "teleport":
                            if (args.length > 2) ArenaSubCommand.teleport(player, args[2]);
                            break;

                        case "restorearena":
                            if (args.length > 2) {
                                Optional<Arena> arenaOptional = KyberPractice.arenaDataManager.getArenas().stream()
                                        .filter(a -> a.getName().equalsIgnoreCase(args[2]))
                                        .findFirst();
                                if (arenaOptional.isPresent()) {
                                    ArenaSubCommand.restoreArena(arenaOptional.get(), player);
                                } else {
                                    player.sendMessage("§bDuels §8» §cCouldn't restore the arena '" + args[2] + "' as it doesn't exist.");
                                }
                            }
                            break;

                        case "savearena":



                            Optional<Arena> arenas = KyberPractice.arenaDataManager.getArenas().stream()
                                    .filter(arenas2 -> arenas2.getName().equals(args[2]))
                                    .findFirst();
                            if (arenas.isPresent()) {
                                KyberPractice.instance.getLogger().info("The arena '" + args[2] + "' has been saved.");
                                ArenaSubCommand.saveSchematic(arenas.get(), player);
                            }else {
                                KyberPractice.instance.getLogger().info("The arena '" + args[2] + "' couldn't  be saved.");
                                player.sendMessage("§bDuels §8» §cCouldn't save the arena '" + args[2] + "' as it doesn't exist.");
                            }
                            break;



                    }
                }
            } else if (args[0].equalsIgnoreCase("kit")) {
                if (args.length > 1) {
                    switch (args[1].toLowerCase()) {
                        case "create":
                            if (args.length > 2) KitSubCommand.create(player, args[2]);
                            break;
                        case "load":
                            if (args.length > 2) KitSubCommand.loadKit(player, args[2]);
                            break;
                    }
                }
            }
        }

        // TODO: Handle other arguments here...

        return true;
    }
}
