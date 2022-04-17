

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

    // Get item list in the form of a string
    public String getInventory() {
        StringBuilder inv = new StringBuilder();
        for (Item i: items) {
            inv.append(" ").append(i.getName());
        }
        return inv.toString().trim();
    }

    //set a list of items in the item holder
    public void setItems(ItemList things) {
        this.items = items;
    }
}