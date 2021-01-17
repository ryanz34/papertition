package controllers;

import pages.Page;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gui.utils.BufferTools.getAllBufferLines;

public class PDFSaver {
    public void save(String path, Map<Integer, List<Page>> documents) {
        try {
            List<String> argumentArrayList = new ArrayList<>() {
                {
                    add("python3");
                    add("../backend/Merger.py");
                }
            };

            for (int documentID : documents.keySet()) {
                argumentArrayList.add("-" + documentID);

                for (Page page : documents.get(documentID)) {
                    argumentArrayList.add(page.path);
                }
            }

            //System.out.println(argumentArrayList);

            String[] arguments = argumentArrayList.toArray(new String[0]);

            ProcessBuilder pb = new ProcessBuilder(arguments);

            Process process = pb.start();

            //BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            process.waitFor();

            //String stdout = getAllBufferLines(stdoutReader);
            String stderr = getAllBufferLines(stderrReader);

            if (stderr.length() > 0) {
                // Pass errors from python
                throw new RuntimeException("Python Error:\n" + stderr);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
