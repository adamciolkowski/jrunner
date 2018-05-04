package jrunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoUtils {

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
