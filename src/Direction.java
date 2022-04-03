import java.util.ArrayList;

public class Direction {

    private String name;
    private String description;
    private ArrayList<Item> items;

    Direction(String name) {
        this.name = name;
        items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

}