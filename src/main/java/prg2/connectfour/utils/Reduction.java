package prg2.connectfour.utils;

import java.util.Stack;
import java.util.function.Function;

/**
 * Helper class for reduction
 *
 * @author Marius Schuler {@literal <binarytenshi@gmail.com>}
 */
public abstract class Reduction {
    /**
     * Reduces the given list.
     * <br>
     * Extracts the key with the given keySelector.
     * <br>
     * Reduces to the number of adjacent keys
     *
     * @param iterable    Input list
     * @param keySelector Function to extract the key from the object
     * @param <T>         List item
     * @param <K>         Key
     * @return Reduced list
     */
    public static <T, K> Stack<Pair<K, Integer>> reduceList(Iterable<T> iterable, Function<T, K> keySelector) {
        Stack<Pair<K, Integer>> stack = new Stack<>();

        for (T item : iterable) {
            K key = keySelector.apply(item);
            add(stack, key);
        }

        return stack;
    }

    /**
     * Add a key to the stack.
     * <br>
     * If the key is on the top, increment its value.
     *
     * @param stack Stack
     * @param key   Key
     * @param <K>   Key type
     */
    private static <K> void add(Stack<Pair<K, Integer>> stack, K key) {
        Pair<K, Integer> pair;

        if (stack.isEmpty()) {
            pair = new Pair<>(key, 1);
        } else {
            pair = stack.pop();

            if (pair.left != key) {
                stack.push(pair);
                pair = new Pair<>(key, 0);
            }

            pair = new Pair<>(key, pair.right + 1);
        }

        stack.push(pair);
    }
}
