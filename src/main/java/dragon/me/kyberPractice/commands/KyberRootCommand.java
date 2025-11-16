package dragon.me.kyberPractice.commands;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.commands.subcommands.ArenaSubCommand;
import dragon.me.kyberPractice.commands.subcommands.KitSubCommand;
import dragon.me.kyberPractice.storage.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



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
                                "   §7§lComing soon!"
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
                                Arena arena = KyberPractice.arenaDataManager.getArena(args[2]);

                                if (arena != null) {
                                    ArenaSubCommand.restoreArena(arena, player);
                                } else {
                                    player.sendMessage("§bDuels §8» §cCouldn't restore the arena '" + args[2] + "' as it doesn't exist.");
                                }
                            }
                            break;

                        case "savearena":

                            Arena arena = KyberPractice.arenaDataManager.getArena(args[2]);

                            if (arena != null) {
                                KyberPractice.instance.getLogger().info("The arena '" + args[2] + "' has been saved.");
                                ArenaSubCommand.saveSchematic(arena, player);
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
            } else if (args[0].equalsIgnoreCase("reload")) {
                //TODO Implement it!
            } else if (args[0].equalsIgnoreCase("setlobby")) {
                player.sendMessage("§bDuels §8» §bThe lobby has been set to your current location. §3(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + ")");
                KyberPractice.instance.getConfig().set("lobby-location", player.getLocation());
                KyberPractice.instance.saveConfig();
            }
        }

        // TODO: Handle other arguments here...

        return true;
    }
}
