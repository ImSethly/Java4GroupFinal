import java.util.ArrayList;

/**
 * Creates Direction class
 * @author Ayoub, Eric, Seth
 */
public class Direction {

    private final String name;
    private String description;

    private boolean hasPath;
    private boolean isLocked;

    Direction(String name) {
        this.name = name;
        ArrayList<Item> items = new ArrayList<>();
    }

    public String getName() { return this.name; }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() { return this.description; }

    //add a msg description of an item if existed
    public void AddToDescription(String itemMsg){
        this.description = this.description + " " + itemMsg;
    }
    //remove a msg description of an item if removed
    public void RemoveFromDescription(String itemMsg){
        try {
            this.description = this.description.replace(itemMsg, "");
        }
        catch (RuntimeException r) {
            throw new RuntimeException("RunTime Error : " + r);
        }
        catch (Exception e){
            System.out.println("Exception: " + e);
        }
    }

    public boolean getHasPath() { return this.hasPath; }
    public void setHasPath(boolean hasPath) { this.hasPath = hasPath; }

    public boolean getIsLocked() { return this.isLocked; }
    public void setIsLocked(boolean isLocked) { this.isLocked = isLocked; }

}