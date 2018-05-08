package jrunner;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestUtil {

    public static Reader newReader(String resourceName) {
        InputStream in = TestUtil.class.getResourceAsStream(resourceName);
        return new InputStreamReader(in, UTF_8);
    }
}
