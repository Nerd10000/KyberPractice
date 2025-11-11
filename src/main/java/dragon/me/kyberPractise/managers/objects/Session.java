package dragon.me.kyberPractise.managers.objects;

public class Session {
    private final String requester;
    private final String target;
    private final String map;
    private final String kit;

    public Session(String requester, String target, String map, String kit) {
        this.requester = requester;
        this.target = target;
        this.map = map;
        this.kit = kit;
    }

    public String getRequester() {
        return requester;
    }

    public String getTarget() {
        return target;
    }

    public String getMap() {
        return map;
    }

    public String getKit() {
        return kit;
    }
}
