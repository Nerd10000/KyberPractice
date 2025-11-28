package dragon.me.kyberPractice.api.impl;

import java.lang.reflect.InaccessibleObjectException;
import java.nio.file.WatchEvent;

import dragon.me.kyberPractice.api.KyberPracticeApi;
import dragon.me.kyberPractice.hooks.PlaceholderApiHook;
import dragon.me.kyberPractice.hooks.SqliteHook;
import dragon.me.kyberPractice.hooks.WorldEditHook;
import dragon.me.kyberPractice.managers.GameSessionManager;
import dragon.me.kyberPractice.managers.InviteManager;
import dragon.me.kyberPractice.managers.PartyManager;
import dragon.me.kyberPractice.managers.PlayerDataManager;

public class KyberPracticeApiImpl implements KyberPracticeApi {
   
    public KyberPracticeApiImpl(GameSessionManager sessionManager,
                                 InviteManager inviteManager,
                                 PartyManager partyManager,
                                 PlayerDataManager playerDataManager,
                                 SqliteHook SqliteHook,
                                 PlaceholderApiHook placeholderApiHook,
                                 WorldEditHook worldEditHook){
    
        
    }

    @Override
    public GameSessionManager getSessionManager() {
        return null;
    }

    @Override
    public InviteManager getInviteManager() {
        return null;
    }

    @Override
    public PartyManager getPartyManager() {
        return null;
    }

    @Override
    public PlayerDataManager getPlayerDataManager() {
        return null;
    }

    @Override
    public SqliteHook getSqliteHook() {
        return null;
    }

    @Override
    public PlaceholderApiHook getPlaceholderApiHook() {
        return null;
    }

    @Override
    public WorldEditHook getWorldEditHook() {
        return null;
    }
}