
package dragon.me.kyberPractice.api;

import dragon.me.kyberPractice.hooks.PlaceholderApiHook;
import dragon.me.kyberPractice.hooks.SqliteHook;
import dragon.me.kyberPractice.hooks.WorldEditHook;
import dragon.me.kyberPractice.listener.PlayerDeathListener;
import dragon.me.kyberPractice.managers.GameSessionManager;
import dragon.me.kyberPractice.managers.InviteManager;
import dragon.me.kyberPractice.managers.PartyManager;
import dragon.me.kyberPractice.managers.PlayerDataManager;

public interface KyberPracticeApi {
   GameSessionManager getSessionManager();
   InviteManager getInviteManager();
   PartyManager getPartyManager();
   PlayerDataManager getPlayerDataManager();

   SqliteHook getSqliteHook();
   PlaceholderApiHook getPlaceholderApiHook();
   WorldEditHook getWorldEditHook();


    
}