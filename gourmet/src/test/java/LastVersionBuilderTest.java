import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.builders.LastVersionBuilder;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


/**
 * Created by javiergon on 30/07/15.
 */
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
        lastVersionBuilder.append(LastVersionBuilder.DATA_JSON, data);
        LastVersion lastVersion = (LastVersion) lastVersionBuilder.build();

        assertNotNull(lastVersion);
        assertEquals(lastVersion.getIdVersion(), "1603617");
        assertEquals(lastVersion.getNameTagVersion(), "v1.0.1");
        assertEquals(lastVersion.getNameVersion(), "Release v1.0.1");
        assertEquals(lastVersion.getIdDownload(), "753792");
        assertEquals(lastVersion.getNameDownload(), "GourmetApp-v1.0.1.apk");
        assertEquals(lastVersion.getUrlDownload(), "https://github.com/javierugarte/GourmetApp-android/releases/download/v1.0.1/GourmetApp-v1.0.1.apk");
        assertEquals(lastVersion.getUrlHomePage(), "https://gourmetapp.github.io/android/");
        assertNotNull(lastVersion.getChangelog());
        // This test not found
        assertEquals(lastVersion.getChangelog(), "* Spanish translation<br>* Exit login when the user has changed the password<br>* Fixed fonts of EditText");
    }

    @Test
    public void testResponseWithoutAssets() throws Exception {
        String data = this.utils.getResourceToString("lastversion_without_assets.json");
        LastVersionBuilder lastVersionBuilder = new LastVersionBuilder();
        lastVersionBuilder.append(LastVersionBuilder.DATA_JSON, data);
        LastVersion lastVersion = (LastVersion) lastVersionBuilder.build();

        assertNotNull(lastVersion);
        assertEquals(lastVersion.getIdVersion(), "1603617");
        assertEquals(lastVersion.getNameTagVersion(), "v1.0.1");
        assertEquals(lastVersion.getNameVersion(), "Release v1.0.1");
        assertEquals(lastVersion.getIdDownload(), null);
        assertEquals(lastVersion.getNameDownload(), null);
        assertEquals(lastVersion.getUrlDownload(), "https://github.com/javierugarte/GourmetApp-android/releases/tag/v1.0.1");
        assertEquals(lastVersion.getUrlHomePage(), "https://gourmetapp.github.io/android/");
        assertNotNull(lastVersion.getChangelog());
        // This test not found
        assertEquals(lastVersion.getChangelog(), "* Spanish translation<br>* Exit login when the user has changed the password<br>* Fixed fonts of EditText");
    }

    @Test
    public void testResponseWithoutReleases() throws Exception {
        String data = this.utils.getResourceToString("lastversion_without_releases.json");
        LastVersionBuilder lastVersionBuilder = new LastVersionBuilder();
        lastVersionBuilder.append(LastVersionBuilder.DATA_JSON, data);
        LastVersion lastVersion = (LastVersion) lastVersionBuilder.build();

        assertNull(lastVersion);
    }

    @Test
    public void testResponseEmptyAndNull() throws Exception {
        String data = "";
        LastVersionBuilder lastVersionBuilder = new LastVersionBuilder();
        lastVersionBuilder.append(LastVersionBuilder.DATA_JSON, data);
        LastVersion lastVersion = (LastVersion) lastVersionBuilder.build();

        assertNull(lastVersion);


        data = null;
        lastVersionBuilder = new LastVersionBuilder();
        lastVersionBuilder.append(LastVersionBuilder.DATA_JSON, data);
        lastVersion = (LastVersion) lastVersionBuilder.build();

        assertNull(lastVersion);


        data = "ldkjfalkdjfoasdjfalkdjfalñkdjfañldkjfalñkdjf";
        lastVersionBuilder = new LastVersionBuilder();
        lastVersionBuilder.append(LastVersionBuilder.DATA_JSON, data);
        lastVersion = (LastVersion) lastVersionBuilder.build();

        assertNull(lastVersion);

    }

}