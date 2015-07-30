import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by javiergon on 30/07/15.
 */
public class TestUtils {

    public String getResourceToString(String resource) throws Exception {
        String path = getClass().getResource("/").toURI().getPath() + "../resources/" + resource;
        System.out.println(path);

        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public String getResourceFromPath(String path) throws Exception {
        String resourcePath = this.getClass().getResource("/").toURI().getPath() + path;
        System.out.println(resourcePath);

        BufferedReader br = new BufferedReader(new FileReader(resourcePath));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

}
