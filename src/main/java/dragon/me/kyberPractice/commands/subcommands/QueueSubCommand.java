package dragon.me.kyberPractice.commands.subcommands;

import dragon.me.kyberPractice.KyberPractice;
import dragon.me.kyberPractice.managers.objects.Queue;
import org.bukkit.entity.Player;


public final class QueueSubCommand {
    private QueueSubCommand() {


    }

    public static void create(String name, String kit, Player player) {
        Queue queue = new Queue(name, KyberPractice.kitDataManager.getKit(kit));
        KyberPractice.queueDataManager.saveData(queue);
        player.sendMessage(KyberPractice.messageSupplier.serializeString(
                KyberPractice.messageSupplier.getRawString("queue.create.success").replace("{queue}", name).replace("{kit}", kit),
                player
        ));

    }
    public static void join(String queueName, Player player){
        KyberPractice.queueDataManager.getQueue(queueName).addPlayer(player);
        player.sendMessage(KyberPractice.messageSupplier.serializeString(
                KyberPractice.messageSupplier.getRawString("queue.join.success").replace("{queue}", queueName),
                player
        ));
    }
}
