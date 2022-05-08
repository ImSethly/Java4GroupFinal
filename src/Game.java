
import org.apache.derby.jdbc.EmbeddedDataSource;

import java.io.*;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Game implements java.io.Serializable {

    // TODO implement an easter egg using date and time
    // TODO save stopwatch time to file as high score
    static final String gameTitle = "The Angry Woods";
    static Player player;
    static String curMsg;

    static ArrayList<Room> Map;
    static ArrayList<NPC> NPCs;

    static LocalTime starttime ;
    static LocalTime endtime ;

    static boolean isTutorial = true;

    static ArrayList<String> orcBuddies = new ArrayList<>();

    static boolean gameOver;

    static int totalMapItems;

    public static void main(String[] args) {

        // Setup game and create rooms
        Game game = new Game();

        starttime = LocalTime.now();


        // set connection for db
        String url = "jdbc:derby:zoo;create=true";
        Connection con = null;
        try {
            con = DriverManager.getConnection(url);
            //System.out.println("Connected to Db!");
            //dataBaseSetup(con);
            con.close();
        }catch (Exception e){
            System.out.println("Connection failed to Db!");
        }

        // Starting Message
        System.out.println("Welcome player, we are in need of your help! \nPlease locate all 6 Orcs and find a way to help them.\n Type Help to see all the valid commands");
        System.out.println(currentLocation());
        System.out.println(NPCs.get(0).getMsg(0));

        // Get user input

        try (Scanner userInput = new Scanner(System.in)) {
            System.out.print(">");
            while (userInput.hasNext() && !gameOver) {

                // Break the userInput into arguments
                String[] input = userInput.nextLine().toLowerCase().split(" ");

                // Assertion test for user input
                //assert input[0].equals("") : "Blank";

                // Execute Commands
                switch (input[0]) {
                    // Help command
                    case "help":
                        System.out.println("Valid Commands: look <direction>, walk <direction>, use <item>, pickup <item>, drop <item>, give <item>, solve <guess>, inventory, map, progress");
                        break;
                    // Look command
                    case "look":
                        if (input.length == 2) {
                            switch (input[1]) {
                                case "north", "up", "east", "right", "south", "down", "west", "left" -> System.out.println(player.getRoom().GetDirection(input[1]).getDescription());
                                default -> System.out.println("Invalid direction!");
                            }
                        } else {
                            System.out.println("Invalid Command. Try: look <direction>");
                        }
                        break;
                    // Walk command
                    case "walk":
                        if (input.length == 2) {
                            if (player.getRoom() == Map.get(0) && isTutorial) {
                                // Check if the ORC is satisfied, display different messages based on that
                                System.out.println("You must finish the tutorial before leaving the room.\n " + NPCs.get(0).getMsg(0));
                            } else {
                                switch (input[1]) {
                                    case "north", "up", "east", "right", "south", "down", "west", "left" -> System.out.println(WalkTo(input[1]));
                                    default -> System.out.println("Invalid direction!");
                                }
                            }
                        } else {
                            System.out.println("Invalid Command. Try: walk <direction>");
                        }
                        break;
                    // Use command
                    case "use":
                        if (input.length == 2) {
                            // remove key items from player inventory once it is used
                            switch (input[1]) {
                                case "flashlight", "book", "rope", "hint1", "hint2", "hint3", "hint4" -> System.out.println(useItem(input[1]));
                                default -> System.out.println("Invalid item name!");
                            }
                        } else {
                            System.out.println("Invalid Command. Try: use <item>");
                        }
                        break;
                    // Pickup command
                    case "pickup":
                        if (input.length == 2) {
                            switch (input[1]) {
                                case "glasses", "flashlight", "book", "rope", "hint1", "hint2", "hint3", "hint4", "robothead", "robotlegs", "robotbody" -> System.out.println(takeItem(input[1]));
                                default -> System.out.println("Invalid item name!");
                            }
                        } else {
                            System.out.println("Invalid Command. Try: pickup <item>");
                        }
                        break;
                    // Drop command
                    case "drop":
                        if (input.length == 2) {
                            switch (input[1]) {
                                case "glasses", "flashlight", "book", "rope", "robothead", "robotlegs", "robotbody" -> System.out.println(dropItem(input[1]));
                                default -> System.out.println("You cannot drop this item.");
                            }
                        } else {
                            System.out.println("Invalid Command. Try: drop <item>");
                        }
                        break;
                    // Give command
                    case "give":
                        if (input.length == 2) {
                            switch (input[1]) {
                                case "glasses", "robothead", "robotbody", "robotlegs" -> System.out.println(give(input[1]));
                                default -> System.out.println("Invalid item name!");
                            }

                        } else {
                            System.out.println("Invalid Command. Try: give <item>");
                        }
                        break;
                    // Inventory command
                    case "inventory":
                        System.out.println(player.getItems().describeItems());
                        break;
                    case "solve":
                        if (input.length == 2) {
                            System.out.println(checkRiddle(input[1]));
                        } else {
                            System.out.println("Invalid Command. Try: solve <guess>");
                        }
                        break;
                    case "progress":
                        System.out.println(progressCheck());
                        break;
                    case "map":
                        showMap();
                        break;
                    case "print":
                        if (input.length == 2) {
                            printAllfiles(input[1]);
                        } else {
                            System.out.println("Invalid Command. Try: print <path>");
                        }
                        break;
                    case "date":
                        System.out.println(getDate());
                        break;
                    case "test":
                        PrintOrcs();
                        System.out.println(getScores());
                        break;
                    default:
                        System.out.println("Invalid command!");
                        break;
                }

                if (!gameOver) {
                    System.out.print(">");
                }
            }
        } catch (Exception ex) {
            System.out.println("Error " + ex);
        }
    }

    public Game() {

        // Set gameOver boolean
        gameOver = false;

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

        // Item list for NPCs
        ItemList Orc1List = new ItemList();
        ItemList Orc2List = new ItemList();
        ItemList Orc3List = new ItemList();
        ItemList Orc4List = new ItemList();
        ItemList Orc5List = new ItemList();
        ItemList Orc6List = new ItemList();

        //add items to each room list of items
        room1List.add(new Item("Glasses", "Huge glasses that must belong to a huge creature!"));
        room1List.get(0).setMsg(", and there are huge glasses on the ground.");

        room2List.add(new Item("Flashlight", "A magically powered flashlight, can light even the darkest of rooms."));
        room2List.get(0).setMsg(", and there is a odd looking flashlight on the ground.");

        room3List.add(new Item("Hint1", "A hint, you need to use it in order to read it!"));
        room3List.get(0).setMsg(" There is a small piece of paper on the ground.(hint1)");
        room3List.add(new Item("RobotBody","A rusty robot body!"));
        room3List.get(1).setMsg(" There is a rusty robot body (RobotBody).");

        room4List.add(new Item("Hint2", "A hint, use it in order to read it!"));
        room4List.get(0).setMsg(" There is a small piece of paper on the ground.(hint2)");
        room4List.add(new Item("RobotLegs","Rusty robot legs!"));
        room4List.get(1).setMsg(" There are rusty robot legs (RobotLegs).");

        room5List.add(new Item("Rope", "A long sturdy rope that can easily hold your body weight."));
        room5List.get(0).setMsg(" there is a long rope.");
        room5List.add(new Item("Hint3", "A hint, use it in order to read it!"));
        room5List.get(1).setMsg(" There is a small piece of paper on the ground.(hint3)");

        room6List.add(new Item("RobotHead","A rusty robot head!"));
        room6List.get(0).setMsg(" there is a rusty robot head (RobotHead).");

        room7List.add(new Item("Book", "An old book, looks too boring to read."));
        room7List.get(0).setMsg(" there is a dusty old book.");

        room8List.add(new Item("Hint4", "A hint, use it in order to read it!"));
        room8List.get(0).setMsg(" There is a small piece of paper on the ground.(hint4)");

        // instantiate map
        Map = new ArrayList<Room>();

        // instantiate orc list
        NPCs = new ArrayList<NPC>();

        // instantiate rooms
        Map.add(new Room("Portal Room", "A room containing the magical portal that brought you here.", room1List, 1));
        Map.add(new Room("Library", "A small library filled with dusty, old books.", room2List, 2));
        Map.add(new Room("Workshop", "Old broken tools, rusty workbenches, and the smell of oil fills the room.", room3List, 3));
        Map.add(new Room("Swamp", "Mosquitoes and a rotten stench permeate the air. Better not stick around too long.", room4List, 4));
        Map.add(new Room("Dining Room", "An ORC sits at the head of a large dinner table. It seems as if he is awaiting company.", room5List, 5));
        Map.add(new Room("Class Room", "Desks, chalk, blackboards, overly optimistic poster phrases! this must be a class room.", room6List, 6));
        Map.add(new Room("Cliff", "A large cliff, be careful near the ledge!", room7List, 7));
        Map.add(new Room("Campsite", "A campsite next to a beautiful shimmering blue stream.", room8List, 8));

        // for each room in the map set directions
        for (Room room : Map) {
            switch (room.getName().toLowerCase()) {
                case "portal room" -> {
                    room.SetDirection("north", "To the north, there is an ugly green creature, and an open path.", true, true);
                    room.SetDirection("east", "To the east, there is an open path.", true, true);
                    room.SetDirection("south", "To the south, you see the portal that brought you here. Walk south to leave and end game.", true, false);
                    room.SetDirection("west", "To the west, there is a dark tunnel, too dark to pass without light source.", true, true);
                }
                case "library" -> {
                    room.SetDirection("north", "To the north, you see woods too thick to pass.", false, false);
                    room.SetDirection("east", "To the east, you see woods too thick to pass.", false, false);
                    room.SetDirection("south", "To the south, there is an open path", true, false);
                    room.SetDirection("west", "To the west, there is a tall bookshelf filled with books. It appears to be missing a book..", true, true);
                }
                case "workshop" -> {
                    room.SetDirection("north", "To the north, you see woods too thick to pass. It seems also to be a robot piece lying around.", false, false);
                    room.SetDirection("east", "To the east, there is open path.", true, false);
                    room.SetDirection("south", "To the south, you see woods too thick to pass.", false, false);
                    room.SetDirection("west", "To the west, there is an ugly green creature.", false, false);
                }
                case "swamp" -> {
                    room.SetDirection("north", "To the north, you see woods too thick to pass.", false, false);
                    room.SetDirection("east", "To the east, you see woods too thick to pass.", false, false);
                    room.SetDirection("south", "To the south, there is an open path.", true, false);
                    room.SetDirection("west", "To the west, you see woods too thick to pass.", false, false);
                }
                case "dining room" -> {
                    room.SetDirection("north", "To the north, there is an ugly green creature.", false, false);
                    room.SetDirection("east", "To the east, there is an open path.", true, false);
                    room.SetDirection("south", "To the south, you see woods too thick to pass. ", false, false);
                    room.SetDirection("west", "To the west, you see woods too thick to pass.", false, false);
                }
                case "class room" -> {
                    room.SetDirection("north", "To the north, there is an ugly green creature, and an open path.", true, false);
                    room.SetDirection("east", "To the east, there is an open path.", true, false);
                    room.SetDirection("south", "To the south, you see woods too thick to pass.", false, false);
                    room.SetDirection("west", "To the west, there is an open path.", true, false);
                }
                case "cliff" -> {
                    room.SetDirection("north", "To the north, you see woods too thick to pass.", false, false);
                    room.SetDirection("east", "To the east, you see woods too thick to pass.", false, false);
                    room.SetDirection("south", "To the south, there is a steep cliff. You might need help getting down to the bottom.", true, true);
                    room.SetDirection("west", "To the west, there is an open path.", true, false);
                }
                case "campsite" -> {
                    room.SetDirection("north", "To the north, you see woods too thick to pass.", true, false);
                    room.SetDirection("east", "To the east, there is an ugly green creature.", false, false);
                    room.SetDirection("south", "To the south, you see woods too thick to pass.", false, false);
                    room.SetDirection("west", "To the west, you see woods too thick to pass.", false, false);
                }
            }
        }

        //Instantiate NPCs
        NPCs.add(new NPC("ORC",Map.get(0),"Durz(ORC1)","ORCDescription1",Orc1List, false));
        NPCs.add(new NPC("ORC",Map.get(2),"Igug(ORC2)","ORCDescription2",Orc2List,false));
        NPCs.add(new NPC("ORC",Map.get(3),"Nar(ORC3)","ORCDescription3",Orc3List, false));
        NPCs.add(new NPC("ORC",Map.get(4),"Vatu(ORC4)","ORCDescription4",Orc4List, false));
        NPCs.add(new NPC("ORC",Map.get(5),"Bor(ORC5)","ORCDescription5",Orc5List, false));
        NPCs.add(new NPC("ORC",Map.get(7),"Ugak(ORC6)","ORCDescription6",Orc6List, false));

        //add a list of messages for each ORC
        for (NPC npc: NPCs) {
            switch (npc.getName().toLowerCase()) {
                case "durz(orc1)" -> {
                    npc.setMsg(npc.getName() + ": Ugh-hh! I can't find my glasses to read my favorite book. If you want to go deeper into the woods, you have to find my glasses and return them to me!");
                    npc.setMsg(npc.getName() + ": Thank you, finally I can read again! My book says that you can now leave the room, try it out!");
                }
                case "igug(orc2)" -> {
                    npc.setMsg(npc.getName() + ": Please help! My toy robot is broken and I can't find all of the pieces... Please find all of the pieces and return them to me!");
                    npc.setMsg(npc.getName() + ": You found all of the missing parts! Dreams really do come true! Thank you for bringing happiness back into my life.");
                    npc.setMsg(npc.getName() + ": Thanks for giving me one of the pieces I need, but we are not done yet. Look around for more pieces!");
                    npc.setMsg(npc.getName() + ": I can't make use of this piece yet. Find a different one first.");
                }
                case "nar(orc3)" -> {
                    npc.setMsg(npc.getName() + ": I'm afraid of the dark, please help me find my way home!");
                    npc.setMsg(npc.getName() + ": Thank you for helping me get back home. The dark isn't so scary when I'm with a friend!");
                    npc.setMsg(npc.getName() + ": This doesn't look like my home... Lets keep looking!");
                }
                case "vatu(orc4)" -> {
                    npc.setMsg(npc.getName() + ": Play with me! Throughout the forest are scattered hints to solve my riddle. Find the hints and piece them together!");
                    npc.setMsg(npc.getName() + ": You figured out the answer to my master riddle, congratulations! Thanks for playing with me. Next time I'll have to make an even harder riddle!");
                    npc.setMsg(npc.getName() + ": Nice try! Think about it more and try again!");
                }
                case "bor(orc5)" -> {
                    npc.setMsg(npc.getName() + ": I'll never be happy unless I can figure out this math problem... What are the first 3 digits of pi??");
                    npc.setMsg(npc.getName() + ": You're really smart, thank you for helping me. Now I can go home and eat the good kind of pie, yum!");
                    npc.setMsg(npc.getName() + ": Nice try! Think about it more and guess again!");
                }
                case "ugak(orc6)" -> {
                    npc.setMsg(npc.getName() + ": I don't trust you, human. Tell me the names of all of my ORC comrades, then maybe we can be friends.");
                    npc.setMsg(npc.getName() + ": Ok you got them all right! I believe you now, maybe we can be friends."); // completion
                    npc.setMsg(npc.getName() + ": You already gave that name. Tell me another one."); // already gave the name
                    npc.setMsg(npc.getName() + ": That's not one of my friends! Try again."); // not a name
                    npc.setMsg(npc.getName() + ": That's right! Tell me another one."); // is a name
                }
            }
        }

        //add the item messages to the directions of the rooms
        Map.get(0).GetDirection("south").AddToDescription(room1List.get(0).getMsg());
        Map.get(1).GetDirection("south").AddToDescription(room2List.get(0).getMsg());
        Map.get(2).GetDirection("south").AddToDescription(room3List.get(0).getMsg());
        Map.get(2).GetDirection("north").AddToDescription(room3List.get(1).getMsg());
        Map.get(3).GetDirection("east").AddToDescription(room4List.get(0).getMsg());
        Map.get(3).GetDirection("north").AddToDescription(room4List.get(1).getMsg());
        Map.get(3).GetDirection("west").AddToDescription(" and there is an ugly green creature.");
        Map.get(4).GetDirection("east").AddToDescription(room5List.get(1).getMsg());
        Map.get(4).GetDirection("south").AddToDescription(room5List.get(0).getMsg());
        Map.get(5).GetDirection("south").AddToDescription(room6List.get(0).getMsg());
        Map.get(6).GetDirection("west").AddToDescription(room7List.get(0).getMsg());
        Map.get(7).GetDirection("west").AddToDescription(room8List.get(0).getMsg());

        // Add OrcBuddies to Array
        orcBuddies.add("durz");
        orcBuddies.add("igug");
        orcBuddies.add("nar");
        orcBuddies.add("vatu");
        orcBuddies.add("bor");

        //add a player and place it in the first room
        player = new Player("Player", "A pro gamer", playerItemsList, Map.get(0));
        curMsg = "What do you want to do?";

        // Count total items at beginning
        totalMapItems = CountMapItems();

        //create scores file
        /*try {
            File file = new File("SCORE.txt");
            if(file.createNewFile()){
                System.out.println("file created!");
            }
        }catch(Exception ex){

        }*/
    }

    public static void dataBaseSetup(Connection con){


        try {
            EmbeddedDataSource ds = new EmbeddedDataSource();
            ds.setDatabaseName("AdventureGame");


            Statement stmt = con.createStatement();
            //if(tableExists(con, "room")){
                stmt.executeUpdate("DROP TABLE room");
            //}
            stmt.executeUpdate("CREATE TABLE room" +
                    "(id INTEGER PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "description VARCHAR(530))");

            for(Room r : Map) {
                String strQuery= "INSERT INTO room VALUES("+r.getId()+ ", '" + r.getName()+"', '"+r.getDescription()+"')";
                stmt.executeUpdate(strQuery);
            }

            ResultSet rs = stmt.executeQuery("SELECT id, name, description FROM room");

            while(rs.next()){
                System.out.println("Room Name" + rs.getString("name"));
                System.out.println("Room description" + rs.getString("description"));
            }

        }catch (Exception e){
            System.out.println("DB error!!!");
        }
    }
    public static boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }

    public static String getScores() throws IOException {
        List<String> scores = new ArrayList<>();
        String msg = "";
        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader("SCORE.txt"));

            String line;
            while ((line = br.readLine()) != null) {
                scores.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }
        for (String score : scores){
            msg+= score;
        }
        return msg;
    }

    public static void PrintOrcs(){
        //create a file locally
        try {
        File myFile = new File("ORCNames.txt");
            if(!myFile.createNewFile()){
                myFile.delete();
                myFile = new File("ORCNames.txt");
            }
            //write into the file created
            FileWriter myWriter = new FileWriter(myFile.getName());
            for(NPC orc : NPCs){
                myWriter.write(orc.getName()+ "\n");
            }
            myWriter.close();

            //read everything from the file created
            FileReader myReader = new FileReader(myFile.getName());
            int i;
            while((i=myReader.read())!=-1)
                System.out.print((char)i);
            myReader.close();

            System.out.println("Successfully wrote to the file.");


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static LocalDate getDate(){
        LocalDate date = LocalDate.now();
        MonthDay Christmas = MonthDay.of(12,25);
        if(date.getMonthValue() == Christmas.getMonthValue() && date.getDayOfMonth() == Christmas.getDayOfMonth()){
            System.out.println("Merry Christmas!");
        }
        return date;
    }
    public void testIntMatching() {
        List<Integer> intList = Arrays.asList(2, 4, 5, 6, 8);

        boolean allEven = intList.stream().allMatch(i -> i % 2 == 0);
        boolean oneEven = intList.stream().anyMatch(i -> i % 2 == 0);
        boolean noneMultipleOfThree = intList.stream().noneMatch(i -> i % 3 == 0);

    }

    //this methode will:
    // Programmatically traverse your directory and print out the name and size of all files
    //Programmatically retrieve a subdirectory and list out all the contents of that directory to the console
    //Choose a file in your directory and print out the contents of that file to the console
    public static void printAllfiles(String path){
        boolean check = Map.get(0).getItems().getItem("glasses").whatever.test("");
        //get the directory
        File dir = new File(path);
        FileWriter fWriter;
        boolean first = true;
        try{
            for(File f: dir.listFiles()){
                if(f.isDirectory()){
                    for(File file : f.listFiles()){
                        if(file.isDirectory()){
                            System.out.println("folder "+ file.getName());
                        }else{
                            System.out.println("file "+ file.getName());
                        }
                    }
                }else{
                    System.out.println("file name is "+ f.getName()+ " with a size of "+ f.length()+ " bytes!");

                }
                if(f.isFile() && first){

                    FileInputStream fis = new FileInputStream(f);
                    int oneByte;
                    while ((oneByte = fis.read()) != -1) {
                        System.out.write(oneByte);
                    }
                    System.out.flush();
                }
            }
        }catch(Exception ex){}
    }
    public static int CountMapItems() {
        int total = 0;

        for (Room r : Map) {
            total += r.getItems().size();
        }

        return total;
    }

    // Check if all Orcs are satisfied
    public static void EndGame() {
        boolean allSatisfied = true;
        endtime = LocalTime.now();
        Duration durationOfPlay = Duration.between(starttime,endtime);
        for(NPC npc : NPCs) {
            if (!npc.getSatisfied()) {
                allSatisfied = false;
            }
        }
        long sec= durationOfPlay.getSeconds();
        String timeplayed = timePlayed(sec);
        // Set gameOver to true
        gameOver = true;

        // Stats message
        String stats;
        stats = String.format("%s\nYou collected %s out of %s items!", progressCheck(), totalMapItems - CountMapItems(), totalMapItems);

        // Send out stats message
        if (allSatisfied) {
            System.out.println("Congratulations, you have helped all of the Orcs! Now they can live in peace once again.\n" + stats + "\nThank you for playing " + gameTitle + "!\n"+ timeplayed);
            try {
                FileWriter Fwriter = new FileWriter("SCORE.txt");
                Fwriter.write("\n"+ timeplayed);
                Fwriter.close();

            }catch (Exception ex){
                //ignore
            }
        }
        else {
            System.out.printf("Better luck next time.\n%s\nThanks for playing %s!\n%s", stats, gameTitle,timeplayed);
        }

        System.exit(0);
    }
    public static String timePlayed(long sec){
        String msg="";
        long seconds;
        long min;
        long minutes;
        long hours;
        if(sec < 60 ){
            msg = "You Played a total of " + sec+ " seconds!";
        }else if(sec< 3600){
            min = sec / 60;
            seconds = sec%60;
            msg = "You Played a total of " + min + " minutes and "+ seconds + " seconds!";
        } else {
            min = sec / 60;
            seconds = sec%60;
            hours = min / 60;
            minutes = min%60;
            msg = "You Played a total of " + hours + " hours and "+ minutes + " minutes and "+ seconds + " seconds!";
        }
        return msg;
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
    private static void moveItem(Item i, ItemList fromLst, ItemList toLst) {
        fromLst.remove(i);
        toLst.add(i);
    }

    public static void showMap(){
        String r1="  ",r2="  ",r3="  ",r4="  ",r5="  ",r6="  ",r7="  ",r8="  ";
        switch (player.getRoom().getId()) {
            case 1 -> r1 = "**";
            case 2 -> r2 = "**";
            case 3 -> r3 = "**";
            case 4 -> r4 = "**";
            case 5 -> r5 = "**";
            case 6 -> r6 = "**";
            case 7 -> r7 = "**";
            case 8 -> r8 = "**";
        }
            System.out.println(" ---Room3---        ---Room2---       ---Room4---");
            System.out.println("|    "+ r3 +"     |------|    "+r2+"     |     |     "+r4+"    |");
            System.out.println(" -----------        -----------       -----------");
            System.out.println("                          |                |");
            System.out.println(" ---Room5---        ---Room1---       ---Room6---       ---Room7---");
            System.out.println("|    "+ r5 +"     |------|    "+r1+"     |-----|     "+r6+"    |-----|     "+r7+"    |");
            System.out.println(" -----------        -----------       -----------       -----------");
            System.out.println("                                                            |");
            System.out.println("                                                        ---Room8---");
            System.out.println("                                                       |     "+r8+"    |");
            System.out.println("                                                        -----------");
    }
    //method to take item that return msg
    public static String takeItem(String itemName) {
        String takemsg = "";
        //get item named or return null if it doesn't exist
        Item i = player.getRoom().getItems().getItem(itemName);

        //if item is null
        if (i == null) {
            takemsg = itemName + " is not here";
        } else {
            EditDirDescription(itemName);
            moveItem(i, player.getRoom().getItems(), player.getItems());
            takemsg = "you just got " + itemName + " !";

        }

        return takemsg;
    }
    public static  String progressCheck(){
        String msg = "";
        int complete = 0;
        for(NPC npc : NPCs){
            if (npc.getSpecies().equals("ORC")){
                if(npc.getSatisfied()){
                    complete++;
                }
            }

        }
        msg = "You've successfully helped " + complete + " out of "+ NPCs.size() + " Orcs!";

        return  msg;
    }
    //method to remove the item msg from direction description once item is picked up
    private static void EditDirDescription(String itemName){
        switch (itemName.toLowerCase()){
            case "glasses":
                for (Item item: player.getRoom().getItems()) {
                    if(item.getName().equalsIgnoreCase("glasses")){
                        player.getRoom().GetDirection("south").RemoveFromDescription(item.getMsg());
                    }
                }
                break;
            case "flashlight":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("flashlight")) {
                        player.getRoom().GetDirection("south").RemoveFromDescription(item.getMsg());
                    }
                }
                break;
            case "book":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("book")) {
                        player.getRoom().GetDirection("west").RemoveFromDescription(item.getMsg());
                    }
                }
                break;
            case "rope":
                for (Item item: player.getRoom().getItems()) {
                    if(item.getName().equalsIgnoreCase("rope")){
                        player.getRoom().GetDirection("south").RemoveFromDescription(item.getMsg());
                    }
                }
                break;
            case "hint1":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("hint1")) {
                        player.getRoom().GetDirection("south").RemoveFromDescription(item.getMsg());
                    }
                }
                break;
            case "hint2":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("hint2")) {
                        player.getRoom().GetDirection("east").RemoveFromDescription(item.getMsg());
                    }
                }
                break;
            case "hint3":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("hint3")) {
                        player.getRoom().GetDirection("east").RemoveFromDescription(item.getMsg());
                    }
                }
                break;
            case "hint4":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("hint4")) {
                        player.getRoom().GetDirection("west").RemoveFromDescription(item.getMsg());
                    }
                }
                break;
            case "robothead":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("RobotHead")) {
                        player.getRoom().GetDirection("south").RemoveFromDescription(item.getMsg());
                    }
                }
                break;
            case "robotlegs":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("RobotLegs")) {
                        player.getRoom().GetDirection("north").RemoveFromDescription(item.getMsg());
                    }
                }
                break;
            case "robotbody":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("RobotBody")) {
                        player.getRoom().GetDirection("north").RemoveFromDescription(item.getMsg());
                    }
                }
                break;
        }
    }
    //method to add the item msg to direction description once item is dropped off
    private static void AddToDirDescription(String itemName){
        switch (itemName.toLowerCase()) {
            case "glasses":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("glasses")) {
                        player.getRoom().GetDirection("south").AddToDescription(item.getMsg());
                    }
                }
                break;
            case "flashlight":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("flashlight")) {
                        player.getRoom().GetDirection("south").AddToDescription(item.getMsg());
                    }
                }
                break;
            case "book":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("book")) {
                        player.getRoom().GetDirection("south").AddToDescription(item.getMsg());
                    }
                }
                break;
            case "rope":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("rope")) {
                        player.getRoom().GetDirection("south").AddToDescription(item.getMsg());
                    }
                }
                break;
            case "robothead":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("robothead")) {
                        player.getRoom().GetDirection("south").AddToDescription(item.getMsg());
                    }
                }
                break;
            case "robotlegs":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("robotlegs")) {
                        player.getRoom().GetDirection("south").AddToDescription(item.getMsg());
                    }
                }
                break;
            case "robotbody":
                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("robotBody")) {
                        player.getRoom().GetDirection("south").AddToDescription(item.getMsg());
                    }
                }
                break;
        }
    }

    //method to use item that return msg
    public static String useItem(String itemName) {
        String usemsg = "";
        //get item named or return null if it doesn't exist
        Item i = player.getItems().getItem(itemName);

        //if item is null
        if (i == null) {
            usemsg = itemName + " isn't in your inventory.";
        } else {
            switch (itemName){
                case "flashlight":
                    if (Map.get(0).equals(player.getRoom())) {
                        unlockPath(player.getRoom(), "west");
                        usemsg = "Used " + itemName + " ! The flashlight lights the way to the west in " + player.getRoom().getName();
                    } else {
                        usemsg = "You cannot use " + itemName + " in " + player.getRoom().getName();
                    }
                    break;
                case "rope":
                    //Check if player is in either room 7 or room 8 else you cannot use rope
                    if (Map.get(6).equals(player.getRoom()))
                    {
                        unlockPath(player.getRoom(), "south");
                        player.getItems().remove(i);
                        usemsg = "You just used " + itemName + "! you can now climb down to the base of the cliff!";
                    }else
                    {
                        usemsg = "You cannot use " + itemName + "here!";
                    }
                    break;
                case "book":
                    if(Map.get(1).equals(player.getRoom())){
                        unlockPath(player.getRoom(), "west");
                        player.getItems().remove(i);
                        usemsg = "You placed the " + itemName + " on the book shelf. The book shelf rotates revealing a hidden path way";
                    }else{
                        usemsg = "This book is written in ORCish, I can't read it!";
                    }
                    break;
                case "hint1":
                    usemsg = "Hint1 : It has a tail!";
                    break;
                case "hint2":
                    usemsg = "Hint2 : It has four legs!";
                    break;
                case "hint3":
                    usemsg = "Hint3 : It is a mammal!";
                    break;
                case "hint4":
                    usemsg = "Hint4 : It barks!";
                    break;
            }


        }
        return usemsg;
    }
    //method to check if the answer for the riddle is correct
    public static String checkRiddle(String guess){
        String msg = "";
        if(player.getRoom().equals(Map.get(4))){
            if(guess.toLowerCase().equals("dog")){
                msg = NPCs.get(3).getMsg(1);
                NPCs.get(3).setSatisfied(true);
            }else
            {
                msg = NPCs.get(3).getMsg(2);
            }
        }else if(player.getRoom().equals((Map.get(5)))){
            if(guess.equals("3.14")){
                msg = NPCs.get(4).getMsg(1);
                NPCs.get(4).setSatisfied(true);
            }else
            {
                msg = NPCs.get(4).getMsg(2);
            }
        } else if (player.getRoom().equals((Map.get(7)))) {
            if (!NPCs.get(5).getSatisfied()) {
                switch (guess.toLowerCase()) {
                    case "durz":
                        if (orcBuddies.contains("durz")) {
                            orcBuddies.remove("durz");
                            msg = NPCs.get(5).getMsg(4);
                            if (checkBuddiesList(orcBuddies)) {
                                msg = NPCs.get(5).getMsg(1);
                                NPCs.get(5).setSatisfied(true);
                            }
                        } else {
                            msg = NPCs.get(5).getMsg(2);
                        }
                        break;
                    case "igug":
                        if (orcBuddies.contains("igug")) {
                            orcBuddies.remove("igug");
                            msg = NPCs.get(5).getMsg(4);
                            if (checkBuddiesList(orcBuddies)) {
                                msg = NPCs.get(5).getMsg(1);
                                NPCs.get(5).setSatisfied(true);
                            }
                        } else {
                            msg = NPCs.get(5).getMsg(2);
                        }
                        break;
                    case "nar":
                        if (orcBuddies.contains("nar")) {
                            orcBuddies.remove("nar");
                            msg = NPCs.get(5).getMsg(4);
                            if (checkBuddiesList(orcBuddies)) {
                                msg = NPCs.get(5).getMsg(1);
                                NPCs.get(5).setSatisfied(true);
                            }
                        } else {
                            msg = NPCs.get(5).getMsg(2);
                        }
                        break;
                    case "vatu":
                        if (orcBuddies.contains("vatu")) {
                            orcBuddies.remove("vatu");
                            msg = NPCs.get(5).getMsg(4);
                            if (checkBuddiesList(orcBuddies)) {
                                msg = NPCs.get(5).getMsg(1);
                                NPCs.get(5).setSatisfied(true);
                            }
                        } else {
                            msg = NPCs.get(5).getMsg(2);
                        }
                        break;
                    case "bor":
                        if (orcBuddies.contains("bor")) {
                            orcBuddies.remove("bor");
                            msg = NPCs.get(5).getMsg(4);
                            if (checkBuddiesList(orcBuddies)) {
                                msg = NPCs.get(5).getMsg(1);
                                NPCs.get(5).setSatisfied(true);
                            }
                        } else {
                            msg = NPCs.get(5).getMsg(2);
                        }
                        break;
                    default:
                        msg = NPCs.get(5).getMsg(3);
                        break;
                }
            }
            else {
                msg = "There is no riddle in this room that needs solving.";
            }
        } else{
            msg = "There is no riddle in this room that needs solving.";
        }

        return msg;
    }

    public static boolean checkBuddiesList(ArrayList<String> list) {
        boolean isEmpty;

        isEmpty = list.size() == 0;

        return isEmpty;
    }

    //method to drop item that return msg
    public static String dropItem(String itemName) {
        String dropmsg = "";
        Item i = player.getItems().getItem(itemName);
        if (itemName.equals("")) {
            dropmsg = "Which Item you would like to drop?";
        }

        if (i == null) {
            dropmsg = itemName + " is not here.";
        } else {

            moveItem(i, player.getItems(), player.getRoom().getItems());
            dropmsg = "you just dropped " + itemName + " !";
            AddToDirDescription(itemName);
        }
        return dropmsg;
    }
    //method to give item to NPC of that room
    public static String give(String itemName) {
        String givemsg ="";

        //get item named or return null if it doesn't exist
        Item i = player.getItems().getItem(itemName);

        if (itemName.equals("")) {
            givemsg = "Which Item you would like to give?";
        }
        if (i == null) {
            givemsg = itemName + " is not here";
        } else
        {
            switch(player.getRoom().getId()){
                case 1:
                    if ("glasses".equals(itemName.toLowerCase())) {
                        moveItem(i, player.getItems(), NPCs.get(0).getItems());
                        unlockPath(player.getRoom(), "north");
                        unlockPath(player.getRoom(), "east");
                        NPCs.get(0).setSatisfied(true);
                        isTutorial = false;
                        givemsg = "You handed " + itemName + " to " + NPCs.get(0).getName() + "\n " + NPCs.get(0).getMsg(1);
                    } else {
                        givemsg = "This Orc doesn't need " + itemName + "!";
                    }
                    break;
                case 2:
                    givemsg = "";
                    break;

                case 3:
                    switch (itemName.toLowerCase()){
                        case "robothead":
                            if(NPCs.get(1).getItems().size()>0){
                                for(Item item : NPCs.get(1).getItems()){
                                    if(item.getName().equalsIgnoreCase("RobotBody"))
                                    {
                                        moveItem(i,player.getItems(), NPCs.get(1).getItems());
                                        NPCs.get(1).setSatisfied(true);
                                        givemsg = "You handed "+ itemName + " to " + NPCs.get(1).getName() +"\n " + NPCs.get(1).getMsg(1);
                                        break;
                                    }else{
                                        givemsg = NPCs.get(1).getMsg(3);
                                    }
                                }
                            }else{
                                givemsg = NPCs.get(1).getMsg(3);
                            }

                            break;
                        case "robotbody":
                            if(NPCs.get(1).getItems().size()>0){
                                for(Item item : NPCs.get(1).getItems()){
                                    if(item.getName().equalsIgnoreCase("RobotLegs"))
                                    {
                                        moveItem(i,player.getItems(), NPCs.get(1).getItems());
                                        givemsg = "You handed "+ itemName + " to " + NPCs.get(1).getName() +"\n " + NPCs.get(1).getMsg(2);
                                        break;
                                    }else{
                                        givemsg = NPCs.get(1).getMsg(3);
                                    }

                                }

                            }else{
                                givemsg = NPCs.get(1).getMsg(3);
                            }
                            break;
                        case "robotlegs":

                            moveItem(i,player.getItems(), NPCs.get(1).getItems());
                            givemsg = "You handed "+ itemName + " to " + NPCs.get(1).getName() +"\n " + NPCs.get(1).getMsg(2);
                            break;
                        default:
                            givemsg = "This Orc doesn't need "+ itemName + "!";
                            break;
                    }
                    break;
                case 4:
                    givemsg = "This Orc doesn't need "+ itemName + "!";
                    break;

                case 5:
                    givemsg = "This Orc doesn't need "+ itemName + "!";;
                    break;
                case 6:
                    givemsg = "This Orc doesn't need "+ itemName + "!";;
                    break;
                case 7:
                    givemsg = "";
                    break;
                case 8:
                    givemsg = "This Orc doesn't need "+ itemName + "!";;
                    break;


            }

        }
        return givemsg;
    }



    //moving the player from one room to another
    public static void movePlayerTo(Player p, Room room) {
        p.setRoom(room);
    }

    //moving the player from one room to another
    public static void moveNPCTo(NPC n, Room room) {
        n.setRoom(room);
    }

    // Return which room you are at currently
    public static String currentLocation(){
        Room r = player.getRoom();
        return "You are in " + r.getName() + " which is " + r.getDescription();
    }

    //Unlock Path
    public static void unlockPath(Room r, String dir){
        String msg = "";
        if(r.GetDirection(dir).getHasPath()){
            if(!r.GetDirection(dir).getIsLocked()) {
                msg = dir + " direction of " + r.getName() + " is already unlocked!";
            }else{
                r.GetDirection(dir).setIsLocked(false);
                msg = dir + " direction of " + r.getName() + " is now unlocked!";
            }
        }else{
            msg = dir + " direction of " + r.getName() + " doesn't have a path.";
        }
    }

    //move inside the room
    public static String WalkTo(String dir) {
        String movemsg = "";

        switch (dir) {
            case "up" -> dir = "north";
            case "down" -> dir = "south";
            case "left" -> dir = "west";
            case "right" -> dir = "east";
        }

        Room r = player.getRoom();
        if (!player.getRoom().GetDirection(dir).getHasPath()) {
            movemsg = "There is no path to the " + dir + " !";
        } else
        {
            if (player.getRoom().GetDirection(dir).getIsLocked()) {
                movemsg = "There is a path to the " + dir + " but it is locked!";
            } else
            {
                // Add message for Orc 3 to tell you if that room is orcs home or not
                String tMsg;
                if (NPCs.get(2).getIsFollowing()) {
                    tMsg = "\n" + NPCs.get(2).getMsg(2);
                }
                else {
                    tMsg = "";
                }

                switch (r.getId()) {
                    case 1:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "You are going to the " + Map.get(1).getName() + tMsg;
                                movePlayerTo(player, Map.get(1));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(1));
                                }
                            }
                            case "east" -> {
                                if(!NPCs.get(4).getSatisfied()){
                                    movemsg = "You are going to the " + Map.get(5).getName() + tMsg + "\n" + NPCs.get(4).getMsg(0);
                                }else
                                {
                                    movemsg = "You are going to the " + Map.get(5).getName() + tMsg;
                                }
                                movePlayerTo(player, Map.get(5));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(5));
                                }
                            }
                            case "south" -> {
                                EndGame();
                            }
                            case "west" -> {
                                if (NPCs.get(2).getIsFollowing()) {
                                    // Set orc3 message, isSatisfied, and isFollowing when it arrives home
                                    tMsg = "\n" + NPCs.get(2).getMsg(1);
                                    NPCs.get(2).setSatisfied(true);
                                    NPCs.get(2).setIsFollowing(false);
                                }

                                if(!NPCs.get(3).getSatisfied()){
                                    movemsg = "You are going to the " + Map.get(4).getName() + tMsg + "\n" + NPCs.get(3).getMsg(0);
                                }else{
                                    movemsg = "You are going to the " + Map.get(4).getName() + tMsg;
                                }
                                movePlayerTo(player, Map.get(4));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(4));
                                }
                            }
                        }
                        break;
                    case 2:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "To the north, there is woods too thick to pass.";
                            }
                            case "east" -> {
                                movemsg = "To the east, there is woods too thick to pass.";
                            }
                            case "south" -> {
                                movemsg = "You are going to the " + Map.get(0).getName() + tMsg;
                                movePlayerTo(player, Map.get(0));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(0));
                                }
                            }
                            case "west" -> {
                                if(!NPCs.get(1).getSatisfied()){
                                    movemsg = "You are going to the " + Map.get(2).getName() + tMsg + "\n" + NPCs.get(1).getMsg(0);
                                }else {
                                    movemsg = "You are going to the " + Map.get(2).getName() + tMsg;
                                }

                                movePlayerTo(player, Map.get(2));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(2));
                                }
                            }
                        }
                        break;
                    case 3:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "To the north, there is woods too thick to pass.";
                            }
                            case "east" -> {
                                movemsg = "You are going to the " + Map.get(1).getName() + tMsg;
                                movePlayerTo(player, Map.get(1));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(1));
                                }
                            }
                            case "south" -> {
                                movemsg = "To the south, there is woods too thick to pass.";
                            }
                            case "west" -> {
                                movemsg = "To the west, there is woods too thick to pass.";
                            }
                        }
                        break;
                    case 4:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "To the north, there is woods too thick to pass.";
                            }
                            case "east" -> {
                                movemsg = "To the east, there is woods too thick to pass.";
                            }
                            case "south" -> {
                                if(!NPCs.get(4).getSatisfied()){
                                    movemsg = "You are going to the " + Map.get(5).getName() + tMsg + "\n" + NPCs.get(4).getMsg(0);
                                }else
                                {
                                    movemsg = "You are going to the " + Map.get(5).getName() + tMsg;
                                }

                                movePlayerTo(player, Map.get(5));
                                if (NPCs.get(2).getIsFollowing()) {
                                    Map.get(3).GetDirection("west").RemoveFromDescription(" and there is an ugly green creature.");
                                    moveNPCTo(NPCs.get(2), Map.get(5));
                                }
                            }
                            case "west" -> {
                                movemsg = "To the west, there is woods too thick to pass.";
                            }
                        }
                        break;
                    case 5:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "To the north , there is woods too thick to pass.";
                            }
                            case "east" -> {
                                movemsg = "You are going to the " + Map.get(0).getName() + tMsg;
                                movePlayerTo(player, Map.get(0));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(0));
                                }
                            }
                            case "south" -> {
                                movemsg = "To the south, there is woods too thick to pass.";
                            }
                            case "west" -> {
                                movemsg = "To the west, there is woods too thick to pass.";
                            }
                        }
                        break;
                    case 6:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                if(!NPCs.get(2).getSatisfied() && !NPCs.get(2).getIsFollowing()){
                                    movemsg = "You are going to the " + Map.get(3).getName()+ "\n" + NPCs.get(2).getMsg(0);
                                    NPCs.get(2).setIsFollowing(true);
                                }
                                else {
                                    movemsg = "You are going to the " + Map.get(3).getName() + tMsg;
                                }

                                movePlayerTo(player, Map.get(3));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(3));
                                }
                            }
                            case "east" -> {
                                movemsg = "You are going to the " + Map.get(6).getName() + tMsg;
                                movePlayerTo(player, Map.get(6));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(6));
                                }
                            }
                            case "south" -> {
                                movemsg = "To the south, there is woods too thick to pass.";
                            }
                            case "west" -> {
                                movemsg = "You are going to the " + Map.get(0).getName() + tMsg;
                                movePlayerTo(player, Map.get(0));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(0));
                                }
                            }
                        }
                        break;
                    case 7:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "To the north , there is woods too thick to pass.";
                            }
                            case "east" -> {
                                movemsg = "To the east, there is woods too thick to pass.";
                            }
                            case "south" -> {
                                if(!NPCs.get(5).getSatisfied()){
                                    movemsg = "You are going to the " + Map.get(7).getName() + tMsg + "\n" + NPCs.get(5).getMsg(0);
                                }else{
                                    movemsg = "You are going to the " + Map.get(7).getName() + tMsg;
                                }

                                movePlayerTo(player, Map.get(7));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(7));
                                }
                            }
                            case "west" -> {
                                if (!NPCs.get(4).getSatisfied()) {
                                    movemsg = "You are going to the " + Map.get(5).getName() + tMsg + "\n" + NPCs.get(4).getMsg(0);
                                }
                                else {
                                    movemsg = "You are going to the " + Map.get(5).getName() + tMsg;
                                }
                                movePlayerTo(player, Map.get(5));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(5));
                                }
                            }
                        }
                        break;
                    case 8:
                        switch (dir.toLowerCase()) {
                            case "north" -> {
                                movemsg = "You are going to the " + Map.get(6).getName() + tMsg;
                                movePlayerTo(player, Map.get(6));
                                if (NPCs.get(2).getIsFollowing()) {
                                    moveNPCTo(NPCs.get(2), Map.get(6));
                                }
                            }
                            case "east" -> {
                                movemsg = "To the east, there is woods too thick to pass.";
                            }
                            case "south" -> {
                                movemsg = "You walk through the portal and find yourself back at home in your bed!";
                            }
                            case "west" -> {
                                movemsg = "To the west, there is woods too thick to pass.";
                            }
                        }
                        break;

                }
            }
        }
        return movemsg;
    }

    static class Player extends ItemHolder {

        //if we consider a room as am actual ItemHolder why don't we leave the inventory there ?
        //private ArrayList<Item> inventory;

        //private int position;
        //I believe it would be nicer to add the whole room to the player class to give it access
        //to all the items inside that room
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
}