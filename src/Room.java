import java.util.ArrayList;

//A room itself can be an ItemHolder because it holds a list of Items
public class Room extends ItemHolder implements java.io.Serializable{

    private final int id;
    private final ArrayList<Direction> directions;

    //Constructor for the room
    public Room(String aName, String aDescription, ItemList il,int id) {
        super(aName,aDescription, il);
        this.id = id;

        //What do you guys think if creating an enum for directions
        directions = new ArrayList<>();
        Direction d;
        d = new Direction("North");
        directions.add(d);
        d = new Direction("East");
        directions.add(d);
        d = new Direction("South");
        directions.add(d);
        d = new Direction("West");
        directions.add(d);
    }

    public void SetDirection(String direction, String description) {
        for (Direction value : directions) {
            if (value.getName().equals(direction)) {
                value.setDescription(description);
            }
        }
    }

}