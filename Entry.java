// Do not modify this class.

/**
 * A generic class for entries.
 * @author Dr. Martin Escardo
 */
public class Entry<Key extends Comparable<Key>,Value> {

    private final Key key;
    private final Value value;

    public Entry(Key key, Value value) {
      this.key = key;
      this.value = value;
    }

    public Key getKey() {
      return key;
    }

    public Value getValue() {
      return value;
    }
}
