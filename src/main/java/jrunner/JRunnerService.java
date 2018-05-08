package jrunner;

import jrunner.compiler.JavaAppCompiler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class JRunnerService {

    public void compileAndRun(String sourceCode, Output output) throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("jrunner");
        JavaAppCompiler compiler = new JavaAppCompiler(tempDir);
        JavaApp app = compiler.compile(new StringReader(sourceCode));

        JavaAppRunner runner = new JavaAppRunner();
        runner.run(app, output);
    }
}
