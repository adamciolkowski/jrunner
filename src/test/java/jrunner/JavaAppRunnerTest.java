package jrunner;

import jrunner.compiler.JavaAppCompiler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Path;

import static java.nio.file.Files.createTempDirectory;
import static java.nio.file.Files.walkFileTree;
import static jrunner.TestUtil.newReader;
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

    @Test
    public void preventsFromReadingSensitiveSystemProperties() throws Exception {
        JavaApp javaApp = compileJavaApp("/security/Properties.java");

        OutputStream error = new ByteArrayOutputStream();
        runner.run(javaApp, new Output(new NullOutputStream(), error));

        assertThat(error.toString()).contains("java.security.AccessControlException: access denied");
    }

    @Test
    public void maxHeapSizeIsLimitedTo8MB() throws Exception {
        JavaApp javaApp = compileJavaApp("/PrintsMaxMemory.java");

        OutputStream output = new ByteArrayOutputStream();
        runner.run(javaApp, new Output(output, new NullOutputStream()));

        String s = output.toString().trim();
        assertThat(Integer.parseInt(s)).isLessThanOrEqualTo(8);
    }

    private JavaApp compileJavaApp(String resource) throws IOException {
        Reader reader = newReader(resource);
        return new JavaAppCompiler(outputDir).compile(reader);
    }

    @After
    public void deleteOutputDirectory() throws Exception {
        walkFileTree(outputDir, new DeletingVisitor());
    }
}