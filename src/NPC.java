import java.util.ArrayList;

/**
 * Creates an NPC character
 * @author Ayoub, Eric, Seth
 */
public class NPC extends ItemHolder implements java.io.Serializable {

    // Species of the NPC
    private String species;

    //room where the NPC is at
    private Room room;

    //satisfied boolean
    private Boolean satisfied;

    //pre-set msg
    private final ArrayList<String> msg = new ArrayList<>();;

    // isFollowing
    private boolean isFollowing;

    // Constructor to create an NPC
    public NPC(String species, Room room, String npcName, String description, ItemList il, Boolean satisfied) {
        super(npcName, description, il);

        this.species = species;
        this.room = room;
        this.satisfied = satisfied;
        this.isFollowing = false;
    }
    public String getMsg(int index) {
        return msg.get(index);
    }

    public void setMsg(String msg) {
        this.msg.add(msg);
    }

    public Boolean getSatisfied() {
        return this.satisfied;
    }

    public void setSatisfied(Boolean satisfied) {
        this.satisfied = satisfied;
    }

    public String getSpecies() {
        return this.species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public boolean getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(boolean bool) {
        isFollowing = bool;
    }

    public Room getRoom() {
        return room;
    }
    public void setRoom(Room rm) {
        room = rm;
    }
}
