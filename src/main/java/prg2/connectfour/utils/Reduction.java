package prg2.connectfour.utils;

import java.util.Stack;
import java.util.function.Function;

public abstract class Reduction {
    public static <T, K> Stack<Pair<K, Integer>> reduceList(Iterable<T> iterable, Function<T, K> keySelector) {
        Stack<Pair<K, Integer>> stack = new Stack<>();

        for (T item : iterable) {
            K key = keySelector.apply(item);
            add(stack, key);
        }

        return stack;
    }

    private static <K> void add(Stack<Pair<K, Integer>> stack, K key) {
        Pair<K, Integer> pair;

        if (stack.isEmpty()) {
            pair = new Pair<>(key, 1);
        } else {
            pair = stack.pop();

            if (pair.getLeft() != key) {
                stack.push(pair);
                pair = new Pair<>(key, 0);
            }

            pair = new Pair<>(key, pair.getRight() + 1);
        }

        stack.push(pair);
    }
}
