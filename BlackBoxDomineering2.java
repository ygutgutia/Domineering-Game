import java.util.ArrayList;
import java.util.Scanner;

/**
 * A black box player for the game Domineering that can play optimally up to
 * 5x4/4x5 boards and heuristically for dimensions above that.
 *
 * @author James Birch
 */
public class BlackBoxDomineering2 {

    private static int    rows; //number of rows on board
    private static int    columns; //number of columns on board
    private static int    size; //number of positions on board
    private static String cpuTurn; //whether the cpu goes first or second
    private static String myTurn; //whether the player is horizontal or vertical
    private static ArrayList<Integer> played        = new ArrayList<Integer>(); //tiles already taken
    private static ArrayList<Integer> illegalMovesH = new ArrayList<Integer>(); //forbidden moves for horizontal
    private static ArrayList<Integer> illegalMovesV = new ArrayList<Integer>(); //forbidden moves for vertical

    private static class BBD implements MoveChannel<DomineeringMove> {

        private static Scanner scanner = new Scanner(System.in);

        @Override
        /**
         * Gets the move from the player.
         * @return A new DomineeringMove object.
         */
        public DomineeringMove getMove() {
            //enter move
            String move = scanner.nextLine(); 
	
            //get the x and y values from the move
            String xCoord  = "";
            String yCoord  = "";
            int    tracker = 0;  //tracks for the location of the comma
            for(int i = 1; i <= move.length(); i++) {
                if(move.substring(i - 1, i).equals(",")) {
                    break;
                }
                else {
                    xCoord += move.substring(i - 1, i);
                }
                tracker++;
            }

            int x = 0; //holding value - does not matter as it will exit if it cannot parse x
            try {
                x = Integer.parseInt(xCoord);
            }
            catch(NumberFormatException e) {
                System.exit(1);
            }
            for(int i = tracker + 2; i <= move.length(); i++) {
                yCoord += move.substring(i - 1, i);
            }

            int y = 0; //holding value - does not matter as it will exit if it cannot parse y
            try {
                y = Integer.parseInt(yCoord);
            }
            catch(NumberFormatException e) {
                System.exit(1);
            }

            //convert that move to the corresponding board position
            int position = x + (columns * y) + 1;

            //early exit conditions
            if(played.contains(position) || position > size || position < 1) {
                System.exit(1);  //exit if any of these conditions are violated
            }

            if(myTurn.equals("H") && illegalMovesH.contains(position)) {
                System.exit(1); //if player who is horizontal plays a illegal horizontal move - exit
            }
            else if(myTurn.equals("V") && illegalMovesV.contains(position)) {
                System.exit(1); //if player who is vertical plays a illegal vertical move - exit
            }

            if(myTurn.equals("H") && played.contains(position + 1)) {
                System.exit(1); //covers scenario where player who is horizontal plays a free tile
                //but the adjacent tile is not free
            }
            else if(myTurn.equals("V") && played.contains(position + columns)) {
                System.exit(1); //covers scenario where player who is vertical plays a free tile
                //but the tile below is not free
            }

            //now add the corresponding positions to played moves
            played.add(position); //add to list of played moves
            if(myTurn.equals("H")) {
                played.add(position + 1); //if player is horizontal - cover square to the right
            }
            else {
                played.add(position + columns); //if player is vertical - cover square below
            }
            //System.out.println("Player: " + position);
            System.out.println("Player: " + x + "," + y);
            return new DomineeringMove(position);
        }

        @Override
        /**
         * Informs the move made by the cpu.
         * @param move The move made.
         */
        public void giveMove(DomineeringMove move) {
            //convert a move so that it prints as x,y
            played.add(move.getMove());
            if(myTurn.equals("H")) { //cpu is vertical
                played.add(move.getMove() + columns); //needs to cover tile below
            }
            else {
                played.add(move.getMove() + 1); //needs to cover tile to the right
            }
            int x       = 0;
            int y       = 0;
            int counter = 1;
            for(int i = 1; i <= size; i++) {
                if(counter == move.getMove()) {
                    break;
                }
                else if(i % columns != 0) {
                    x++;
                }
                else {
                    x = 0;
                    y++;
                }
                counter++;
            }
            System.out.println("Computer: " + x + "," + y);
            //System.err.println("Computer: " + (x + columns * y + 1));
        }

        @Override
        /**
         * Informs the game is over.
         * @param Value The value of the outcome.
         */
        public void end(int Value) {
            //System.err.println("Game over. The result is " + Value);
            scanner.close();
            System.exit(0);
        }

        @Override
        /**
         * Prints a comment to System.out.
         * @param msg The message to print.
         */
        public void comment(String msg) {
            System.out.println(msg);
        }
    }

    /**
     * Creates a new instance of the game with either the human going first or the computer.
     */
    public static void main(String[] args) {
        assert (args.length == 4);

        try {
            cpuTurn = args[0]; //first or second
            columns = Integer.parseInt(args[2]); //width
            rows = Integer.parseInt(args[3]); //height
            /*	Do not need to record args[1] due to the promise by Martin that the program
             * 	will only be called as 'first horizontal x y' or 'second vertical x y' which
	     *  means that the convention where the player who goes first is always horizontal
             * 	and the player that goes second is always vertical is being opted for.
	     */
        }
        catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: java BlackBoxDomineering2 <first/second> <horizontal/vertical> <x> <y>");
            System.exit(1); //exit if columns or rows is not given as an int
        }
	
	if((columns == 0 || rows == 0) && cpuTurn.equals("second")) {
		System.exit(0); //horizontal always goes first so will always lose in this situation		
	}
	
        assert (args[0].equals("first") || args[0].equals("second"));
        assert (args[1].equals("horizontal") || args[1].equals("vertical"));

        size = rows * columns;

        if(cpuTurn.equals("first")) {
            myTurn = "V"; //if cpu is first - they are horizontal so we must be vertical
        }
        else {
            myTurn = "H"; //if cpu is second - they are vertical so we must be horizontal
        }

        //assign each player's illegal moves (e.g. last row or column respectively)
        for(int i = 1; i <= size; i++) {
            if(i % columns == 0) {
                illegalMovesH.add(i);
            }
            else if(i > (size - columns)) {
                illegalMovesV.add(i);
            }
        }

        DomineeringBoard2 board = new DomineeringBoard2(columns, rows);
        
        if((columns <= 5 && rows <= 5) && !(columns == 5 && rows == 5)) { //if true -> take optimal approach
            if(cpuTurn.equals("first")) {
                board.tree(-10, -1, 1, false).firstPlayer(new BBD(), -10, -1, 1, false); //computer first (plays as H)
                //use a bogus value for the level - just so that it can use the tree method.
            }
            else {
                board.tree(-10, -1, 1, false).secondPlayer(new BBD(), -10, -1, 1, false); //computer second (plays as V)
                //use a bogus value for the level - just so that it can use the tree method.
            }
        }
        else { //otherwise take heuristic approach
            if(cpuTurn.equals("first")) {
                board.tree(5, -1, 1, true).firstPlayer(new BBD(), 5, -1, 1, true); //computer first (plays as H)
            }
            else {
                board.tree(5, -1, 1, true).secondPlayer(new BBD(), 5, -1, 1, true); //computer second (plays as V)
            }
        }
    }
}
