
import com.jugarte.gourmet.data.chequegourmet.ChequeGourmet;
import com.jugarte.gourmet.data.chequegourmet.ChequeGourmetBuilder;
import com.jugarte.gourmet.exceptions.ConnectionException;
import com.jugarte.gourmet.exceptions.EmptyException;
import com.jugarte.gourmet.exceptions.NotFoundException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class GourmetBuilderTest {

    private TestUtils utils = null;
    ChequeGourmetBuilder gourmetBuilder;

    @Before
    public void setUp() {
        this.utils = new TestUtils();
        this.gourmetBuilder = new ChequeGourmetBuilder();
    }

    @Test
    public void testBalanceIsOk() throws Exception {
        String data = this.utils.getResourceToString("response_ok.html");

        gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");


        assertEquals(gourmet.getBalance(), "34,56");
    }

    @Test
    public void testHaveNineOperations() throws Exception {
        String data = this.utils.getResourceToString("response_ok.html");

        gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");

        assertEquals(gourmet.getOperations().size(), 9);
    }

    @Test
    public void testFirstOperationIsOk() throws Exception {
        String data = this.utils.getResourceToString("response_ok.html");

        gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");

        assertEquals(gourmet.getOperations().get(0).getName(), "BURGER KING QUEVEDO");
        assertEquals(gourmet.getOperations().get(0).getPrice(), "7,45");
        assertEquals(gourmet.getOperations().get(0).getDate(), "05/07/2015");
        assertEquals(gourmet.getOperations().get(0).getHour(), "22:58");
    }

    @Test
    public void testLastOperationIsOk() throws Exception {
        String data = this.utils.getResourceToString("response_ok.html");

        gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");

        assertEquals(gourmet.getOperations().get(8).getName(), "BURGER KING QUEVEDO");
        assertEquals(gourmet.getOperations().get(8).getPrice(), "1,49");
        assertEquals(gourmet.getOperations().get(8).getDate(), "21/06/2015");
        assertEquals(gourmet.getOperations().get(8).getHour(), "21:55");
    }

    @Test
    public void testResponseOkWithOtherResponse() throws Exception {
        String data = this.utils.getResourceToString("response_ok2.html");

        gourmetBuilder = new ChequeGourmetBuilder();
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
    public void testResponseIsNullWhenWithoutOperations() throws Exception {
        String data = this.utils.getResourceToString("response_okwithoutops.html");

        gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");

        assertNotNull(gourmet.getBalance());
        assertNull(gourmet.getOperations());
    }

    @Test(expected = NotFoundException.class)
    public void testResponseFail() throws Exception {
        String data = this.utils.getResourceToString("response_fail.html");

        gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");

        assertNull(gourmet.getBalance());
        assertNull(gourmet.getOperations());
    }

    @Test(expected = EmptyException.class)
    public void testResultIsNullWhenResponseIsEmpty() throws Exception {
        String data = "";

        gourmetBuilder = new ChequeGourmetBuilder();
        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");

        assertNull(gourmet);
    }

    @Test(expected = ConnectionException.class)
    public void testResultIsNullWhenResponseIsNull() throws Exception {
        ChequeGourmet gourmet = gourmetBuilder.build(null, "0000000000000");

        assertNull(gourmet);
    }

    @Test(expected = EmptyException.class)
    public void testResultIsNullWhenResponseIsStrange() throws Exception {
        String data = "ldkjfalkdjfoasdjfalkdjfalñkdjfañldkjfalñkdjf";

        ChequeGourmet gourmet = gourmetBuilder.build(data, "0000000000000");

        assertNull(gourmet);
    }

    @Test
    public void testCleanTextWhenHaveSpaces() throws Exception {
        assertEquals(gourmetBuilder.cleanString(" hola "), "hola");
    }

    @Test
    public void testCleanTextWhenHaveTabs() throws Exception {
        assertEquals(gourmetBuilder.cleanString(" hola    "), "hola");
    }

    @Test
    public void testCleanTextWhenHaveTabsSymbols() throws Exception {
        assertEquals(gourmetBuilder.cleanString(" hola  \n  \t  "), "hola");
        assertEquals(gourmetBuilder.cleanString(" hola paco  \n  \t  "), "hola paco");
    }

    @Test
    public void testNotRemoveLastWord() throws Exception {
        assertEquals(gourmetBuilder.removeLastWord("Hola"), "Hola");
    }

    @Test
    public void testRemoveLastWord() throws Exception {
        assertEquals(gourmetBuilder.removeLastWord("Hola d"), "Hola");
    }

    @Test
    public void testNotRemoveLastWordWhenIsUpdate() throws Exception {
        assertEquals(gourmetBuilder.removeLastWord("Actualizacion de saldo"), "Actualizacion de saldo");
    }

    @Test
    public void testRemoveLastWordWhenIsACountry() throws Exception {
        assertEquals(gourmetBuilder.removeLastWord("Restaurante M"), "Restaurante");
    }

    @Test
    public void testRemoveLastWordAndCleanSpaces() throws Exception {
        assertEquals(gourmetBuilder.removeLastWord(" Restaurante  M"), "Restaurante");
    }

}
