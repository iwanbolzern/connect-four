package prg2.connectfour.utils;

import java.util.Stack;

public class Reduction<K> {
    private Stack<Pair<K, Integer>> stack;

    public Reduction() {
        this.stack = new Stack<>();
    }

    public void add(K key) {
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

    public Stack<Pair<K, Integer>> getStack() {
        return stack;
    }
}
