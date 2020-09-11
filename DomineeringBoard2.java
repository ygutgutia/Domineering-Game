import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents the Domineering board for the game.
 *
 * @author James Birch
 *         <p>
 *         A 4x4 grid of positions.
 *         <p>
 *         1----2----3----4
 *         5----6----7----8
 *         9---10---11----12
 *         13--14---15----16
 *         <p>
 *         This means that for any size board, the positions are numbered
 *         from 1 to rows*columns where m is the number of rows and n is
 *         the number of columns.
 */
public class DomineeringBoard2 extends Board2<DomineeringMove> {

    public static final Player H = Player.MAXIMIZER;
    public static final Player V = Player.MINIMIZER;

    private final LinkedHashSet<DomineeringMove> hMoves;  //moves played by H
    private final LinkedHashSet<DomineeringMove> vMoves;  //moves played by V

    private final int size; //number of total positions
    private final int rows;
    private final int columns;

    /**
     * Default configuration with no parameters.
     */
    public DomineeringBoard2() {
        columns = 4;
        rows = 4;
        size = rows * columns;

        hMoves = new LinkedHashSet<DomineeringMove>();
        vMoves = new LinkedHashSet<DomineeringMove>();
    }

    /**
     * @param m Number of columns.
     * @param n Number of rows.
     */
    public DomineeringBoard2(int m, int n) {
        columns = m;
        rows = n;
        size = rows * columns;

        hMoves = new LinkedHashSet<DomineeringMove>();
        vMoves = new LinkedHashSet<DomineeringMove>();
    }

    /**
     * @param hMoves Moves H has made.
     * @param vMoves Moves V has made.
     * @param m      Number of columns.
     * @param n      Number of rows.
     * @param size   Number of positions.
     */
    private DomineeringBoard2(LinkedHashSet<DomineeringMove> hMoves, LinkedHashSet<DomineeringMove> vMoves,
                              int m, int n, int size) {
        try {
            assert (disjoint(hMoves, vMoves));
        }
        catch(AssertionError e) {
            System.err.println("Assertion violated: 'disjoint(hMoves, vMoves)' in constructor for DomineeringBoard2");
            System.exit(1);
        }

        this.hMoves = hMoves;
        this.vMoves = vMoves;
        this.columns = m;
        this.rows = n;
        this.size = size;
    }

    @Override
    /**
     * Determines who the next player is.
     * @return The next player.
     */
    Player nextPlayer() {
        return (hMoves.size() + vMoves.size()) % 4 == 0 ? H : V;
        //As horizontal is first, they will cover 2 positions so (size % 4 != 0) making it vertical's turn etc.
    }

    @Override
    /**
     * Finds the available moves for a player.
     * @return A set of available moves.
     */
    Set<DomineeringMove> availableMoves() {
        return (value() == 0
			     ? (nextPlayer() == H ? playerAvailableMoves(H) : playerAvailableMoves(V))
                             : new LinkedHashSet<DomineeringMove>());

        /*return (value() == 0
                ? complement(union(hMoves, vMoves)) //all moves that are not in hMoves and vMoves
                : new LinkedHashSet<DomineeringMove>());*/
    }

    /**
     * Available moves for a specific player.
     *
     * @param player The player to get the moves of.
     *
     * @return A set of that player's moves.
     */
    public Set<DomineeringMove> playerAvailableMoves(Player player) {
        LinkedHashSet<DomineeringMove> a     = new LinkedHashSet<DomineeringMove>();
        LinkedHashSet<DomineeringMove> union = union(hMoves, vMoves);
        if(player == H && (size != 0)) {
            for(int i = 1; i <= size; i++) {
                if(!union.contains(new DomineeringMove(i)) && !union.contains(new DomineeringMove(i + 1))) {
                    if(i % columns != 0) {
                        a.add(new DomineeringMove(i));  //only add if i isn't in the farthest right column
                    }
                }
            }
        }
        else if (size != 0) {
            for(int i = 1; i <= (size - columns); i++) { //only add up to penultimate row
                if(!union.contains(new DomineeringMove(i)) && !union.contains(new DomineeringMove(i + columns))) {
                    a.add(new DomineeringMove(i));
                }
            }
        }
        return a;
    }

    @Override
    /**
     * The value of the game state (0 is still playing, 1 is H wins, -1 is V wins).
     * @return The value of the game state.
     */
    int value() {
    	//return (loses(H) && nextPlayer() == H) ? -1
	//			   : (loses(V) && nextPlayer() == V ? 1 
	//					   			: 0);
    	if(loses(H) && nextPlayer() == H) {
        	return -1;
        }
        else if(loses(V) && nextPlayer() == V) {
        	return 1;
        }
        else {
        	return 0;
        }
    }

