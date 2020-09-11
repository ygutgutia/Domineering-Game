import java.util.Collection;

// Game trees for abstract games two-person games with outcomes in the
// type of integers, parameterised by a type of moves.

/**
 * An implementation of a game tree using heuristics where necessary.
 * 
 * @author Martin Escardo
 * (Martin Escardo provided the framework)
 *
 * @author James Birch
 * (I made adjustments to the player methods and implemented size and height - these were used as part of marking)
 *
 * @param <Move>
 */
public class GameTree2<Move extends Comparable<Move>> {
	private final Board2<Move>                       board2;
	private final Table<Move, GameTree2<Move>>       children;
	private final int                                optimalOutcome;

	/**
	 * Saves the board, table and optimal outcome for a tree.
	 * @param Board2 The board.
	 * @param children The children of a given board.
	 * @param optimalOutcome The optimal outcome for the board.
	 */
	public GameTree2(Board2<Move> Board2,
			Table<Move, GameTree2<Move>> children,
			int optimalOutcome) {

		assert (Board2 != null && children != null);
		this.board2 = Board2;
		this.children = children;
		this.optimalOutcome = optimalOutcome;
	}

	/**
	 * Determines if a board is a leaf node.
	 * @return A boolean for whether the board is a leaf.
	 */
	public boolean isLeaf() {
		return (children.isEmpty());
	}

	// Getter methods:
	
	/**
	 * Gets the board.
	 * @return The board.
	 */
	public Board2<Move> Board2() {
		return board2;
	}

	/**
	 * Gets the children of the board.
	 * @return The children of the board.
	 */
	public Table<Move, GameTree2<Move>> children() {
		return children;
	}

	/**
	 * Gets the optimal outcome.
	 * @return The optimal outcome.
	 */
	public int optimalOutcome() {
		return optimalOutcome;
	}

	// The following two methods are for game tree statistics only.
	// They are not used for playing.

	/**
	 * Calculates the number of nodes in the tree.
	 * @return The number of nodes in the tree.
	 */
	public int size() {
		int size = 1;
		Collection<GameTree2<Move>> values = children.values();
		for(GameTree2<Move> child : values) {
			size += child.size();
		}
		return size;
	}

	/**
	 * Calculates the height of the tree (leaf node has height 0 instead of -1).
	 * @return The height of the tree.
	 */
	public int height() {
		int height = -1;
		Collection<GameTree2<Move>> values = children.values();
		for(GameTree2<Move> child : values) {
			height = Math.max(height,child.height());
		}
		return 1 + height;
	}

	/**
	 * Plays first using this tree.
	 * @param c A move channel to send moves between.
	 * @param level The current depth.
	 * @param alpha The lower bound.
	 * @param beta The upper bound.
	 * @param heuristic A boolean for whether the game player is using the heuristic version.
	 */
	public void firstPlayer(MoveChannel<Move> c, int level, int alpha, int beta,
			boolean heuristic) {
		if(heuristic) {
			c.comment(board2 + "\nThe heuristic outcome is " + optimalOutcome);
		}
		else {
			c.comment(board2 + "\nThe optimal outcome is " + optimalOutcome);
		}

		if(isLeaf()) {
			if(board2.availableMoves().isEmpty()) {
				assert (optimalOutcome == board2.value());
				c.end(board2.value());
			}
			else if(heuristic) {
				board2.tree(level, alpha, beta, heuristic).firstPlayer(c, level, alpha, beta, heuristic);
			}
		}
		else {
			Entry<Move, GameTree2<Move>> optimalEntry = null;
			Collection<Move> moves = children.keys();
			for (Move child : moves) {
				if (optimalOutcome == children.get(child).get().optimalOutcome) {
					optimalEntry = new Entry<Move, GameTree2<Move>>(child, children.get(child).get());
					break;
				}
			}
			assert (optimalEntry != null);
			c.giveMove(optimalEntry.getKey());
			optimalEntry.getValue().secondPlayer(c, level, alpha, beta, heuristic);
		}
	}

	/**
	 * Plays second using this tree.
	 * @param c A move channel to send moves between.
	 * @param level The current depth.
	 * @param alpha The lower bound.
	 * @param beta The upper bound.
	 * @param heuristic A boolean for whether the game player is using the heuristic version.
	 */
	public void secondPlayer(MoveChannel<Move> c, int level, int alpha, int beta,
			boolean heuristic) {
		if(heuristic) {
			c.comment(board2 + "\nThe heuristic outcome is " + optimalOutcome);
		}
		else {
			c.comment(board2 + "\nThe optimal outcome is " + optimalOutcome);
		}

		if(isLeaf()) {
			if(board2.availableMoves().isEmpty()) {
				assert (optimalOutcome == board2.value());
				c.end(board2.value());
			}
			else if(heuristic) {
				board2.tree(level, alpha, beta, heuristic).secondPlayer(c, level, alpha, beta, heuristic);
			}
		}
		else {
			Move m = c.getMove();
			if(children.containsKey(m)) {
				children.get(m).get().firstPlayer(c, level, alpha, beta, heuristic);
			}
			else {
				board2.newTree(m, level, alpha, beta, heuristic).firstPlayer(c, level, alpha, beta, heuristic);
			}
		}
	}
}
