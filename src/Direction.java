import java.util.ArrayList;

public class Direction {

    private String name;
    private String description;
    private ArrayList<Item> items;

    private boolean hasPath;
    private boolean isLocked;

    Direction(String name) {
        this.name = name;
        items = new ArrayList<>();
    }

    public String getName() { return this.name; }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() { return this.description; }

    public boolean getHasPath() { return this.hasPath; }
    public void setHasPath(boolean hasPath) { this.hasPath = hasPath; }

    public boolean getIsLocked() { return this.isLocked; }
    public void setIsLocked(boolean isLocked) { this.isLocked = isLocked; }

}