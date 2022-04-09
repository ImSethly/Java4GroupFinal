

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
}