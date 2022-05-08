import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//each Item holder has a name and a description
public class ItemHolder extends Item implements java.io.Serializable {

    private ItemList items = new ItemList();

    //constructor for ItemHolder
    public ItemHolder(String aName, String aDescription, ItemList tl) {
        super(aName, aDescription);
        items = tl;
    }

    // return the list of items in the ItemHolder
    public ItemList getItems() {
        return items;
    }

    //set a list of items in the item holder
    public void setItems(ItemList things) {
        this.items = items;
    }


    public static class ItemSorter implements Comparator<Item>
    {

        @Override
        public int compare(Item item1, Item item2) {
            return item1.getName().compareToIgnoreCase( item2.getName() );
        }
    }
}