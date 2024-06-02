package simple.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import simple.Simple;

public class testSimple {

    @Test
    public void testSimple() {
        final int N = 247;
        Simple s = new Simple();
        s.setData(N);
        assertEquals("Value gotten from object does not match value put in",
                N, s.getData());
    }

}
