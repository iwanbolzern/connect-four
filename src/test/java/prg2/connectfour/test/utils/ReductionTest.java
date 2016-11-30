package prg2.connectfour.test.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prg2.connectfour.utils.Pair;
import prg2.connectfour.utils.Reduction;

import java.util.Stack;

public class ReductionTest {
    private Reduction<String> subject;

    @Before
    public void setup() {
        this.subject = new Reduction<>();
    }

    @Test
    public void testReducesCorrectly() {
        String[] values = {
                "1", "1",
                "2",
                "3", "3", "3", "3",
                "2"
        };

        for (String str : values) {
            this.subject.add(str);
        }

        Stack<Pair<String, Integer>> stack = this.subject.getStack();
        Pair<String, Integer> pair;

        pair = stack.pop();
        Assert.assertEquals("2", pair.getLeft());
        Assert.assertEquals((Integer)1, pair.getRight());

        pair = stack.pop();
        Assert.assertEquals("3", pair.getLeft());
        Assert.assertEquals((Integer)4, pair.getRight());

        pair = stack.pop();
        Assert.assertEquals("2", pair.getLeft());
        Assert.assertEquals((Integer)1, pair.getRight());

        pair = stack.pop();
        Assert.assertEquals("1", pair.getLeft());
        Assert.assertEquals((Integer)2, pair.getRight());
    }
}
