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
        player = new Player(0);

        rooms = new ArrayList<>();

        Room r;

        r = new Room(0);
        r.SetDirection("North", "Theres a wall");
        r.SetDirection("East", "Theres a door");
        r.SetDirection("South", "Theres a window");
        r.SetDirection("West", "Theres another wall");
        rooms.add(r);

        r = new Room(1);
        rooms.add(r);

        r = new Room(2);
        rooms.add(r);

        r = new Room(3);
        rooms.add(r);

        r = new Room(4);
        rooms.add(r);

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