package mumbler.simple.env;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EnvironmentTest {
    
    private Environment env;
    
    @Before
    public void setUp() {
        this.env = new Environment();
    }
    
    @Test
    public void testGetValue() {
        this.env.putValue("foo", new Long(1));
        assertEquals(1L, this.env.getValue("foo"));
    }

}
