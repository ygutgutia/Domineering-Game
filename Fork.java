import java.lang.reflect.Array;
import java.util.Optional;

/**
 * A class which implements the interface Bst to represent a non-empty binary search tree.
 * @author James Birch
 */
public class Fork<Key extends Comparable<Key>,Value> implements Bst<Key,Value> {

	private Key key;
	private Value value;
	private Bst<Key, Value> left;
	private Bst<Key, Value> right;

	/**
	 * @param k The key part of the root of the tree.
	 * @param v The value part of the root of the tree.
	 * @param left The left subtree for this key.
	 * @param right The right subtree for this key.
	 */
	public Fork(Key k, Value v, Bst<Key, Value> left, Bst<Key, Value> right) {
		assert(k != null);
		assert(v != null);
		assert(left != null);       // Refuse to work with null pointers.
		assert(right != null);

		assert(left.smaller(k)); // Refuse to violate the bst property.
		assert(right.bigger(k)); // So all our objects will really be BSTs.

		this.key = k;
		this.value = v;
		this.left = left;
		this.right = right;	
	}

	@Override
	/**
	 * Change what is printed.
	 */
	public String toString() {
		return "Fork("  +  key + "," + left.toString()  +  ","  +  right.toString()  +  ")";
	}

	@Override
	/**
	 * Get the stored key.
	 * @return The stored key.
	 */
	public Optional<Key> getKey() {
		return Optional.of(key);
	}

	@Override
	/**
	 * Get the stored value.
	 * @return The stored value.
	 */
	public Optional<Value> getValue() {
		return Optional.of(value);
	}

	@Override
	/**
	 * Get the left subtree.
	 * @return The stored left subtree.
	 */
	public Optional<Bst<Key,Value>> getLeft() {
		return Optional.of(left);
	}

	@Override
	/**
	 * Get the right subtree.
	 * @return The stored right subtree.
	 */
	public Optional<Bst<Key,Value>> getRight() {
		return Optional.of(right);
	}

	@Override
	/**
	 * Determine is tree is empty.
	 * @return True or false respectively for whether the tree is empty or not.
	 */
	public boolean isEmpty() {
		return false;
	}

	@Override
	/**
	 * Determine if every key is smaller than k.
	 * @param k The key to compare to.
	 * @return True or false for whether every key is smaller than k.
	 */
	public boolean smaller(Key k) {
		return key.compareTo(k) < 0 && right.smaller(k); //make sure k is greater than all keys
	}

	@Override
	/**
	 * Determine if every key is larger than k.
	 * @param k The key to compare to.
	 * @return True or false for whether every key is larger than k.
	 */
	public boolean bigger(Key k) {
		return key.compareTo(k) > 0 && left.bigger(k); //make sure k is less than all keys
	}

	@Override
	/**
	 * Determine if the tree contains key k.
	 * @param k The key to see if the tree contains.
	 * @return True or false for whether the tree contains key k.
	 */
	public boolean has(Key k) {
		if (k.compareTo(key) == 0) {
			return true;
		}
		else {
			if (k.compareTo(key) < 0) { // Only one sub-tree needs to be searched.
				return left.has(k);
			}
			else {
				return right.has(k);
			}
		}
	}

	@Override
	/**
	 * Finds the key k, if it exists.
	 * @param k The key to find.
	 * @return An option type which either is empty or contains the value that the key k refers to.
	 */
	public Optional<Value> find(Key k) {
		if (k.compareTo(key) == 0) {
			return Optional.ofNullable(value); //if value is null (because k doesn't exist) - return empty option     
		}
		else {
			if (k.compareTo(key) < 0) { //only needs to search one subtree
				return left.find(k);
			}
			else {
				return right.find(k);
			}
		}
	}

	@Override
	/**
	 * Inserts the key k and value v into the binary search tree. The value is overwritten if k already exists.
	 * @param k The key to insert.
	 * @param v The value to insert.
	 * @return A copy of the binary search tree with k, v inserted.
	 */
	public Bst<Key, Value> put(Key k, Value v) {
		if (k.compareTo(key) == 0) {
			return new Fork<Key, Value>(k, v, left, right); //overwrite the key and value (even though key == k)
		}
		else {                     
			if (k.compareTo(key) < 0) {
				return new Fork<Key, Value>(key, value, left.put(k, v), right); 
			}
			else {
				return new Fork<Key, Value>(key, value, left, right.put(k, v));
			}
		}
	}

	@Override
	/**
	 * Deletes key k from the binary search tree.
	 * @param k The key to be deleted.
	 * @return A copy of the binary search tree with k and its corresponding value deleted.
	 */
	public Optional<Bst<Key, Value>> delete(Key k) {
		if (k.compareTo(key) == 0) {
			if (left.isEmpty()) {
				return Optional.ofNullable(right); //right could be empty
			}
			else {
				if (right.isEmpty()) {
					return Optional.of(left);
				}
				else { // Both non-empty.
					return Optional.of(new Fork<Key, Value>(left.largest().get().getKey(), 
							left.largest().get().getValue(), left.deleteLargest().get(), right));
				}
			}
		}
		else {// We have to delete from one of the subtrees.               
			if (k.compareTo(key) < 0) {
				return Optional.of(new Fork<Key, Value>(key, value, left.delete(k).get(), right));
			}
			else {
				return Optional.of(new Fork<Key, Value>(key, value, left, right.delete(k).get()));
			}
		}
	}

