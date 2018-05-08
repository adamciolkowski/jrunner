package jrunner.web;

import jrunner.JRunnerService;
import jrunner.Output;
import jrunner.compiler.CompilationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Controller
public class JRunnerController {

    private final JRunnerService jRunnerService;

    @Autowired
    public JRunnerController(JRunnerService jRunnerService) {
        this.jRunnerService = jRunnerService;
    }

    @GetMapping("/")
    public String get() {
        return "index";
    }

    @PostMapping("/")
    public void post(String sourceCode, OutputStream out) throws IOException, InterruptedException {
        try {
            jRunnerService.compileAndRun(sourceCode, new Output(out, out));
        } catch (CompilationFailedException e) {
            Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            writer.write(e.getMessage());
            writer.flush();
        }
    }
}
