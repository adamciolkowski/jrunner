package jrunner.web;

import jrunner.IoUtils;
import jrunner.JRunnerService;
import jrunner.Output;
import jrunner.compiler.CompilationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import static java.nio.charset.StandardCharsets.UTF_8;

@Controller
public class JRunnerController {

    private final JRunnerService jRunnerService;

    @Autowired
    public JRunnerController(JRunnerService jRunnerService) {
        this.jRunnerService = jRunnerService;
    }

    @GetMapping("/")
    public String get(Model model) {
        model.addAttribute("sourceCode", initialSourceCode());
        return "index";
    }

    private String initialSourceCode() {
        try {
            return tryReadInitialSourceCode();
        } catch (IOException e) {
            return  "";
        }
    }

    private String tryReadInitialSourceCode() throws IOException {
        InputStream in = getClass().getResourceAsStream("/HelloWorld.java");
        StringWriter out = new StringWriter();
        IoUtils.copy(new InputStreamReader(in, UTF_8), out);
        return out.toString();
    }

    @PostMapping("/")
    public void post(String sourceCode, OutputStream out) throws IOException, InterruptedException {
        try {
            jRunnerService.compileAndRun(sourceCode, new Output(out, out));
        } catch (CompilationFailedException e) {
            Writer writer = new OutputStreamWriter(out, UTF_8);
            writer.write(e.getMessage());
            writer.flush();
        }
    }
}
