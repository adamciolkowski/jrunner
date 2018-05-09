package jrunner;

import java.io.IOException;

public class JavaAppRunner {

    public void run(JavaApp app, Output output) throws IOException, InterruptedException {
        String[] command = {
                "java",
                "-cp", app.getClassPath().toString(),
                "-Djava.security.manager",
                "-Xms2m", "-Xmx8m",
                app.getMainClassName()
        };

        Process process = new ProcessBuilder(command).start();
        IoUtils.copyStream(process.getInputStream(), output.getOutputStream());
        IoUtils.copyStream(process.getErrorStream(), output.getErrorStream());
        process.waitFor();
    }

}
