import java.util.*;

public class Game implements java.io.Serializable {

    static Player player;
    static String curMsg;

    static ArrayList<Room> Map;
    static ArrayList<NPC> NPCS;

    static boolean isTutorial = true;

    public static void main(String[] args) {

        // Setup game and create rooms
        Game game = new Game();

        // Starting Message
        System.out.println("Welcome player! We are in need of your help! \nPlease locate all 6 Orcs and find a way to calm them down.\n Type Help to see all the valid commands");
        System.out.println(currentLocation());
        System.out.println(NPCS.get(0).getmsg(0));

        // Get user input
        Scanner userInput = new Scanner(System.in);

        System.out.print(">");
        while (userInput.hasNext()) {

            // Break the userInput into arguments
            String[] input = userInput.nextLine().toLowerCase().split(" ");

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
                                case "north":
                                case "east":
                                case "south":
                                case "west":
                                    System.out.println(player.getRoom().GetDirection(input[1]).getDescription());
                                    break;
                                default:
                                    System.out.println("Invalid direction!");
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
                                //TODO: Check if we the ORC is satisfied, display different messages based on that
                                System.out.println("Please finish the tutorial before leaving the room.\n "+ NPCS.get(0).getmsg(0));
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
                                        System.out.println("Invalid direction!");
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
                            // TODO remove key items from player inventory once it is used
                            switch (input[1]) {
                                case "flashlight", "book", "rope", "hint1", "hint2", "hint3", "hint4" -> System.out.println(useItem(input[1]));
                                default -> System.out.println("Invalid item name!");
                            }
                        }
                        else {
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
                        }
                        else {
                            System.out.println("Invalid Command. Try: pickup <item>");
                        }
                        break;
                    // Drop command
                    case "drop":
                        if (input.length == 2) {
                            switch (input[1]) {
                                case "glasses", "flashlight", "book", "rope", "robothead","robotlegs", "robotbody":
                                    System.out.println(dropItem(input[1]));
                                    break;
                                default:
                                    System.out.println("You cannot drop this item. Invalid item name!");
                                    break;
                            }
                        }
                        else {
                            System.out.println("Invalid Command. Try: drop <item>");
                        }
                        break;
                    // Give command
                    case "give":
                        if (input.length == 2) {
                            switch (input[1]) {
                                case "glasses", "robothead", "robotbody", "robotlegs":
                                    System.out.println(give(input[1]));
                                    break;
                                default:
                                    System.out.println("Invalid item name!");
                                    break;
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
                        if (input.length == 2)
                        {
                            System.out.println(checkRiddle(input[1]));
                        }
                        else
                        {
                            System.out.println("Invalid Command. Try: solve <guess>");
                        }
                        break;
                    // implement a command to check the progress
                    case "progress":
                        System.out.println(progressCheck());
                        break;

                    case "map":
                        showMap();
                        break;

                    default:
                        System.out.println("Invalid command!");
                        break;
                }


            System.out.print(">");
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

        // Item list for NPC's
        ItemList Orc1List = new ItemList();
        ItemList Orc2List = new ItemList();
        ItemList Orc3List = new ItemList();
        ItemList Orc4List = new ItemList();
        ItemList Orc5List = new ItemList();
        ItemList Orc6List = new ItemList();

        //add items to each room list of items
        room1List.add(new Item("Glasses", "Huge glasses seems to belong to a huge creature"));
        room1List.get(0).setmsg(", and there are glasses on the ground that you can pick");

        room2List.add(new Item("Flashlight", "A magically powered flashlight, can light even the darkest of rooms."));
        room2List.get(0).setmsg(", and there is a flashlight on the ground");

        room3List.add(new Item("Hint1", "A hint you need to use it in order to read it!"));
        room3List.get(0).setmsg(" There is a small piece of paper laying on the ground.(hint1)");
        room3List.add(new Item("RobotBody","Rusty robot body!"));
        room3List.get(1).setmsg(" There is a rusty robot body (RobotBody)");

        room4List.add(new Item("Hint2", "A hint you need to use it in order to read it!"));
        room4List.get(0).setmsg(" There is a small piece of paper laying on the ground.(hint2)");
        room4List.add(new Item("RobotLegs","Rusty robot legs!"));
        room4List.get(1).setmsg(" There are rusty robot legs (RobotLegs)");

        room5List.add(new Item("Rope", "A long sturdy rope that can easily hold your body weight"));
        room5List.get(0).setmsg(" there is long rope.");
        room5List.add(new Item("Hint3", "A hint you need to use it in order to read it!"));
        room5List.get(1).setmsg(" There is a small piece of paper laying on the ground.(hint3)");

        room6List.add(new Item("RobotHead","A rusty robot head!"));
        room6List.get(0).setmsg(" there is a rusty robot head (RobotHead)");

        room7List.add(new Item("Book", "An old book, looks too boring to read."));
        room7List.get(0).setmsg(" there is an old book");

        room8List.add(new Item("Hint4", "A hint you need to use it in order to read it!"));
        room8List.get(0).setmsg(" There is a small piece of paper laying on the ground.(hint4)");

        //TODO:add items to player if applicable

        //TODO:add items to Orcs if applicable

        // instantiate map
        Map = new ArrayList<Room>();

        // instantiate orc list
        NPCS = new ArrayList<NPC>();

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
                    room.SetDirection("south", "To the south, you see the portal that brought you here", false, false);
                    room.SetDirection("west", "To the west, there is a dark tunnel, too dark to pass without light source.", true, true);
                    break;
                case "room2":
                    room.SetDirection("north", "To the north, you see woods too thick to pass.", false, false);
                    room.SetDirection("east", "To the east, you see woods too thick to pass.", false, false);
                    room.SetDirection("south", "To the south, there is an open path", true, false);
                    room.SetDirection("west", "To the west, there is a tall bookshelf filled with books. It appears to be missing a book..", true, true);
                    break;
                case "room3":
                    room.SetDirection("north", "To the north, you see woods too thick to pass. It seems also to be a robot piece laying around", false, false);
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
                    room.SetDirection("south", "To the south,  you see woods too thick to pass. ", false, false);
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
                    room.SetDirection("south", "To the south, there is a steep cliff. You might need help getting down to the bottom", true, true);
                    room.SetDirection("west", "To the west,there is an open path.", true, false);
                    break;
                case "room8":
                    room.SetDirection("north", "To the north, you see woods too thick to pass.", true, false);
                    room.SetDirection("east", "To the east, there is an ugly green creature.", false, false);
                    room.SetDirection("south", "To the south, you see woods too thick to pass.", false, false);
                    room.SetDirection("west", "To the west, you see woods too thick to pass.", false, false);
                    break;

            }
        }

