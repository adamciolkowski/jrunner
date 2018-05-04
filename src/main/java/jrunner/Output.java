package jrunner;

import java.io.OutputStream;

public class Output {

    private final OutputStream output;
    private final OutputStream error;

    public Output(OutputStream output, OutputStream error) {
        this.output = output;
        this.error = error;
    }

    public OutputStream getOutputStream() {
        return output;
    }

    public OutputStream getErrorStream() {
        return error;
    }
}
