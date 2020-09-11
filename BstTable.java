import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * An implementation of tables using binary search trees.
 * @author James Birch
 */
public class BstTable<Key extends Comparable<Key>,Value> implements Table<Key, Value> {

	private Bst<Key,Value> bst;
	
	public BstTable() {
		bst = new Empty<Key,Value>();
	}
	
	/**
	 * @param bst A binary search tree
	 */
	public BstTable(Bst<Key,Value> bst) {
		this.bst = bst;
	}
	
	@Override
	/**
	 * Checks to see if a binary search tree contains a key.
	 * @param k The key to check.
	 * @return A boolean for whether that key exists.
	 */
	public boolean containsKey(Key k) {
		return bst.has(k);
	}

	@Override
	/**
	 * Gets the relevant value from the binary search tree for a key, if it exists.
	 * @param k The key to try and find.
	 * @return The value as an option type (empty option if it doesn't exist).
	 */
	public Optional<Value> get(Key k) {
		return bst.find(k);
	}

	@Override
	/**
	 * Checks to see if the binary search tree is empty.
	 * @return A boolean for whether the binary search tree is empty or not.
	 */
	public boolean isEmpty() {
		return bst.isEmpty();
	}

	@Override
	/**
	 * Inserts a key and value into the table.
	 * @param k The key to insert.
	 * @param v The value to insert.
	 * @return A new table with the key and value inserted.
	 */
	public Table<Key, Value> put(Key k, Value v) {
		return new BstTable<Key,Value>(bst.put(k, v));
	}

	@Override
	/**
	 * Removes a key and its corresponding value from the table.
	 * @param k The key to remove.
	 * @return A new table of optional type with the key removed (empty type if empty table).
	 */
	public Optional<Table<Key, Value>> remove(Key k) {
		return Optional.ofNullable(new BstTable<Key,Value>(bst.delete(k).get()));
	}

	@Override
	/**
	 * The size of the table.
	 * @return The size of the table as an int.
	 */
	public int size() {
		return bst.size();
	}
	
	@Override
	/**
	 * A collection of values held in the table.
	 * @return An ArrayList which stores all of the values in the table.
	 */
	public Collection<Value> values() {
		Entry<Key,Value>[] array = getArray(bst);
		Collection<Value> values = new ArrayList<Value>();
		for(Entry<Key,Value> entry : array) {
			values.add(entry.getValue());
		}
		return values;
	}

	@Override
	/**
	 * A collection of keys held in the table.
	 * @return An ArrayList which stores all of the keys in the table.
	 */
	public Collection<Key> keys() {
		Entry<Key,Value>[] array = getArray(bst);
		Collection<Key> keys = new ArrayList<Key>();
		for(Entry<Key,Value> entry : array) {
			keys.add(entry.getKey());
		}
		return keys;
	}
	
	/**
	 * Creates an array from the binary search tree held in the table.
	 * @param bst A binary search tree.
	 * @return An array of type Entry<Key,Value>.
	 */
	private Entry<Key,Value>[] getArray(Bst<Key,Value> bst) {
		@SuppressWarnings("unchecked")
	    Entry<Key,Value>[] a = (Entry<Key,Value>[]) Array.newInstance(Entry.class, size());
	    bst.saveInOrder(a);
	    return a;
	}
}
