import java.util.Optional;

/**
 * A class which implements the interface Bst to represent an empty binary search tree.
 * @author James Birch
 */
public class Empty<Key extends Comparable<Key>,Value> implements Bst<Key,Value> {

	public Empty() {
		//Do nothing
	}

	@Override
	/**
	 * Change what is printed.
	 */
	public String toString() { 
		return ""; 
	}

	@Override
	/**
	 * Get the stored key.
	 * @return The stored key.
	 */
	public Optional<Key> getKey() {
		return Optional.empty();
	}

	@Override
	/**
	 * Get the stored value.
	 * @return The stored value.
	 */
	public Optional<Value> getValue() {
		return Optional.empty();
	}

	@Override
	/**
	 * Get the left subtree.
	 * @return The stored left subtree.
	 */
	public Optional<Bst<Key,Value>> getLeft() {
		return Optional.empty();
	}

	@Override
	/**
	 * Get the right subtree.
	 * @return The stored right subtree.
	 */
	public Optional<Bst<Key,Value>> getRight() {
		return Optional.empty();
	}

	@Override
	/**
	 * Determine is tree is empty.
	 * @return True or false respectively for whether the tree is empty or not.
	 */
	public boolean isEmpty() {
		return true;
	}

	@Override
	/**
	 * Determine if every key is smaller than k.
	 * @param k The key to compare to.
	 * @return True or false for whether every key is smaller than k.
	 */
	public boolean smaller(Key k) {
		return true; //k will always be smaller as there are no keys to compare to
	}

	@Override
	/**
	 * Determine if every key is larger than k.
	 * @param k The key to compare to.
	 * @return True or false for whether every key is larger than k.
	 */
	public boolean bigger(Key k) {
		return true; //k will always be bigger as there are no keys to compare to
	}

	@Override
	/**
	 * Determine if the tree contains key k.
	 * @param k The key to see if the tree contains.
	 * @return True or false for whether the tree contains key k.
	 */
	public boolean has(Key k) {
		return false; //an empty tree cannot possess a key k
	}

	@Override
	/**
	 * Finds the key k, if it exists.
	 * @param k The key to find.
	 * @return An option type which either is empty or contains the value that the key k refers to.
	 */
	public Optional<Value> find(Key k) {
		return Optional.empty(); //cannot find a key within an empty tree
	}

	@Override
	/**
	 * Inserts the key k and value v into the binary search tree. The value is overwritten if k already exists.
	 * @param k The key to insert.
	 * @param v The value to insert.
	 * @return A copy of the binary search tree with k, v inserted.
	 */
	public Bst<Key, Value> put(Key k, Value v) {
		return new Fork<Key, Value>(k, v, new Empty<Key, Value>(), new Empty<Key, Value>());
		//return a new tree where the tree has one node (k, v)
	}

	@Override
	/**
	 * Deletes key k from the binary search tree.
	 * @param k The key to be deleted.
	 * @return A copy of the binary search tree with k and its corresponding value deleted.
	 */
	public Optional<Bst<Key, Value>> delete(Key k) {
		return Optional.of(this); //deleting from an empty tree will have no overall effect - return this
	}

	@Override
	/**
	 * Finds the smallest key in the tree.
	 * @return An option type which is empty if the tree is empty otherwise it is an option of type Entry<Key, Value>.
	 */
	public Optional<Entry<Key, Value>> smallest() {
		return Optional.empty(); //cannot find smallest key in an empty tree
	}

	@Override
	/**
	 * Deletes the smallest key in the tree.
	 * @return An option type which is empty if the tree is empty otherwise it is an option of type Bst<Key, Value>.
	 */
	public Optional<Bst<Key, Value>> deleteSmallest() {
		return Optional.empty(); //cannot delete smallest key in an empty tree
	}

	@Override
	/**
	 * Finds the largest key in the tree.
	 * @return An option type which is empty if the tree is empty otherwise it is an option of type Entry<Key, Value>. 
	 */
	public Optional<Entry<Key, Value>> largest() {
		return Optional.empty(); //cannot find largest key in an empty tree
	}

	@Override
	/**
	 * Deletes the largest key in the tree.
	 * @return An option type which is empty if the tree is empty otherwise it is an option of type Bst<Key, Value>.
	 */
	public Optional<Bst<Key, Value>> deleteLargest() {
		return Optional.empty(); //cannot delete largest key in an empty tree
	}

	@Override
	/**
	 * Prints out the binary search tree in a human-readable format.
	 * @return A human-readable binary search tree.
	 */
	public String fancyToString() {
		return ""; //empty string
	}

	@Override
	/**
	 * fancyToString started at depth d.
	 * @return A human-readable binary search tree starting from depth d.
	 */
	public String fancyToString(int d) {
		return fancyToString(); //empty string
	}

	@Override
	/**
	 * The number of nodes in the binary search tree.
	 * @return The number of nodes.
	 */
	public int size() {
		return 0;
	}

	@Override
	/**
	 * The height of the binary search tree.
	 * @return The height of the tree. By convention, it is -1 for the empty tree.
	 */
	public int height() {
		return -1;
	}

	@Override
	/**
	 * Prints the nodes as a result of an in-order traversal.
	 */
	public void printInOrder() {
		System.out.println(""); //print nothing
	}

	@Override
	/**
	 * Add the nodes in an array as a result of an in-order traversal starting from zero.
	 * @param a An array which is to have the tree saved to.
	 */
	public void saveInOrder(Entry<Key, Value>[] a) {
		//Do nothing
	}

	@Override
	/**
	 * Add the nodes in an array as a result of an in-order traversal starting from position i.
	 * @param a An array which is to have the tree saved to.
	 * @param i The position in the array to start adding from.
	 * @return The next available position in the array.
	 */
	public int saveInOrder(Entry<Key, Value>[] a, int i) {
		return i; //don't need to save anything - next available position in array is i
	}

	@Override
	/**
	 * Balances a tree so that it has minimal height.
	 * @return A copy of the tree which has been changed so that it is balanced.
	 */
	public Bst<Key, Value> balanced() {
		return this; //just return this (the empty tree)
	}
}
