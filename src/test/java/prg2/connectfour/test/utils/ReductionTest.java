package prg2.connectfour.test.utils;

import org.junit.Assert;
import org.junit.Test;
import prg2.connectfour.utils.Pair;
import prg2.connectfour.utils.Reduction;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ReductionTest {
    @Test
    public void testReducesCorrectly() {
        List<String> values = Arrays.asList(
                "1", "1",
                "2",
                "3", "3", "3", "3",
                "2"
        );

        Stack<Pair<String, Integer>> stack = Reduction.reduceList(values, t -> t);
        Pair<String, Integer> pair;

        pair = stack.pop();
        Assert.assertEquals("2", pair.getLeft());
        Assert.assertEquals((Integer) 1, pair.getRight());

        pair = stack.pop();
        Assert.assertEquals("3", pair.getLeft());
        Assert.assertEquals((Integer) 4, pair.getRight());

        pair = stack.pop();
        Assert.assertEquals("2", pair.getLeft());
        Assert.assertEquals((Integer) 1, pair.getRight());

        pair = stack.pop();
        Assert.assertEquals("1", pair.getLeft());
        Assert.assertEquals((Integer) 2, pair.getRight());
    }
}
