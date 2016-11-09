
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
public class GourmetBuilderTest {

    private TestUtils utils = null;
    private Context context = null;
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

        assertEquals(gourmet.getErrorCode(), "0");

        assertEquals(gourmet.getCurrentBalance(), "34,56");
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

        GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(context);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, data);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, "0000000000000");
        gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, "20/09/2015");
        Gourmet gourmet = (Gourmet) gourmetBuilder.build();

        assertEquals(gourmet.getErrorCode(), "0");

        assertEquals(gourmet.getCurrentBalance(), "101,89");
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

        GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(context);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, data);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, "0000000000000");
        gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, "20/09/2015");
        Gourmet gourmet = (Gourmet) gourmetBuilder.build();

        assertEquals(gourmet.getErrorCode(), "0");

        assertNotNull(gourmet.getCurrentBalance());
        assertNull(gourmet.getOperations());
    }

    @Test
    public void testResponseFail() throws Exception {
        String data = this.utils.getResourceToString("response_fail.html");

        GourmetInternalBuilder gourmetBuilder = new GourmetInternalBuilder(context);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_JSON, data);
        gourmetBuilder.append(GourmetInternalBuilder.DATA_CARD_NUMBER, "0000000000000");
        gourmetBuilder.append(GourmetInternalBuilder.DATA_MODIFICATION_DATE, "20/09/2015");
        Gourmet gourmet = (Gourmet) gourmetBuilder.build();

        assertEquals(gourmet.getErrorCode(), "2");

        assertNull(gourmet.getCurrentBalance());
        assertNull(gourmet.getOperations());
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