        //Instantiate NPCS
        NPCS.add(new NPC("ORC",Map.get(0),"Durz(ORC1)","ORCDescription1",Orc1List, false));
        NPCS.add(new NPC("ORC",Map.get(2),"Igug(ORC2)","ORCDescription2",Orc2List,false));
        NPCS.add(new NPC("ORC",Map.get(3),"Nar(ORC3)","ORCDescription3",Orc3List, false));
        NPCS.add(new NPC("ORC",Map.get(4),"Vatu(ORC4)","ORCDescription4",Orc4List, false));
        NPCS.add(new NPC("ORC",Map.get(5),"Bor(ORC5)","ORCDescription5",Orc5List, false));
        NPCS.add(new NPC("ORC",Map.get(7),"Ugak(ORC6)","ORCDescription6",Orc6List, false));

        //add a list of msgs for each ORC
        for (NPC npc: NPCS) {
            switch (npc.getName().toLowerCase()){
                case "durz(orc1)":
                    npc.setmsg(npc.getName() + ": Ugh-hh! I can't find my glasses to read my favorite book. If you want to pass to the next level you have to look around, find my glasses, and return them to me!");
                    npc.setmsg(npc.getName() + ": Thank you, finally I can read again! My book says that you can now leave the room, try it out!");
                    break;
                case "igug(orc2)":
                    npc.setmsg(npc.getName() + ": Please help! My toy robot is broken and I can't find all of the pieces... Please find all of the pieces and return them to me!");
                    npc.setmsg(npc.getName() + ": You found all of the missing parts! Dreams really do come true! Thank you for bringing happiness back into my life once more.");
                    npc.setmsg(npc.getName() + ": Thanks for giving me one of the pieces I need, but we are not done yet. Look around for more pieces!");
                    npc.setmsg(npc.getName() + ": I can't make use of this piece yet. Find a different one first.");
                    break;
                case "nar(orc3)":
                    npc.setmsg(npc.getName() + ": I'm afraid of the dark, can you help me find my way home? I'd appreciate it greatly!");
                    npc.setmsg(npc.getName() + ": Thank you for guiding me through the dark. Darkness is much friendlier with a friend... and a flashlight!");
                    break;
                case "vatu(orc4)":
                    npc.setmsg(npc.getName() + ": Play with me! Throughout the forest are scattered hints to solve my riddle. Find the hints and piece them together (metaphorically)!");
                    npc.setmsg(npc.getName() + ": You figured out the answer to my master riddle, congratulations! Thanks for playing with me. Next time I'll have to make an even hard one!");
                    npc.setmsg(npc.getName() + ": Nice try! Think about it more and guess again!");
                    break;
                case "bor(orc5)":
                    npc.setmsg(npc.getName() + ": I'll never be happy unless I can figure out this math problem... What are the first 3 digits of pi??");
                    npc.setmsg(npc.getName() + ": You're really smart, thank you for helping me. Now I can go home and eat the good kind of pie, yum!");
                    npc.setmsg(npc.getName() + ": Nice try! Think about it more and guess again!");
                    break;
                case "ugak(orc6)":
                    npc.setmsg(npc.getName() + ": I don't trust you, human. Tell me the names of all of my orc comrades, then maybe we can be friends.");
                    npc.setmsg(npc.getName() + ": You're pretty nice for a human. I suppose we can be friends. Ugak is happy.");
                    break;
            }
        }

