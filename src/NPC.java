/**
 * Creates an NPC character
 * @author Ayoub, Eric, Seth
 */
public class NPC extends ItemHolder implements java.io.Serializable {

    // Species of the NPC
    private String species;

    // Constructor to create an NPC
    public NPC(String species, String npcName, String description, ItemList il) {
        super(npcName, description, il);

        this.species = species;
    }

}
