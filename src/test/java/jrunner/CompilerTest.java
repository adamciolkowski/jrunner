package jrunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

import static java.nio.file.Files.createTempDirectory;
import static java.nio.file.Files.walkFileTree;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CompilerTest {

    Path outputDir;

    @Before
    public void createOutputDirectory() throws Exception {
        outputDir = createTempDirectory("jrunner");
    }

    @Test
    public void compilesJavaClassFromStream() throws Exception {
        jrunner.Compiler compiler = new jrunner.Compiler(outputDir);

        InputStream in = getClass().getResourceAsStream("/Main.java");
        compiler.compile(in);

        Object result = instantiateAndCall("Main");
        assertThat(result).isEqualTo("ok");
    }

    @Test
    public void throwsExceptionIfCodeDoesNotCompile() {
        Compiler compiler = new Compiler(outputDir);

        InputStream in = getClass().getResourceAsStream("/DoesNotCompile.java");
        assertThatThrownBy(() -> compiler.compile(in))
                .isInstanceOf(CompilationFailedException.class)
                .hasMessageContaining("error: illegal combination of modifiers: abstract and final");

    }

    @After
    public void deleteOutputDirectory() throws Exception {
        walkFileTree(outputDir, new DeletingVisitor());
    }

    private Object instantiateAndCall(String className) throws Exception {
        URL[] urls = {outputDir.toUri().toURL()};
        Class<?> clazz = new URLClassLoader(urls).loadClass(className);
        Object o = clazz.newInstance();
        return clazz.getMethod("returnsOk").invoke(o);
    }
}
