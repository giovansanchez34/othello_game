package gamelogic;

/**
 * Pair is small utility class used to store two types of objects.
 */
public class Pair<T, V>
{
    // a prime number that will aid in determining a fair hash code for the current pair object
    private static final int HASH_FACTOR = 31;

    public T first;
    public V second;

    public Pair(T first, V second)
    {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns true if both pairs have the same contents. Otherwise, they are not equal.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || obj.getClass() != getClass())
        {
            return false;
        }
        Pair<Integer, Integer> pair = (Pair<Integer, Integer>) obj;
        return pair.first.equals(first) && pair.second.equals(second);
    }

    /**
     * Determinse and Returns the hashcode for this pair.
     */
    @Override
    public int hashCode()
    {
        return first.hashCode() * HASH_FACTOR + second.hashCode() * HASH_FACTOR;
    }

}