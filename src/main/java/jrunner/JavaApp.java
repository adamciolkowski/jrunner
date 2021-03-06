package jrunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaApp {

    private final Path classPath;
    private final String mainClassName;

    public JavaApp(Path classPath, String mainClassName) {
        this.classPath = classPath;
        this.mainClassName = mainClassName;
    }

    public Path getClassPath() {
        return classPath;
    }

    public String getMainClassName() {
        return mainClassName;
    }

    public void destroy() throws IOException {
        Files.walkFileTree(classPath, new DeletingVisitor());
    }
}
