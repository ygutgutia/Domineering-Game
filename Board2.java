import java.util.Set;

/**
 * An abstract two-player game with outcomes in the integers.
 * We define a particular game by implementing the abstract methods.
 * Our approach requires immutable implementations of Board2.
 *
 * @author Martin Escardo 
 * (Martin Escardo provided most of the framework)
 *
 * @author James Birch
 * (I filled in the tree methods and added heuristic functionality)
 *
 * @param <Move> A move on the board.
 */
public abstract class Board2<Move extends Comparable<Move>> {

	/**
	 * The next player to play.
	 *
	 * @return The next player.
	 */
	abstract Player nextPlayer();

	/**
	 * The available moves.
	 *
	 * @return A set of available moves.
	 */
	abstract Set<Move> availableMoves();

	/**
	 * The current state of the board (1 is H win, 0 is still playing
	 * -1 is V win).
	 *
	 * @return The current state of the board as an int.
	 */
	abstract int value();

	/**
	 * Plays a move.
	 *
	 * @param move The move to play.
	 * @return The board with the move played on it.
	 */
	abstract Board2<Move> play(Move move);

	/**
	 * Generates a heuristic value for a game board (Either 1 or -1).
	 *
	 * @return An estimate for the outcome of a game given a board.
	 */
	abstract int heuristicValue();

	/**
	 * A tree of the game.
	 * 
	 * @param level     The current depth.
	 * @param alpha     The lower bound.
	 * @param beta      The upper bound.
	 * @param heuristic Whether the game played is heuristic.
	 * @return A game tree of the game.
	 */
	public GameTree2<Move> tree(int level, int alpha, int beta, boolean heuristic) {
		if(availableMoves().isEmpty()) {
			return new GameTree2<Move>(this, new BstTable<Move, GameTree2<Move>>(), value());
		}
		else {
			return (nextPlayer() == Player.MAXIMIZER ? maxTree(level - 1, alpha, beta, heuristic)
					: minTree(level - 1, alpha, beta, heuristic));
		}
	}

	/**
	 * Generates a new tree with a move included.
	 *
	 * @param move      The move to play.
	 * @param level     The current depth.
	 * @param alpha     The lower bound.
	 * @param beta      The upper bound.
	 * @param heuristic Whether the game played is heuristic.
	 * @return A new tree with a defined move.
	 */
	public GameTree2<Move> newTree(Move move, int level, int alpha, int beta, boolean heuristic) {

		return play(move).tree(level - 1, alpha, beta, heuristic);
	}

	/**
	 * A helper for tree(). Produces the tree for the maximiser.
	 * 
	 * @param level     The current depth.
	 * @param alpha     The lower bound.
	 * @param beta      The upper bound.
	 * @param heuristic Whether the game played is heuristic.
	 * @return The game tree for the maximiser.
	 */
	public GameTree2<Move> maxTree(int level, int alpha, int beta, boolean heuristic) {
		try {
			assert (!availableMoves().isEmpty());
		}
		catch(AssertionError e) {
			System.err.println("Assertion failed: '!availableMoves().isEmpty' in maxTree()");
			System.exit(1);
		}

		int optimalOutcome = Integer.MIN_VALUE;
		Table<Move, GameTree2<Move>> children
		= new BstTable<Move, GameTree2<Move>>();

		if(level <= 0 && heuristic) {
			optimalOutcome = heuristicValue();
		}
		else {
			for(Move m : availableMoves()) {
				GameTree2<Move> subtree = play(m).tree(level, alpha, beta, heuristic);
				children = children.put(m, subtree);
				optimalOutcome = Math.max(optimalOutcome, subtree.optimalOutcome());
				if(optimalOutcome == beta) {
					break;
				}
			}
		}

		return new GameTree2<Move>(this, children, optimalOutcome);
	}

	/**
	 * A helper for tree(). Produces the tree for the minimiser.
	 * 
	 * @param level     The current depth.
	 * @param alpha     The lower bound.
	 * @param beta      The upper bound.
	 * @param heuristic Whether the game played is heuristic.
	 * @return The game tree for the minimiser.
	 */
	public GameTree2<Move> minTree(int level, int alpha, int beta, boolean heuristic) {
		try {
			assert (!availableMoves().isEmpty());
		}
		catch(AssertionError e) {
			System.err.println("Assertion failed: '!availableMoves().isEmpty' in minTree()");
			System.exit(1);
		}

		int optimalOutcome = Integer.MAX_VALUE;
		Table<Move, GameTree2<Move>> children = new BstTable<Move, GameTree2<Move>>();

		if(level <= 0 && heuristic) {
			optimalOutcome = heuristicValue();
		}
		else {
			for(Move m : availableMoves()) {
				GameTree2<Move> subtree = play(m).tree(level, alpha, beta, heuristic);
				children = children.put(m, subtree);
				optimalOutcome = Math.min(optimalOutcome, subtree.optimalOutcome());
				if(optimalOutcome == alpha) {
					break;
				}
			}
		}

		return new GameTree2<Move>(this, children, optimalOutcome);
	}
}
