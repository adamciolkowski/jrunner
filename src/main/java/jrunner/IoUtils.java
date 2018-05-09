package jrunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IoUtils {

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void copy(Reader reader, Writer writer) throws IOException {
        char[] buffer = new char[4096];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, read);
        }
    }
}
