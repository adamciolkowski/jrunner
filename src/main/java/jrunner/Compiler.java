package jrunner;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Compiler {

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private final Path outputDir;

    public Compiler(Path outputDir) {
        this.outputDir = outputDir;
    }

    public void compile(InputStream in) throws IOException {
        Path source = outputDir.resolve("Main.java");
        try {
            Files.copy(in, source);
            compile(source);
        } finally {
            Files.delete(source);
        }
    }

    private void compile(Path source) {
        ByteArrayOutputStream error = new ByteArrayOutputStream();
        int exitCode = compiler.run(System.in, System.out, error, source.toString());
        if (exitCode != 0)
            throw new CompilationFailedException(error.toString());
    }

}
