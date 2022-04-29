import java.util.ArrayList;

//A room itself can be an ItemHolder because it holds a list of Items
public class Room extends ItemHolder implements java.io.Serializable {

    private final int id;
    private final ArrayList<Direction> directions;

    //Constructor for the room
    public Room(String aName, String aDescription, ItemList il,int id) {
        super(aName,aDescription, il);
        this.id = id;

        //What do you guys think if creating an enum for directions
        directions = new ArrayList<>();
        Direction d;
        d = new Direction("north");
        directions.add(d);
        d = new Direction("east");
        directions.add(d);
        d = new Direction("south");
        directions.add(d);
        d = new Direction("west");
        directions.add(d);
    }

    public void SetDirection(String direction, String description, boolean hasPath, boolean isLocked) {
        for (Direction value : directions) {
            if (value.getName().equals(direction)) {
                value.setDescription(description);
                value.setHasPath(hasPath);
                value.setIsLocked(isLocked);
            }
        }
    }

    public Direction GetDirection(String direction) {
        Direction dir = null;

        switch (direction) {
            case "up" -> direction = "north";
            case "down" -> direction = "south";
            case "left" -> direction = "west";
            case "right" -> direction = "east";
        }

        for (Direction d : directions) {
            if (d.getName().equals(direction.toLowerCase())) {
                dir = d;
            }
        }

        return dir;
    }

    public int getId() { return this.id; }

}