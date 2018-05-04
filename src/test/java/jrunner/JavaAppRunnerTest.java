package jrunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import static java.nio.file.Files.createTempDirectory;
import static java.nio.file.Files.walkFileTree;
import static org.assertj.core.api.Assertions.assertThat;

public class JavaAppRunnerTest {

    Path outputDir;

    JavaAppRunner runner = new JavaAppRunner();

    @Before
    public void createOutputDirectory() throws Exception {
        outputDir = createTempDirectory("jrunner");
    }

    @Test
    public void shouldRunJavaCodeAndWriteOutputToGivenStream() throws Exception {
        JavaApp javaApp = compileJavaApp("/PrintsToStreams.java");

        OutputStream output = new ByteArrayOutputStream();
        OutputStream error = new ByteArrayOutputStream();
        runner.run(javaApp, new Output(output, error));

        assertThat(output.toString()).isEqualTo("ok");
        assertThat(error.toString()).isEqualTo("error");
    }

    private JavaApp compileJavaApp(String resource) throws IOException {
        InputStream in = getClass().getResourceAsStream(resource);
        return new JavaAppCompiler(outputDir).compile(in);
    }

    @After
    public void deleteOutputDirectory() throws Exception {
        walkFileTree(outputDir, new DeletingVisitor());
    }
}