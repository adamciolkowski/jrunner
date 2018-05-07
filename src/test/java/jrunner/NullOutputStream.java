package jrunner;

import java.io.OutputStream;

public class NullOutputStream extends OutputStream {

    @Override
    public void write(int b) { }
}
