import java.util.function.Predicate;

/**
 * Creates Item class
 * Any game object is an Item
 * @author Ayoub, Eric, Seth
 */


public class Item {

    private String name;
    private String description;
    private String msg;
    public Predicate<String> whatever = String::isEmpty;

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

    //get item msg
    public String getMsg() {
        return msg;
    }

    //set item msg
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString(){
        return this.name +": "+ this.description ;
    }

}