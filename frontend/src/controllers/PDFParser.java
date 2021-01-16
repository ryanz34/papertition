package controllers;

import pages.Page;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PDFParser implements IPDFParser {

    private String getAllBufferLines(BufferedReader reader) throws IOException {
        return reader.lines().collect(Collectors.joining("\n"));
    }

    private Set<Page> generatePages(String stdout) {
        Set<Page> out = new HashSet<>();

        for (String line : stdout.split("\n")) {
            String[] lineArray = line.split(",");

            out.add(new Page(
                    lineArray[2].replaceAll("^\"|\"$", ""),
                    Integer.parseInt(lineArray[1].replaceAll("^\"|\"$", "")),
                    Integer.parseInt(lineArray[0].replaceAll("^\"|\"$", ""))
            ));
        }

        return out;
    }

    @Override
    public Set<Page> run(String path, Consumer<String> setLoadingText) {
        try {
            setLoadingText.accept("Preparing to import");

            ProcessBuilder pb = new ProcessBuilder("python3", "../backend/pdf_parser.py", "parse", path);

            //pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            //pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            Process process = pb.start();

            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            setLoadingText.accept("Processing");

            process.waitFor();

            String stdout = getAllBufferLines(stdoutReader);
            String stderr = getAllBufferLines(stderrReader);

            //System.out.println("stdout: " + stdout);
            //System.out.println("stderr: " + stderr);

            if (stderr.length() > 0) {
                // Pass errors from python
                throw new RuntimeException("Python Error:\n" + stderr);
            }

            return generatePages(stdout);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
