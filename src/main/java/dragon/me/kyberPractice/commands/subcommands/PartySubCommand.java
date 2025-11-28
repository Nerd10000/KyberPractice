package dragon.me.commands.subcommands;

import javax.naming.PartialResultException;
import javax.security.auth.callback.PasswordCallback;

import dragon.me.kyberPractice.managers.PartyManager;

public final class PartySubCommand {

    public void create(String name,Player creator){
        //TODO implement logic
        PartyManager.addParty(name, creator.getUniqueId());


    }
    public void disband(String name, Player disbander){
        if (PartyManager.partyMap.get(name).get(0) == disbander.getUniqueId()){
            PartyManager.removeParty(name);
        }

    }



}