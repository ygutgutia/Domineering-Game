/**
 * Represents a tile on the Domineering board. Two of these are
 * needed to fully represent a 'move'.
 *
 * @author James Birch
 */
public class DomineeringMove implements Comparable<DomineeringMove> {

	private final int move;

	/**
	 * Saves the position into the move.
	 * @param position A position on the game board.
	 */
	public DomineeringMove(int position) {
		this.move = position;
	}

	/**
	 * Gets the move.
	 * @return A move as an int.
	 */
	public int getMove() {
		return this.move;
	}

	@Override
	/**
	 * Change the default print out of a DomineeringMove.
	 * @return A string which represents this.
	 */
	public String toString() {
		return String.format("%d", move);
	}

	@Override
	/**
	 * Check two DomineeringMove objects are equal.
	 * @param obj An object.
	 * @return A boolean saying whether this is equal to obj.
	 */
	public boolean equals(Object obj) {
		return (obj instanceof DomineeringMove ? ((DomineeringMove)obj).getMove() == move : false);
	}

	@Override
	/**
	 * Overrides the default hashcode.
	 * @return An int for the hashcode.
	 */
	public int hashCode() {
		String hash = "" + move;
		return hash.hashCode();
	}

	@Override
	/**
	 * A way to compare two DomineeringMove objects.
	 * @param arg0 The move to compare.
	 * @return -1 if specified object is less than the move, 0 if equal and 1 if greater.
	 */
	public int compareTo(DomineeringMove arg0) {
		return((move == arg0.getMove()) ? 0 : ((move < arg0.getMove()) ? 1 : -1));
	}
}
