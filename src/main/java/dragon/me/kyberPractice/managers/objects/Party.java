package dragon.me.kyberPractice.managers.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Party {
    private final String name;
    private final List<UUID> members = new ArrayList<>();
    private final List<UUID> pendingMemberList = new ArrayList<>();

    public Party(String name) {
        this.name = name;
    }

    public Party(String name, UUID creator) {
        this(name);
        if (creator != null) {
            this.members.add(creator);
        }
    }

    public String getName() {
        return name;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void addMember(UUID member) {
        if (member == null) return;
        if (!members.contains(member)) members.add(member);
    }

    public void removeMember(UUID member) {
        if (member == null) return;
        members.remove(member);
    }

    public void addPendingMember(UUID member) {
        if (member == null) return;
        if (!pendingMemberList.contains(member)) pendingMemberList.add(member);
    }

    public void removePendingMember(UUID member) {
        if (member == null) return;
        pendingMemberList.remove(member);
    }

    public List<UUID> getPendingMemberList() {
        return pendingMemberList;
    }

    public void movePendingToMembers(UUID member) {
        if (member == null) return;
        if (pendingMemberList.contains(member)) {
            pendingMemberList.remove(member);
            addMember(member);
        }
    }

}