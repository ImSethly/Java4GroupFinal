import java.util.ArrayList;

//a player can also be an item holder because he can hold stuff
public class Player extends ItemHolder{

    //if we consider a room as am actual ItemHolder why don't we leave the inventory there ?
    //private ArrayList<Item> inventory;


    //private int position;
    //I believe it would be nicer to add the whole room to the player class so he has access
    //to all the items inside of that room
    private Room room;

    public Player(String playerName, String Description, ItemList items, Room currentRoom) {
        super(playerName, Description, items);

        this.room = currentRoom;
    }

    public Room getRoom() {
        return room;
    }
    public void setRoom(Room rm) {
        room = rm;
    }

}