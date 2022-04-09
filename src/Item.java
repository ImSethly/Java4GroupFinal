public class Item {

    private String name;
    private String description;

    //Item constructor
    public Item(String name, String aDescription) {
        this.name = name;
        this.description = aDescription;
    }

    //get item name
    public String getName() {
        return name;
    }

    //set item name
    public void setName(String aName) {
        this.name = aName;
    }

    //get item description
    public String getDescription() {
        return description;
    }

    //set item description
    public void setDescription(String aDescription) {
        this.description = aDescription;
    }
}