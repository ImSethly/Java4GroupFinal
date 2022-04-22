import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Game implements java.io.Serializable {

    static Player player;
    static String curMsg;

    static ArrayList<Room> Map;

    boolean isTutorial = true;

    public static void main(String[] args) {

        // Setup game and create rooms
        Game game = new Game();

        // Starting Message
        System.out.println("Welcome player! We are in need of your help!\nThe Orcs have become rather distressed and we need your help calming them down.\nPlease locate all 6 Orcs and find a way to calm them down.");
        System.out.println(currentLocation());

        // Get user input
        Scanner userInput = new Scanner(System.in);
        while (userInput.hasNext()) {

            // Break the userInput into arguments
            String[] input = userInput.nextLine().toLowerCase().split(" ");

            if (input.length > 0 && input.length < 3) {
                // Execute Commands
                switch (input[0]) {
                    // Help command
                    case "help":
                        System.out.println("Valid Commands: look <direction>, walk <direction>, use <item>, pickup <item>, drop <item>, inventory");
                        break;
                    // Look command
                    case "look":
                        if (input.length == 2) {
                            switch (input[1]) {
                                case "north":
                                case "east":
                                case "south":
                                case "west":
                                    System.out.println(player.getRoom().GetDirection(input[1]).getDescription());
                                    break;
                                default:
                                    System.out.println("Invalid command!");
                                    break;
                            }
                        }
                        else {
                            System.out.println("Invalid Command. Try: look <direction>");
                        }
                        break;
                    // Walk command
                    case "walk":
                        if (input.length == 2) {
                            if (player.getRoom() == Map.get(0) && game.isTutorial) {
                                System.out.println("Please finish the tutorial before leaving the room.");
                            }
                            else {
                                switch (input[1]) {
                                    case "north":
                                    case "east":
                                    case "south":
                                    case "west":
                                        System.out.println(walkto(input[1]));
                                        break;
                                    default:
                                        System.out.println("Invalid command!");
                                        break;
                                }
                            }
                        }
                        else {
                            System.out.println("Invalid Command. Try: walk <direction>");
                        }
                        break;
                    // Use command
                    case "use":
                        if (input.length == 2) {
                            // TODO implement using an item
                        }
                        else {
                            System.out.println("Invalid Command. Try: use <item>");
                        }
                        break;
                    // Pickup command
                    case "pickup":
                        if (input.length == 2) {
                            switch (input[1]) {
                                case "apple":
                                    System.out.println(takeItem(input[1]));
                                    break;
                                default:
                                    System.out.println("Invalid command!");
                                    break;
                            }
                        }
                        else {
                            System.out.println("Invalid Command. Try: pickup <item>");
                        }
                        break;
                    // Drop command
                    case "drop":
                        if (input.length == 2) {
                            switch (input[1]) {
                                case "apple":
                                    System.out.println(dropItem(input[1]));
                                    break;
                                default:
                                    System.out.println("Invalid command!");
                                    break;
                            }
                        }
                        else {
                            System.out.println("Invalid Command. Try: drop <item>");
                        }
                        break;
                    // Inventory command
                    case "inventory":
                        System.out.println(player.getItems().describeItems());
                        break;
                    default:
                        System.out.println("Invalid command!");
                        break;
                }
            }
            else {
                System.out.println("Please only use one and two word commands.");
            }
        }

    }

    public Game() {

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
        Map.add(new Room("Room1", "Description1", room1List, 1));
        Map.add(new Room("Room2", "Description2", room2List, 2));
        Map.add(new Room("Room3", "Description3", room3List, 3));
        Map.add(new Room("Room4", "Description4", room4List, 4));
        Map.add(new Room("Room5", "Description5", room5List, 5));
        Map.add(new Room("Room6", "Description6", room6List, 6));
        Map.add(new Room("Room7", "Description7", room7List, 7));
        Map.add(new Room("Room8", "Description8", room8List, 8));

        // for each room in the map set directions
        for (Room room : Map) {
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
        player = new Player("Player", "A pro gamer", playerItemsList, Map.get(0));
        curMsg = "What do you want to do?";
    }

    //getting and setting the map
    public static ArrayList getMap() {
        return Map;
    }

    public void setMap(ArrayList aMap) {
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
    private static void moveItem(Item i, ItemList fromlst, ItemList tolst) {
        fromlst.remove(i);
        tolst.add(i);
    }

    //method to take item that return msg
    public static String takeItem(String itemname) {
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
    public static String dropItem(String itemname) {
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
    public static void movePlayerTo(Player p, Room room) {
        p.setRoom(room);
    }

    // Return which room you are at currently
    public static String currentLocation(){
        Room r = player.getRoom();
        return "You are in " + r.getName() + " which is " + r.getDescription();
    }

    //Unlock Path
    public static String unlockPath(Room r, String dir){
        String msg = "";
        if(r.GetDirection(dir).getHasPath()){
            if(!r.GetDirection(dir).getIsLocked()) {
                msg = dir + " direction of " + r.getName() + " is already unlocked!";
            }else{
                r.GetDirection(dir).setIsLocked(false);
                msg = dir + " direction of " + r.getName() + " is now unlocked!";
            }
        }else{
            msg = dir + " direction of " + r.getName() + " doesn't have a path";
        }
        return msg;
    }


    // TODO we still need to create a methode to move inside of the room
    public static String walkto(String dir) {
        String movemsg = "";
        Room r = player.getRoom();
        if (!player.getRoom().GetDirection(dir).getHasPath()) {
            movemsg = "There is no path to the " + dir + " !";
        } else
        {
            if (player.getRoom().GetDirection(dir).getIsLocked()) {
                movemsg = "There is a path to the " + dir + " but it is locked!";
            } else
            {
                switch (r.getId()) {
                    case 1:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "You are going to " + Map.get(1).getName();
                                movePlayerTo(player, Map.get(1));
                            }
                            case "east" -> {
                                movemsg = "You are going to " + Map.get(5).getName();
                                movePlayerTo(player, Map.get(5));
                            }
                            case "south" -> {
                                movemsg = "To the south, you see the portal that brought you here. You cannot access it";
                            }
                            case "west" -> {
                                movemsg = "You are going to " + Map.get(4).getName();
                                movePlayerTo(player, Map.get(4));
                            }
                        }
                        break;
                    case 2:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "To the north, there is woods too thick to pass. You cannot access it";
                            }
                            case "east" -> {
                                movemsg = "To the east, there is woods too thick to pass. You cannot access it";
                            }
                            case "south" -> {
                                movemsg = "You are going to " + Map.get(0).getName();
                                movePlayerTo(player, Map.get(0));
                            }
                            case "west" -> {
                                movemsg = "You are going to " + Map.get(2).getName();
                                movePlayerTo(player, Map.get(2));
                            }
                        }
                        break;
                    case 3:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "To the north, there is woods too thick to pass. You cannot access it";
                            }
                            case "east" -> {
                                movemsg = "You are going to " + Map.get(1).getName();
                                movePlayerTo(player, Map.get(1));
                            }
                            case "south" -> {
                                movemsg = "To the south, there is woods too thick to pass. You cannot access it";
                            }
                            case "west" -> {
                                movemsg = "To the west, there is woods too thick to pass. You cannot access it";
                            }
                        }
                        break;
                    case 4:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "To the north, there is woods too thick to pass. You cannot access it";
                            }
                            case "east" -> {
                                movemsg = "To the east, there is woods too thick to pass. You cannot access it";
                            }
                            case "south" -> {
                                movemsg = "You are going to " + Map.get(5).getName();
                                movePlayerTo(player, Map.get(5));
                            }
                            case "west" -> {
                                movemsg = "To the west, there is woods too thick to pass. You cannot access it";
                            }
                        }
                        break;
                    case 5:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "To the north , there is woods too thick to pass. You cannot access it";
                            }
                            case "east" -> {
                                movemsg = "You are going to " + Map.get(0).getName();
                                movePlayerTo(player, Map.get(0));
                            }
                            case "south" -> {
                                movemsg = "To the south, there is woods too thick to pass. You cannot access it";
                            }
                            case "west" -> {
                                movemsg = "To the west, there is woods too thick to pass. You cannot access it";
                            }
                        }
                        break;
                    case 6:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "You are going to " + Map.get(3).getName();
                                movePlayerTo(player, Map.get(3));
                            }
                            case "east" -> {
                                movemsg = "You are going to " + Map.get(6).getName();
                                movePlayerTo(player, Map.get(6));
                            }
                            case "south" -> {
                                movemsg = "To the south, there is woods too thick to pass. You cannot access it";
                            }
                            case "west" -> {
                                movemsg = "You are going to " + Map.get(0).getName();
                                movePlayerTo(player, Map.get(0));
                            }
                        }
                        break;
                    case 7:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "To the north , there is woods too thick to pass. You cannot access it";
                            }
                            case "east" -> {
                                movemsg = "To the east, there is woods too thick to pass. You cannot access it";
                            }
                            case "south" -> {
                                movemsg = "You are going to " + Map.get(7).getName();
                                movePlayerTo(player, Map.get(7));
                            }
                            case "west" -> {
                                movemsg = "You are going to " + Map.get(5).getName();
                                movePlayerTo(player, Map.get(5));
                            }
                        }
                        break;
                    case 8:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "You are going to " + Map.get(6).getName();
                                movePlayerTo(player, Map.get(6));
                            }
                            case "east" -> {
                                movemsg = "To the east, there is woods too thick to pass. You cannot access it";
                            }
                            case "south" -> {
                                movemsg = "Congrats!! you found your Exit !";
                            }
                            case "west" -> {
                                movemsg = "To the west, there is woods too thick to pass. You cannot access it";
                            }
                        }
                        break;

                }
            }
        }
        return movemsg;
    }
}