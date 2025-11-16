package dragon.me.kyberPractice.hooks;

import dragon.me.kyberPractice.KyberPractice;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderApiHook extends PlaceholderExpansion {


    @Override
    public @NotNull String getIdentifier() {
        return "kyberpractice";
    }

    @Override
    public @NotNull String getAuthor() {
        return "DragonCraft64";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("wins")) {
            int wins = SqliteHook.getGlobalWins(player);
            return wins + "";
        }else if (params.startsWith("wins:")) {
            String name = params.split(":",2)[1];
            OfflinePlayer target = Bukkit.getPlayer(name);

            return target == null ? "0" : SqliteHook.getGlobalWins(target) + "";

        }else {
            return null;
        }
    }
    @Override
    public boolean persist(){
        return true;
    }
    public static void registerHook(){
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderApiHook().register();
            KyberPractice.instance.getLogger().info("[APIs] [ApiHookEvent] PlaceholderAPI hooked successfully!");
        }
    }
}
