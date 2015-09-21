
import android.app.Activity;
import android.content.Context;
import android.test.mock.MockContext;

import com.jugarte.gourmet.beans.Gourmet;
import com.jugarte.gourmet.builders.GourmetInternalBuilder;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;



/**
 * Created by javiergon on 30/07/15.
 */
public class GourmetBuilderTest  {

    private TestUtils utils = null;
    private Context context= null;
    private Activity activity;

    @Before
    public void setUp() {
        this.utils = new TestUtils();
        context = new MockContext();
    }

    @Test
    public void testResponseOk() throws Exception {
        String data = this.utils.getResourceToString("response_ok.html");

        GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(context);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, data);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, "0000000000000");
        gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, "20/09/2015");
        Gourmet gourmet = (Gourmet) gourmetBuilder.build();

        assertEquals(gourmet.errorCode, "0");

        assertEquals(gourmet.currentBalance, "34,56");
        assertNotNull(gourmet.operations);
        assertTrue(gourmet.operations.size() == 9);
        assertEquals(gourmet.operations.get(0).name, "BURGER KING QUEVEDO");
        assertEquals(gourmet.operations.get(0).price, "7,45");
        assertEquals(gourmet.operations.get(0).date, "05/07/2015");
        assertEquals(gourmet.operations.get(0).hour, "22:58");

        assertEquals(gourmet.operations.get(1).name, "COECOE HOSTELEROS");

        assertEquals(gourmet.operations.get(3).name, "DI BOCCA RESTAURACIO");
        assertEquals(gourmet.operations.get(3).price, "11,95");

        assertEquals(gourmet.operations.get(8).name, "BURGER KING QUEVEDO");
        assertEquals(gourmet.operations.get(8).price, "1,49");
        assertEquals(gourmet.operations.get(8).date, "21/06/2015");
        assertEquals(gourmet.operations.get(8).hour, "21:55");
    }

    @Test
    public void testResponseOk2() throws Exception {
        String data = this.utils.getResourceToString("response_ok2.html");

        GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(context);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, data);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, "0000000000000");
        gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, "20/09/2015");
        Gourmet gourmet = (Gourmet) gourmetBuilder.build();

        assertEquals(gourmet.errorCode, "0");

        assertEquals(gourmet.currentBalance, "101,89");
        assertNotNull(gourmet.operations);
        assertTrue(gourmet.operations.size() == 9);
        assertEquals(gourmet.operations.get(0).name, "DI BOCCA RESTAURACIO");
        assertEquals(gourmet.operations.get(0).price, "23,90");
        assertEquals(gourmet.operations.get(0).date, "20/08/2015");
        assertEquals(gourmet.operations.get(0).hour, "15:51");

        assertEquals(gourmet.operations.get(1).name, "BK TALAVERA GOLF");

        assertEquals(gourmet.operations.get(2).name, "BAR-RESTAURANTE USE");
        assertEquals(gourmet.operations.get(3).name, "Actualizaci�n de saldo");
        assertEquals(gourmet.operations.get(4).name, "BURGER KING QUEVEDO");
    }

    @Test
    public void testResponseWithoutOperations() throws Exception {
        String data = this.utils.getResourceToString("response_okwithoutops.html");

        GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(context);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, data);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, "0000000000000");
        gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, "20/09/2015");
        Gourmet gourmet = (Gourmet) gourmetBuilder.build();

        assertEquals(gourmet.errorCode, "0");

        assertNotNull(gourmet.currentBalance);
        assertNull(gourmet.operations);
    }

    @Test
    public void testResponseFail() throws Exception {
        String data = this.utils.getResourceToString("response_fail.html");

        GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(context);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, data);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, "0000000000000");
        gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, "20/09/2015");
        Gourmet gourmet = (Gourmet) gourmetBuilder.build();

        assertEquals(gourmet.errorCode, "2");

        assertNull(gourmet.currentBalance);
        assertNull(gourmet.operations);
    }

    @Test
    public void testResponseEmptyAndNull() throws Exception {
        String data = "";
        GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(context);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, data);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, "0000000000000");
        gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, "20/09/2015");
        Gourmet gourmet = (Gourmet) gourmetBuilder.build();

        assertNull(gourmet);

        data = null;
        gourmetBuilder = new GourmetInternalBuilder(context);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, data);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, "0000000000000");
        gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, "20/09/2015");
        gourmet = (Gourmet) gourmetBuilder.build();

        assertNull(gourmet);

        data = "ldkjfalkdjfoasdjfalkdjfalñkdjfañldkjfalñkdjf";
        gourmetBuilder = new GourmetInternalBuilder(context);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, data);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, "0000000000000");
        gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, "20/09/2015");
        gourmet = (Gourmet) gourmetBuilder.build();

        assertNull(gourmet);
    }

    @Test
    public void testHelperClass() throws Exception {

        GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(context);

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
