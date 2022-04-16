import java.util.ArrayList;
import java.util.Scanner;

public class Game implements java.io.Serializable {

    static Player player;
    static String curMsg;

    static ArrayList<Room> rooms;
    ArrayList<Room> Map ;
    public static void main(String[] args) {

        // Setup game and create rooms
        Game game = new Game();

        // Get user input
        Scanner userInput = new Scanner(System.in);
        while (userInput.hasNext()) {

            // Break the userInput into arguments
            String[] input = userInput.nextLine().toLowerCase().split(" ");

            // Parsing method here
            if (input.length != 2) {
                System.out.println("Please use 2 word commands only.");
            }
            else {
                // Execute Commands
                switch (input[0]) {
                    case "look":
                        switch(input[1]) {
                            case "north":
                            case "east":
                            case "south":
                            case "west":
                                System.out.println(player.getRoom().GetDirection(input[1]).getDescription());
                                break;
                        }
                        break;
                    case "walk":
                        switch(input[1]) {
                            // TODO implement moving
                            case "north":
                            case "east":
                            case "south":
                            case "west":
                        }
                        break;
                    case "use":
                    case "pickup":
                    case "drop":
                    default:
                        System.out.println("Invalid command!");
                        break;
                }
            }
        }

    }

    public  Game() {

        // Item lists for each room
        ItemList room1List = new ItemList();
        ItemList room2List = new ItemList();
        ItemList room3List = new ItemList();
        ItemList room4List = new ItemList();
        ItemList room5List = new ItemList();
        ItemList room6List = new ItemList();
        ItemList room7List = new ItemList();
        ItemList room8List = new ItemList();

        // Item list for player
        ItemList playerItemsList = new ItemList();

        // instantiate map
        Map = new ArrayList<Room>();

        // instantiate rooms
        Map.add(new Room("Room1","Description1",room1List,1));
        Map.add(new Room("Room2","Description2",room2List,2));
        Map.add(new Room("Room3","Description3",room3List,3));
        Map.add(new Room("Room4","Description4",room4List,4));
        Map.add(new Room("Room5","Description5",room5List,5));
        Map.add(new Room("Room6","Description6",room6List,6));
        Map.add(new Room("Room7","Description7",room7List,7));
        Map.add(new Room("Room8","Description8",room8List,8));

        // for each room in the map set directions
        for (Room room: Map) {
            switch (room.getName().toLowerCase()) {
                case "room1":
                    room.SetDirection("north", "To the north, there is an ugly green creature, and an open path.", true, true);
                    room.SetDirection("east", "To the east, there is an open path.", true, true);
                    room.SetDirection("south", "To the south, you see the portal that brought you here.", false, false);
                    room.SetDirection("west", "To the west, there is a dark tunnel.", true, true);
                    break;
                case "room2":
                    room.SetDirection("north", "To the north, you see woods too thick to pass.", false, false);
                    room.SetDirection("east", "To the east, you see woods too thick to pass.", false, false);
                    room.SetDirection("south", "To the south, there is a flashlight on the ground, and an open path.", true, false);
                    room.SetDirection("west", "To the west, there is a tall bookshelf filled with books. It appears to be missing a book..", true, true);
                    break;
                case "room3":
                    room.SetDirection("north", "To the north, you see woods too thick to pass.", false, false);
                    room.SetDirection("east", "To the east, there is open path.", true, false);
                    room.SetDirection("south", "To the south, you see woods too thick to pass.", false, false);
                    room.SetDirection("west", "To the west, there is an ugly green creature.", false, false);
                    break;
                case "room4":
                    room.SetDirection("north", "To the north, you see woods too thick to pass.", false, false);
                    room.SetDirection("east", "To the east, you see woods too thick to pass.", false, false);
                    room.SetDirection("south", "To the south, there is an open path.", true, false);
                    room.SetDirection("west", "To the west, there is an ugly green creature.", false, false);
                    break;
                case "room5":
                    room.SetDirection("north", "To the north, there is an ugly green creature.", false, false);
                    room.SetDirection("east", "To the east, there is an open path.", true, false);
                    room.SetDirection("south", "To the south, there is long rope.", false, false);
                    room.SetDirection("west", "To the west, you see woods too thick to pass.", false, false);
                    break;
                case "room6":
                    room.SetDirection("north", "To the north, there is an ugly green creature, and an open path.", true, false);
                    room.SetDirection("east", "To the east, there is an open path.", true, false);
                    room.SetDirection("south", "To the south, you see woods too thick to pass.", false, false);
                    room.SetDirection("west", "To the west, there is an open path.", true, false);
                    break;
                case "room7":
                    room.SetDirection("north", "To the north, you see woods too thick to pass.", false, false);
                    room.SetDirection("east", "To the east, you see woods too thick to pass.", false, false);
                    room.SetDirection("south", "To the south, there is a steep cliff.", true, true);
                    room.SetDirection("west", "To the west, there is an old book, and an open path.", true, false);
                    break;
                case "room8":
                    room.SetDirection("north", "To the north, you see woods too thick to pass.", true, false);
                    room.SetDirection("east", "To the east, there is an ugly green creature.", false, false);
                    room.SetDirection("south", "To the south, you see woods too thick to pass.", false, false);
                    room.SetDirection("west", "To the west, you see woods too thick to pass.", false, false);
                    break;

            }
        }

        //add a player and place it in the first room
        player = new Player("Player","A pro gamer",playerItemsList,Map.get(0));
        curMsg = "What do you want to do?";
    }

    //getting and setting the map
    ArrayList getMap() {
        return Map;
    }
    void setMap(ArrayList aMap) {
        Map = aMap;
    }

    //getting and setting the player
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player aPlayer) {
        player = aPlayer;
    }

    // take and drop items
    private void moveItem(Item i, ItemList fromlst, ItemList tolst) {
        fromlst.remove(i);
        tolst.add(i);
    }

    //method to take item that return msg
    public String takeItem(String itemname) {
        String takemsg = "";
        //get item named or return null if it doesn't exist
        Item i = player.getRoom().getItems().getItem(itemname);

        //if there is no name of the item specified
        if (itemname.equals("")) {
            itemname = "nameless object";
        }

        //if item is null
        if (i == null) {
            takemsg = itemname + " is not here";
        } else {
            moveItem(i, player.getRoom().getItems(), player.getItems());
            takemsg = "you just got " + itemname + " !";
        }
        return takemsg;
    }

    //method to drop item that return msg
    public String dropItem(String itemname) {
        String dropmsg = "";
        Item i = player.getItems().getItem(itemname);
        if (itemname.equals("")) {
            dropmsg = "Can you be more specific! which Item you would like to drop?";
        }

        if (i == null) {
            dropmsg = itemname + " is not here";
        } else {
            moveItem(i, player.getItems(), player.getRoom().getItems());
            dropmsg = "you just dropped " + itemname + " !";
        }
        return dropmsg;
    }

    //moving the player from one room to another
    void movePlayerTo(Player p, Room room) {
        p.setRoom(room);
    }

    // TODO we still need to create a methode to move inside of the room

}