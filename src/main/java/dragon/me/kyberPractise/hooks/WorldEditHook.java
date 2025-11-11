package dragon.me.kyberPractise.hooks;

import dragon.me.kyberPractise.KyberPractise;

public class WorldEditHook {

    public WorldEditHook() {
        if (!KyberPractise.isWorldEditAvailable) {
            KyberPractise.instance.getLogger().warning("WorldEdit/FAWE is not installed, some features (like arena restoration) is NOT GOING TO WORK!");
        } else {
            KyberPractise.instance.getLogger().info("WorldEdit/FAWE is installed, enjoy the features!");
        }
    }
}
