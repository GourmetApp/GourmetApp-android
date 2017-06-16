
import com.jugarte.gourmet.data.chequegourmet.ChequeGourmet;
import com.jugarte.gourmet.data.chequegourmet.ChequeGourmetBuilder;
import com.jugarte.gourmet.exceptions.NotFoundException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class GourmetBuilderTest {

    private TestUtils utils = null;

    @Before
    public void setUp() {
        this.utils = new TestUtils();
    }

    @Test
    public void testResponseOk() throws Exception {
        String data = this.utils.getResourceToString("response_ok.html");

        ChequeGourmetBuilder gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");


        assertEquals(gourmet.getBalance(), "34,56");
        assertNotNull(gourmet.getOperations());
        assertTrue(gourmet.getOperations().size() == 9);
        assertEquals(gourmet.getOperations().get(0).getName(), "BURGER KING QUEVEDO");
        assertEquals(gourmet.getOperations().get(0).getPrice(), "7,45");
        assertEquals(gourmet.getOperations().get(0).getDate(), "05/07/2015");
        assertEquals(gourmet.getOperations().get(0).getHour(), "22:58");

        assertEquals(gourmet.getOperations().get(1).getName(), "COECOE HOSTELEROS");

        assertEquals(gourmet.getOperations().get(3).getName(), "DI BOCCA RESTAURACIO");
        assertEquals(gourmet.getOperations().get(3).getPrice(), "11,95");

        assertEquals(gourmet.getOperations().get(8).getName(), "BURGER KING QUEVEDO");
        assertEquals(gourmet.getOperations().get(8).getPrice(), "1,49");
        assertEquals(gourmet.getOperations().get(8).getDate(), "21/06/2015");
        assertEquals(gourmet.getOperations().get(8).getHour(), "21:55");
    }

    @Test
    public void testResponseOk2() throws Exception {
        String data = this.utils.getResourceToString("response_ok2.html");

        ChequeGourmetBuilder gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");

        assertEquals(gourmet.getBalance(), "101,89");
        assertNotNull(gourmet.getOperations());
        assertTrue(gourmet.getOperations().size() == 9);
        assertEquals(gourmet.getOperations().get(0).getName(), "DI BOCCA RESTAURACIO");
        assertEquals(gourmet.getOperations().get(0).getPrice(), "23,90");
        assertEquals(gourmet.getOperations().get(0).getDate(), "20/08/2015");
        assertEquals(gourmet.getOperations().get(0).getHour(), "15:51");

        assertEquals(gourmet.getOperations().get(1).getName(), "BK TALAVERA GOLF");

        assertEquals(gourmet.getOperations().get(2).getName(), "BAR-RESTAURANTE USE");
        assertEquals(gourmet.getOperations().get(3).getName(), "Actualizaci�n de saldo");
        assertEquals(gourmet.getOperations().get(4).getName(), "BURGER KING QUEVEDO");
    }

    @Test
    public void testResponseWithoutOperations() throws Exception {
        String data = this.utils.getResourceToString("response_okwithoutops.html");

        ChequeGourmetBuilder gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");

        assertNotNull(gourmet.getBalance());
        assertNull(gourmet.getOperations());
    }

    @Test(expected = NotFoundException.class)
    public void testResponseFail() throws Exception {
        String data = this.utils.getResourceToString("response_fail.html");

        ChequeGourmetBuilder gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");

        assertNull(gourmet.getBalance());
        assertNull(gourmet.getOperations());
    }

    @Test
    public void testResponseEmptyAndNull() throws Exception {
        String data = "";
        ChequeGourmetBuilder gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");

        assertNull(gourmet);

        data = null;

        gourmetBuilder = new ChequeGourmetBuilder();
        gourmet = gourmetBuilder.build(data, "0000000000000");

        assertNull(gourmet);

        data = "ldkjfalkdjfoasdjfalkdjfalñkdjfañldkjfalñkdjf";
        gourmetBuilder = new ChequeGourmetBuilder();
        gourmet = gourmetBuilder.build(data, "0000000000000");

        assertNull(gourmet);
    }

    @Test
    public void testHelperClass() throws Exception {

        ChequeGourmetBuilder gourmetBuilder = new ChequeGourmetBuilder();

        assertEquals(gourmetBuilder.cleanString(" hola "), "hola");
        assertEquals(gourmetBuilder.cleanString(" hola    "), "hola");
        assertEquals(gourmetBuilder.cleanString(" hola  \n  \t  "), "hola");
        assertEquals(gourmetBuilder.cleanString(" hola paco  \n  \t  "), "hola paco");

        assertEquals(gourmetBuilder.removeLastWord("Hola"), "Hola");
        assertEquals(gourmetBuilder.removeLastWord("Hola d"), "Hola");
        assertEquals(gourmetBuilder.removeLastWord("Actualizacion de saldo"), "Actualizacion de saldo");
        assertEquals(gourmetBuilder.removeLastWord("Restaurante M"), "Restaurante");
        assertEquals(gourmetBuilder.removeLastWord(" Restaurante  M"), "Restaurante");

    }

}