    @Override
    /**
     * Play a move on the board.
     * @param move A move to make.
     * @return A new board with the move made on it.
     */
    Board2<DomineeringMove> play(DomineeringMove move) {
        try {
            assert (!hMoves.contains(move) && !vMoves.contains(move));
        }
        catch(AssertionError e) {
            System.err.println("Assertion violated: '!hMoves.contains(move) && !vMoves.contains(move)' in 'play'");
            System.exit(1);
        }

        if(nextPlayer() == H) {
            return new DomineeringBoard2(add(hMoves, move, new DomineeringMove(move.getMove() + 1)), vMoves,
                                         columns, rows, size);
        }
        else {
            return new DomineeringBoard2(hMoves, add(vMoves, move, new DomineeringMove(move.getMove() + columns)),
                                         columns, rows, size);
        }
    }

    @Override
    /**
     * A printout of the board.
     * @return The current board.
     */
    public String toString() {
        String displayBoard = "";
        int    counter      = 1;
        for(int i = 1; i <= rows; i++) {
            for(int j = 1; j <= columns; j++) {
                displayBoard += pm(new DomineeringMove(counter));
                counter++;
            }
            displayBoard += "\n";
        }
        return displayBoard;
    }

    /**
     * Prints a player if they have moved in a particular position.
     *
     * @param m A move.
     * @return A string with who moved.
     */
    private String pm(DomineeringMove m) {
        return (hMoves.contains(m) ? "H " : vMoves.contains(m) ? "V " : "- ");
    }

    /**
     * Determine if a player has lost based on their available moves.
     *
     * @return A boolean for whether a player has lost.
     */
    private boolean loses(Player player) {
        if(player == H) {
            if(playerAvailableMoves(player).isEmpty() && !playerAvailableMoves(V).isEmpty()) {
                return true;
            }
            else if(!playerAvailableMoves(player).isEmpty() && playerAvailableMoves(V).isEmpty()) {
                return false;
            }
            else if(playerAvailableMoves(player).isEmpty() && playerAvailableMoves(V).isEmpty()) {
                return nextPlayer() == player;
            }
            else {
                return false;
            }
        }
        else {
            if(playerAvailableMoves(player).isEmpty() && !playerAvailableMoves(H).isEmpty()) {
                return true;
            }
            else if(!playerAvailableMoves(player).isEmpty() && playerAvailableMoves(H).isEmpty()) {
                return false;
            }
            else if(playerAvailableMoves(player).isEmpty() && playerAvailableMoves(H).isEmpty()) {

                return nextPlayer() == player;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Returns the intersection of 2 sets a and b.
     *
     * @param a A set.
     * @param b A set.
     * @return The intersection of the 2 sets.
     */
    private LinkedHashSet<DomineeringMove> intersection(LinkedHashSet<DomineeringMove> a,
                                                        LinkedHashSet<DomineeringMove> b) {
        LinkedHashSet<DomineeringMove> c = new LinkedHashSet<DomineeringMove>();
        for(int i = 1; i <= size; i++) {
            if(a.contains(new DomineeringMove(i)) && b.contains(new DomineeringMove(i))) {
                c.add(new DomineeringMove(i));
            }
        }
        return c;
    }

    /**
     * Checks to see whether the 2 sets a and b are disjoint.
     *
     * @param a A set of moves.
     * @param b A set of moves.
     * @return A boolean as to whether the 2 sets are disjoint.
     */
    private boolean disjoint(LinkedHashSet<DomineeringMove> a, LinkedHashSet<DomineeringMove> b) {
        return (intersection(a, b).isEmpty());
    }

    /**
     * Constructs the union of the 2 sets a and b.
     *
     * @param a A set.
     * @param b A set.
     * @return The union of the 2 sets.
     */
    private LinkedHashSet<DomineeringMove> union(LinkedHashSet<DomineeringMove> a,
                                                 LinkedHashSet<DomineeringMove> b) {
        LinkedHashSet<DomineeringMove> c = new LinkedHashSet<DomineeringMove>();
        for(int i = 1; i <= size; i++) {
            if(a.contains(new DomineeringMove(i))) {
                c.add(new DomineeringMove(i));
            }
        }
        for(int i = 1; i <= size; i++) {
            if(b.contains(new DomineeringMove(i))) {
                c.add(new DomineeringMove(i));
            }
        }
        return c;
    }

    /**
     * Adds the move b to the set a.
     *
     * @param a A set.
     * @param b A DomineeringMove object.
     * @param c A DomineeringMove object.
     * @return The set a with the move b and c added to it.
     */
    private LinkedHashSet<DomineeringMove> add(LinkedHashSet<DomineeringMove> a, DomineeringMove b, DomineeringMove c) {
        LinkedHashSet<DomineeringMove> d = new LinkedHashSet<DomineeringMove>();
        for(int i = 1; i <= size; i++) {
            if(a.contains(new DomineeringMove(i))) {
                d.add(new DomineeringMove(i));
            }
        }
        d.add(new DomineeringMove(b.getMove()));
        d.add(new DomineeringMove(c.getMove()));
        return d;
    }

    /**
     * A way of heuristically calculating what the value of a board is (based on number
     * of available moves).
     *
     * @return The size of the maximiser's available moves.
     */
    public int heuristicValue() {
        return (playerAvailableMoves(H).size() > playerAvailableMoves(V).size() ? 1 : -1);
    }
}
