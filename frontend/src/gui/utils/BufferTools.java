package gui.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class BufferTools {
    public static String getAllBufferLines(BufferedReader reader) throws IOException {
        return reader.lines().collect(Collectors.joining("\n"));
    }
}