	@Override
	/**
	 * Finds the smallest key in the tree.
	 * @return An option type which is empty if the tree is empty otherwise it is an option of type Entry<Key, Value>.
	 */
	public Optional<Entry<Key, Value>> smallest() {
		if (left.isEmpty()) {
			return Optional.of(new Entry<Key, Value>(key, value));
		}
		else {
			return left.smallest();
		}
	}

	@Override
	/**
	 * Deletes the smallest key in the tree.
	 * @return An option type which is empty if the tree is empty otherwise it is an option of type Bst<Key, Value>.
	 */
	public Optional<Bst<Key, Value>> deleteSmallest() {
		if (left.isEmpty()) {
			return Optional.ofNullable(right); //right could be empty
		}
		else {
			return Optional.of(new Fork<Key, Value>(key, value, left.deleteSmallest().get(), right));
		}
	}

	@Override
	/**
	 * Finds the largest key in the tree.
	 * @return An option type which is empty if the tree is empty otherwise it is an option of type Entry<Key, Value>. 
	 */
	public Optional<Entry<Key, Value>> largest() {
		if (right.isEmpty()) {
			return Optional.of(new Entry<Key, Value>(key, value));
		}
		else {
			return right.largest();
		}
	}

	@Override
	/**
	 * Deletes the largest key in the tree.
	 * @return An option type which is empty if the tree is empty otherwise it is an option of type Bst<Key, Value>.
	 */
	public Optional<Bst<Key, Value>> deleteLargest() {
		if (right.isEmpty()) {
			return Optional.ofNullable(left); //left could be empty
		}
		else {
			return Optional.of(new Fork<Key, Value>(key, value, left, right.deleteLargest().get()));
		}
	}

	@Override
	/**
	 * Prints out the binary search tree in a human-readable format.
	 * The tree is rotated 90 degrees anti-clockwise.
	 * @return A human-readable binary search tree.
	 */
	public String fancyToString() {
		return "\n\n\n" + fancyToString(0) + "\n\n\n";
	}

	@Override
	/**
	 * fancyToString started at depth d.
	 * @param d The depth of the tree.
	 * @return A human-readable binary search tree starting from depth d.
	 */
	public String fancyToString(int d) {
		int step = 4;  // depth step
		String l = left.fancyToString(d+step);
		String r = right.fancyToString(d+step);
		return r + spaces(d) + key + "\n" + l;
	}

	/**
	 * Helper method for fancyToString(int d).
	 * @param n Number of spaces.
	 * @return Spaces to help with construction of printed binary search tree.
	 */
	private String spaces(int n) {
		String s = "";
		for (int i = 0; i < n; i++) {
			s = s + " ";
		}
		return s;
	}

	@Override
	/**
	 * The number of nodes in the binary search tree.
	 * @return The number of nodes.
	 */
	public int size() {
		return (1 + left.size() + right.size());
	}

	@Override
	/**
	 * The height of the binary search tree.
	 * @return The height of the tree.
	 */
	public int height() {
		return 1 + Math.max(left.height(), right.height());
	}

	@Override
	/**
	 * Prints the nodes as a result of an in-order traversal.
	 */
	public void printInOrder() {
		if(!isEmpty()) {
			left.printInOrder();
			System.out.println(key);
			right.printInOrder();
		}
	}

	@Override
	/**
	 * Add the nodes in an array as a result of an in-order traversal starting from zero.
	 * @param a An array which is to have the tree saved to.
	 */
	public void saveInOrder(Entry<Key, Value>[] a) {
		saveInOrder(a, 0);
	}

	@Override
	/**
	 * Add the nodes in an array as a result of an in-order traversal starting from position i.
	 * @param a An array which is to have the tree saved to.
	 * @param i The position in the array to start adding from.
	 * @return The next available position in the array.
	 */
	public int saveInOrder(Entry<Key, Value>[] a, int i) {
		Entry<Key, Value> root = new Entry<Key, Value>(key, value);
		i = left.saveInOrder(a, i);
		a[i++] = root;
		i = right.saveInOrder(a, i);
		return i;
	}

	@Override
	/**
	 * Balances a tree so that it has minimal height.
	 * @return A copy of the tree which has been changed so that it is balanced.
	 */
	public Bst<Key, Value> balanced() {
		@SuppressWarnings("unchecked")
		Entry<Key,Value>[] a = (Entry<Key,Value>[]) Array.newInstance(Entry.class, size());

		saveInOrder(a);
		return arrayToBst(a, 0, a.length-1);
	}

	/**
	 * A recursive procedure that does the work of balancing the binary search tree
	 * by keeping track of the start and end indices of the array.
	 * @param a The array to build a tree from.
	 * @param start The index to start from.
	 * @param end The index to stop at.
	 * @return A balanced tree.
	 */
	private Bst<Key,Value> arrayToBst(Entry<Key, Value>[] a, int start, int end) {
		if(start == end && end == a.length-1) {
			return new Fork<Key,Value>(a[end].getKey(), a[end].getValue(),
					new Empty<Key,Value>(), new Empty<Key,Value>());
		}
		else if(start == end) {
			return new Empty<Key,Value>();
		}
		else {
			int midpoint = ((start + end)/2);
			return new Fork<Key,Value>(a[midpoint].getKey(), a[midpoint].getValue(),
					arrayToBst(a, start, midpoint), arrayToBst(a, midpoint+1, end));
		}	
	}
}