        //add the item messages to the directions of the rooms
        Map.get(0).GetDirection("south").AddToDescription(room1List.get(0).getmsg());
        Map.get(1).GetDirection("south").AddToDescription(room2List.get(0).getmsg());
        Map.get(2).GetDirection("south").AddToDescription(room3List.get(0).getmsg());
        Map.get(2).GetDirection("north").AddToDescription(room3List.get(1).getmsg());
        Map.get(3).GetDirection("east").AddToDescription(room4List.get(0).getmsg());
        Map.get(3).GetDirection("north").AddToDescription(room4List.get(1).getmsg());
        Map.get(4).GetDirection("east").AddToDescription(room5List.get(1).getmsg());
        Map.get(4).GetDirection("south").AddToDescription(room5List.get(0).getmsg());
        Map.get(5).GetDirection("south").AddToDescription(room6List.get(0).getmsg());
        Map.get(6).GetDirection("west").AddToDescription(room7List.get(0).getmsg());
        Map.get(7).GetDirection("west").AddToDescription(room8List.get(0).getmsg());



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

    public static void showMap(){
        String r1="  ",r2="  ",r3="  ",r4="  ",r5="  ",r6="  ",r7="  ",r8="  ";
        switch (player.getRoom().getId()){
            case 1:
                r1 = "**";
                break;
            case 2:
                r2 = "**";
                break;
            case 3:
                r3 = "**";
                break;
            case 4:
                r4 = "**";
                break;
            case 5:
                r5 = "**";
                break;
            case 6:
                r6 = "**";
                break;
            case 7:
                r7= "**";
                break;
            case 8:
                r8 = "**";
                break;
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
    public static String takeItem(String itemname) {
        String takemsg = "";
        //get item named or return null if it doesn't exist
        Item i = player.getRoom().getItems().getItem(itemname);

        //if item is null
        if (i == null) {
            takemsg = itemname + " is not here";
        } else {
            ReditDirDescription(itemname);
            moveItem(i, player.getRoom().getItems(), player.getItems());
            takemsg = "you just got " + itemname + " !";

        }

        return takemsg;
    }
    public static  String progressCheck(){
        String msg = "";
        int complete = 0;
        for(NPC npc : NPCS){
            if (npc.getSpecies().equals("ORC")){
                if(npc.getsatisfied()){
                    complete++;
                }
            }

        }
        msg = "You've successfully helped " + complete + " out of "+ NPCS.size() + " Orcs!";

        return  msg;
    }
    //method to remove the item msg from direction description once item is picked up
    private static void ReditDirDescription(String itemname){
        switch (itemname.toLowerCase()){
            case "glasses":

                    for (Item item: player.getRoom().getItems()) {
                        if(item.getName().equalsIgnoreCase("glasses")){
                            player.getRoom().GetDirection("south").RemoveFromDescription(item.getmsg());
                        }
                    }


                break;
            case "flashlight":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("flashlight")) {
                            player.getRoom().GetDirection("south").RemoveFromDescription(item.getmsg());
                        }
                    }

                break;
            case "book":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("book")) {
                            player.getRoom().GetDirection("west").RemoveFromDescription(item.getmsg());
                        }
                    }

