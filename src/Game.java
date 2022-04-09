import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    static Player player;
    static String curMsg;

    static ArrayList<Room> rooms;

    public static void main(String[] args) {

        // Setup game
        Setup();

        // Start
        GetInput(curMsg);

    }

    public static void Setup() {

        // instead of giving just the position of the room to the player
        //why dont we pass the whole room
        //player = new Player(0);

        //I see your point here but why dont we create a generic list of type room
        // more fun we can call it Map instead of rooms
        //extending from the map Idea we can create a map for each room called roomMap
        //rooms = new ArrayList<>();
        ArrayList<Room> Map ;
        Room r;

        //we can later use our imagination to give cool name and description for each room
        //foreach room declare and instantiate an ItemList
        ItemList room1List = new ItemList();
        ItemList room2List = new ItemList();
        ItemList room3List = new ItemList();
        ItemList room4List = new ItemList();
        ItemList room5List = new ItemList();

        //since the player can hold items in both his hand, we can consider him as an ItemHolder
        //and he needs Items list
        //Instantiate player Item list
        ItemList playerItemsList = new ItemList();

        //instantiate map
        Map = new ArrayList<Room>();

        //instantiate rooms
        //add rooms to the map
        //I changed the id counting where it starts with 1 instead of 0 so we can stay consistent
        Map.add(new Room("Room1","Description1",room1List,1));
        Map.add(new Room("Room2","Description2",room2List,2));
        Map.add(new Room("Room3","Description3",room3List,3));
        Map.add(new Room("Room4","Description4",room4List,4));
        Map.add(new Room("Room5","Description5",room5List,5));

        // for each room in the map set directions
        for (Room room: Map) {
            switch (room.getName().toLowerCase()) {
                case "room1":
                    room.SetDirection("North", "Theres a wall");
                    room.SetDirection("East", "Theres a door");
                    room.SetDirection("South", "Theres a window");
                    room.SetDirection("West", "Theres another wall");
                    break;
                case "room2":
                    room.SetDirection("North", "Theres a wall2");
                    room.SetDirection("East", "Theres a door2");
                    room.SetDirection("South", "Theres a window2");
                    room.SetDirection("West", "Theres another wall2");
                    break;
                case "room3":
                    room.SetDirection("North", "Theres a wall3");
                    room.SetDirection("East", "Theres a door3");
                    room.SetDirection("South", "Theres a window3");
                    room.SetDirection("West", "Theres another wall3");
                    break;
                case "room4":
                    room.SetDirection("North", "Theres a wall4");
                    room.SetDirection("East", "Theres a door4");
                    room.SetDirection("South", "Theres a window4");
                    room.SetDirection("West", "Theres another wall4");
                    break;
                case "room5":
                    room.SetDirection("North", "Theres a wall5");
                    room.SetDirection("East", "Theres a door5");
                    room.SetDirection("South", "Theres a window5");
                    room.SetDirection("West", "Theres another wall5");
                    break;

            }
        }


        //we don't need this since we set the rooms and direction up in the code
        //r = new Room("Room1","Description1",room1List,1);
        /*r.SetDirection("North", "Theres a wall");
        r.SetDirection("East", "Theres a door");
        r.SetDirection("South", "Theres a window");
        r.SetDirection("West", "Theres another wall");
        rooms.add(r);*/
        //r = new Room("Room2","Description2",room2List,2);
        //rooms.add(r);
        //r = new Room("Room3","Description3",room3List,3);
        //rooms.add(r);
        //r = new Room("Room4","Description4",room4List,4);
        //rooms.add(r);
        //r = new Room("Room5","Description5",room5List,5);
        //rooms.add(r);


        //add a player and place it in the first room
        player = new Player("Player","A pro gamer",playerItemsList,Map.get(0));
        curMsg = "What do you want to do?";
    }

    private static void ExecuteCommand(String input) {

        String[] args = input.split(" ");

        switch (args[0].toLowerCase()) {
            case "look":
                System.out.println("What you looking at");
                //TODO implement look command
                break;
            case "walk":
                System.out.println("Where you walkin");
                //TODO implement walk command
                break;
            default:
                System.out.println("Invalid command!");
                GetInput(curMsg);
                break;
        }
    }

    public static void GetInput(String msg) {
        Scanner obj = new Scanner(System.in);
        System.out.println(msg);

        ExecuteCommand(obj.nextLine());
    }

}