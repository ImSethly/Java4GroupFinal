import java.util.ArrayList;


public class ItemList extends ArrayList<Item> implements java.io.Serializable{

    //If there is any item in ItemList display all item's name and description
    public String describeItems() {
        StringBuilder s = new StringBuilder();
        if (this.size() == 0) {
            s = new StringBuilder("nothing.\n");
        } else {
            for (Item t : this) {
                s.append(t.getName()).append(": ").append(t.getDescription()).append("\n");
            }
        }
        return s.toString();
    }

    //return the item selected based on the name of the item
    public Item getitem(String aName) {
        Item anItem = null;
        String itemName = "";
        String aNameLowCase = aName.trim().toLowerCase();
        for (Item t : this) {
            itemName = t.getName().trim().toLowerCase();
            if (itemName.equals(aNameLowCase)) {
                anItem = t;
            }
        }
        return anItem;
    }
}