                break;
            case "rope":

                    for (Item item: player.getRoom().getItems()) {
                        if(item.getName().equalsIgnoreCase("rope")){
                            player.getRoom().GetDirection("south").RemoveFromDescription(item.getmsg());
                        }
                    }

                break;
            case "hint1":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("hint1")) {
                            player.getRoom().GetDirection("south").RemoveFromDescription(item.getmsg());
                        }
                    }

                break;
            case "hint2":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("hint2")) {
                            player.getRoom().GetDirection("east").RemoveFromDescription(item.getmsg());
                        }
                    }

                break;
            case "hint3":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("hint3")) {
                            player.getRoom().GetDirection("east").RemoveFromDescription(item.getmsg());
                        }
                    }

                break;
            case "hint4":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("hint4")) {
                            player.getRoom().GetDirection("west").RemoveFromDescription(item.getmsg());
                        }
                    }


                break;
            case "robothead":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("RobotHead")) {
                            player.getRoom().GetDirection("south").RemoveFromDescription(item.getmsg());
                        }
                    }


                break;
            case "robotlegs":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("RobotLegs")) {
                            player.getRoom().GetDirection("north").RemoveFromDescription(item.getmsg());
                        }
                    }
                break;
            case "robotbody":

                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("RobotBody")) {
                        player.getRoom().GetDirection("north").RemoveFromDescription(item.getmsg());
                    }
                }
                break;



        }
    }
    //method to add the item msg to direction description once item is dropped off
    private static void AddtoDirDescription(String itemname){
        switch (itemname.toLowerCase()) {
            case "glasses":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("glasses")) {
                            player.getRoom().GetDirection("south").AddToDescription(item.getmsg());
                        }
                    }

                break;
            case "flashlight":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("flashlight")) {
                            player.getRoom().GetDirection("south").AddToDescription(item.getmsg());
                        }
                    }

                break;
            case "book":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("book")) {
                            player.getRoom().GetDirection("south").AddToDescription(item.getmsg());
                        }
                    }

                break;
            case "rope":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("rope")) {
                            player.getRoom().GetDirection("south").AddToDescription(item.getmsg());
                        }
                    }


                break;
            case "robothead":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("robothead")) {
                            player.getRoom().GetDirection("south").AddToDescription(item.getmsg());
                        }
                    }


                break;
            case "robotlegs":

                    for (Item item: player.getRoom().getItems()) {
                        if (item.getName().equalsIgnoreCase("robotlegs")) {
                            player.getRoom().GetDirection("south").AddToDescription(item.getmsg());
                        }
                    }


                break;
            case "robotbody":

                for (Item item: player.getRoom().getItems()) {
                    if (item.getName().equalsIgnoreCase("robotBody")) {
                        player.getRoom().GetDirection("south").AddToDescription(item.getmsg());
                    }
                }


                break;
        }
    }

    //method to use item that return msg
    public static String useItem(String itemname) {
        String usemsg = "";
        //get item named or return null if it doesn't exist
        Item i = player.getItems().getItem(itemname);

        //if item is null
        if (i == null) {
            usemsg = itemname + " isn't in your inventory.";
        } else {
            switch (itemname){
                case "flashlight":
                    if (Map.get(0).equals(player.getRoom())) {
                        unlockPath(player.getRoom(), "west");
                        usemsg = "Used " + itemname + " ! The flashlight lights the way to the west in " + player.getRoom().getName();
                    } else {
                        usemsg = "You cannot use " + itemname + " in " + player.getRoom().getName();
                    }
                    break;
                case "rope":
                    //Check if player is in either room 7 or room 8 else you cannot use rope
                    if (Map.get(6).equals(player.getRoom()))
                    {
                        unlockPath(player.getRoom(), "south");
                        usemsg = "You just used " + itemname + "! you can now climb down to the other side (walk south)";
                    }else
                    {
                        usemsg = "You cannot use " + itemname + "here!";
                    }
                    break;
                case "book":
                    if(Map.get(1).equals(player.getRoom())){
                        unlockPath(player.getRoom(), "west");
                        usemsg = "You placed the " + itemname + " on the book shelf. The book shelf rotates revealing a hidden path way";
                        player.getItems().remove(i);
                    }else{
                        usemsg = "This book is written in ORCish, I can't read it!";
                    }
                    break;
                    //TODO: Fill details about the hints
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
                msg = NPCS.get(3).getmsg(1);
                NPCS.get(3).setsatisfied(true);
            }else
            {
                msg = NPCS.get(3).getmsg(2);
            }
        }else if(player.getRoom().equals((Map.get(5)))){
            if(guess.equals("3.14")){
                msg = NPCS.get(4).getmsg(1);
                NPCS.get(4).setsatisfied(true);
            }else
            {
                msg = NPCS.get(4).getmsg(2);
            }
        }else{
            msg = "You need to be in "+ Map.get(4).getName() + " to solve the riddle! \nOr in "+ Map.get(5).getName() + " to solve the math test!!" ;
        }
        //TODO: if the player guess the correct word what should we do to the ORC ?

        return msg;
    }
    //method to drop item that return msg
    public static String dropItem(String itemname) {
        String dropmsg = "";
        Item i = player.getItems().getItem(itemname);
        if (itemname.equals("")) {
            dropmsg = "Which Item you would like to drop?";
        }

        if (i == null) {
            dropmsg = itemname + " is not here";
        } else {

            moveItem(i, player.getItems(), player.getRoom().getItems());
            dropmsg = "you just dropped " + itemname + " !";
            AddtoDirDescription(itemname);
        }
        return dropmsg;
    }
    //method to give item to NPC of that room
    public static String give(String itemname) {
        String givemsg ="";

        //get item named or return null if it doesn't exist
        Item i = player.getItems().getItem(itemname);

        if (itemname.equals("")) {
            givemsg = "Which Item you would like to give?";
        }
        if (i == null) {
            givemsg = itemname + " is not here";
        } else
        {
            switch(player.getRoom().getId()){
                case 1:
                    switch (itemname.toLowerCase()){
                        case "glasses":
                            moveItem(i, player.getItems(), NPCS.get(0).getItems());
                            unlockPath(player.getRoom(),"north");
                            unlockPath(player.getRoom(),"east");
                            NPCS.get(0).setsatisfied(true);
                            isTutorial = false;
                            givemsg = "You handed " + itemname + " to " + NPCS.get(0).getName() +"\n " + NPCS.get(0).getmsg(1);
                            break;
                        default:
                            givemsg = "This Orc doesn't need "+ itemname + "!";
                            break;
                    }
                    break;
                case 2:
                    givemsg = "";
                    break;

                case 3:
                    switch (itemname.toLowerCase()){
                        case "robothead":
                            if(NPCS.get(1).getItems().size()>0){
                                for(Item item : NPCS.get(1).getItems()){
                                    if(item.getName().equalsIgnoreCase("RobotBody"))
                                    {
                                        moveItem(i,player.getItems(),NPCS.get(1).getItems());
                                        NPCS.get(1).setsatisfied(true);
                                        givemsg = "You handed "+ itemname + " to " + NPCS.get(1).getName() +"\n " + NPCS.get(1).getmsg(1);
                                        break;
                                    }else{
                                        givemsg = NPCS.get(1).getmsg(3);
                                    }
                                }
                            }else{
                                givemsg = NPCS.get(1).getmsg(3);
                            }

                            break;
                        case "robotbody":
                            if(NPCS.get(1).getItems().size()>0){
                                for(Item item : NPCS.get(1).getItems()){
                                    if(item.getName().equalsIgnoreCase("RobotLegs"))
                                    {
                                        moveItem(i,player.getItems(),NPCS.get(1).getItems());
                                        givemsg = "You handed "+ itemname + " to " + NPCS.get(1).getName() +"\n " + NPCS.get(1).getmsg(2);
                                        break;
                                    }else{
                                        givemsg = NPCS.get(1).getmsg(3);
                                    }

                                }

                            }else{
                                givemsg = NPCS.get(1).getmsg(3);
                            }
                            break;
                        case "robotlegs":

                            moveItem(i,player.getItems(),NPCS.get(1).getItems());
                            givemsg = "You handed "+ itemname + " to " + NPCS.get(1).getName() +"\n " + NPCS.get(1).getmsg(2);
                            break;
                        default:
                            givemsg = "This Orc doesn't need "+ itemname + "!";
                            break;
                    }
                    break;
                case 4:
                    givemsg = "This Orc doesn't need "+ itemname + "!";
                    break;

                case 5:
                    givemsg = "This Orc doesn't need "+ itemname + "!";;
                    break;
                case 6:
                    givemsg = "This Orc doesn't need "+ itemname + "!";;
                    break;
                case 7:
                    givemsg = "";
                    break;
                case 8:
                    givemsg = "This Orc doesn't need "+ itemname + "!";;
                    break;


            }

        }
        return givemsg;
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


    //move inside of the room
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
                                if(!NPCS.get(4).getsatisfied()){
                                    movemsg = "You are going to " + Map.get(5).getName()+ "\n" + NPCS.get(4).getmsg(0);
                                }else
                                {
                                    movemsg = "You are going to " + Map.get(5).getName();
                                }
                                movePlayerTo(player, Map.get(5));
                            }
                            case "south" -> {
                                movemsg = "To the south, you see the portal that brought you here. You cannot access it";
                            }
                            case "west" -> {
                                if(!NPCS.get(3).getsatisfied()){
                                    movemsg = "You are going to " + Map.get(4).getName() + "\n" + NPCS.get(3).getmsg(0);
                                }else{
                                    movemsg = "You are going to " + Map.get(4).getName();
                                }
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
                                if(!NPCS.get(3).getsatisfied()){
                                    movemsg = "You are going to " + Map.get(2).getName() + "\n" + NPCS.get(1).getmsg(0);
                                }else {
                                    movemsg = "You are going to " + Map.get(2).getName();
                                }

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
                                if(!NPCS.get(4).getsatisfied()){
                                    movemsg = "You are going to " + Map.get(5).getName() + "\n" + NPCS.get(4).getmsg(0);
                                }else
                                {
                                    movemsg = "You are going to " + Map.get(5).getName();
                                }

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
                                if(!NPCS.get(2).getsatisfied()){
                                    movemsg = "You are going to " + Map.get(3).getName()+ "\n" + NPCS.get(2).getmsg(0);
                                }else{
                                    movemsg = "You are going to " + Map.get(3).getName();
                                }

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
                                if(!NPCS.get(5).getsatisfied()){
                                    movemsg = "You are going to " + Map.get(7).getName()+ "\n" + NPCS.get(5).getmsg(0);
                                }else{
                                    movemsg = "You are going to " + Map.get(7).getName();
                                }

                                movePlayerTo(player, Map.get(7));
                            }
                            case "west" -> {
                                movemsg = "You are going to " + Map.get(5).getName()+ "\n" + NPCS.get(4).getmsg(0);
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