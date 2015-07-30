import org.junit.Before;

/**
 * Created by javiergon on 30/07/15.
 */
public class BaseTest {

    public TestUtils utils = null;

    @Before
    public void setUp() {
        this.utils = new TestUtils();
    }
}
