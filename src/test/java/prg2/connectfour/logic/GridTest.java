import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class GridTest {
    private Grid subject;
    private int width = 10;
    private int height = 5;

    @Before
    public void setup() {
        this.subject = new Grid(width, height);
    }

    @Test
    public void testInvalidPosition() {
        Cell c = this.subject.getCellAt(10, 10);
        Assert.assertNull(c);
    }

    @Test
    public void testValidPosition() {
        Cell c = this.subject.getCellAt(0, 0);
        Assert.assertNotNull(c);
    }
}
