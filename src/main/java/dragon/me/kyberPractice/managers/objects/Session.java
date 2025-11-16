package dragon.me.kyberPractice.managers.objects;

import dragon.me.kyberPractice.managers.objects.enums.GameState;

public class Session {
    private final String requester;
    private final String target;
    private final String map;
    private final String kit;
    private  GameState state;

    public Session(String requester, String target, String map, String kit) {
        this.requester = requester;
        this.target = target;
        this.map = map;
        this.kit = kit;
        state = GameState.STARTING;
    }
    public void setState(GameState state) {
        this.state = state;
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
    public  GameState getState() {
        return state;
    }
}
