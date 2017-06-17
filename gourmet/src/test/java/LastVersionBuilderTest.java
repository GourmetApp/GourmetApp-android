import com.jugarte.gourmet.domine.beans.LastVersion;
import com.jugarte.gourmet.data.github.LastVersionBuilder;
import com.jugarte.gourmet.exceptions.ConnectionException;
import com.jugarte.gourmet.exceptions.NotFoundException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class LastVersionBuilderTest {

    public TestUtils utils = null;

    @Before
    public void setUp() {
        this.utils = new TestUtils();
    }

    @Test
    public void testResponseOk() throws Exception {
        String data = this.utils.getResourceToString("lastversion_ok.json");

        LastVersionBuilder lastVersionBuilder = new LastVersionBuilder();
        LastVersion lastVersion = lastVersionBuilder.build(data);

        assertNotNull(lastVersion);
        assertEquals(lastVersion.getNameTagVersion(), "v1.0.1");
        assertEquals(lastVersion.getNameVersion(), "Release v1.0.1");
        assertEquals(lastVersion.getNameDownload(), "GourmetApp-v1.0.1.apk");
        assertEquals(lastVersion.getUrlDownload(), "https://github.com/javierugarte/GourmetApp-android/releases/download/v1.0.1/GourmetApp-v1.0.1.apk");
        assertEquals(lastVersion.getUrlHomePage(), "https://gourmetapp.github.io/android/");

    }

    @Test
    public void testIsChangelogOk() throws Exception {
        String data = this.utils.getResourceToString("lastversion_ok.json");

        LastVersionBuilder lastVersionBuilder = new LastVersionBuilder();
        LastVersion lastVersion = lastVersionBuilder.build(data);
        assertEquals(lastVersion.getChangelog(), "* Spanish translation<br>* Exit login when the user has changed the password<br>* Fixed fonts of EditText");
    }

    @Test
    public void testIsNullWhenResponseWithoutReleases() throws Exception {
        String data = this.utils.getResourceToString("lastversion_without_releases.json");
        LastVersionBuilder lastVersionBuilder = new LastVersionBuilder();
        LastVersion lastVersion = lastVersionBuilder.build(data);

        assertNull(lastVersion);
    }

    @Test(expected = ConnectionException.class)
    public void testConnectionExceptionWhenResponseIsEmpty() throws ConnectionException, JSONException {
        String data = "";
        LastVersionBuilder lastVersionBuilder = new LastVersionBuilder();
        LastVersion lastVersion = lastVersionBuilder.build(data);

        assertNull(lastVersion);

    }

    @Test(expected = ConnectionException.class)
    public void testConnectionExceptionWhenResponseIsNull() throws ConnectionException, JSONException {
        LastVersionBuilder lastVersionBuilder = new LastVersionBuilder();
        LastVersion lastVersion = lastVersionBuilder.build(null);

        assertNull(lastVersion);
    }

    @Test(expected = JSONException.class)
    public void testConnectionExceptionWhenResponseIsStrong() throws ConnectionException, JSONException {

        String data = "ldkjfalkdjfoasdjfalkdjfalñkdjfañldkjfalñkdjf";
        LastVersionBuilder lastVersionBuilder = new LastVersionBuilder();
        LastVersion lastVersion = lastVersionBuilder.build(data);

        assertNull(lastVersion);

    }

}