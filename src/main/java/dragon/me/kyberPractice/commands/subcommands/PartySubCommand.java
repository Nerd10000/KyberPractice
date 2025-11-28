package dragon.me.kyberPractice.commands.subcommands;

import dragon.me.kyberPractice.managers.PartyManager;
import dragon.me.kyberPractice.managers.objects.Party;
import org.bukkit.entity.Player;

public final class PartySubCommand {

    private PartySubCommand() {}

    public static void create(Player creator, String name) {
        if (creator == null || name == null) return;
        PartyManager.addParty(name, creator.getUniqueId());
    }

    public static void disband(Player disbander, String name) {
        if (disbander == null || name == null) return;
        Party party = PartyManager.getParty(name);
        if (party != null && !party.getMembers().isEmpty() && party.getMembers().get(0).equals(disbander.getUniqueId())) {
            PartyManager.removeParty(name);
        }
    }
    public static void invite(Player inviter, Player target,String partyName){
        
    }


}
