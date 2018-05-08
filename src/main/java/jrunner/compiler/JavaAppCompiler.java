package jrunner.compiler;

import jrunner.JavaApp;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaAppCompiler {

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private final Path outputDir;

    public JavaAppCompiler(Path outputDir) {
        this.outputDir = outputDir;
    }

    public JavaApp compile(Reader sourceCode) throws IOException {
        String mainClassName = "Main";
        Path source = outputDir.resolve(mainClassName + ".java");
        try {
            copy(source, sourceCode);
            compile(source);
            return new JavaApp(outputDir, mainClassName);
        } finally {
            Files.delete(source);
        }
    }

    private void copy(Path source, Reader reader) throws IOException {
        try (Writer writer = Files.newBufferedWriter(source)) {
            char[] buffer = new char[4096];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, read);
            }
        }
    }

    private void compile(Path source) {
        ByteArrayOutputStream error = new ByteArrayOutputStream();
        int exitCode = compiler.run(System.in, System.out, error, source.toString());
        if (exitCode != 0)
            throw new CompilationFailedException(error.toString());
    }

}
