import java.util.ArrayList;

public class Room {

    private final int id;
    private final ArrayList<Direction> directions;

    Room(int id) {
        this.id = id;

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