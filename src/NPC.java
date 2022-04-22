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

    //pre-set msgs
    private ArrayList<String> msgs;

    // Constructor to create an NPC
    public NPC(String species, Room room, String npcName, String description, ItemList il) {
        super(npcName, description, il);

        this.species = species;
        this.room = room;
    }
    public String getmsg(int index) {
        return msgs.get(index);
    }

    public void setmsg(String msg) {
        this.msgs.add(msg);
    }
}
