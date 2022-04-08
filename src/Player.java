import java.util.ArrayList;

public class Player {

    private ArrayList<Item> inventory;
    private int position;

    Player(int position) {
        inventory = new ArrayList<>();
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
    public void setPosition(int val) {
        position = val;
    }

}