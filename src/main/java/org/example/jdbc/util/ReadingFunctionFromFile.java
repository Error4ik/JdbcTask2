package org.example.jdbc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadingFunctionFromFile {

    private final String functionFilePath = "src/main/data/function.sql";

    public List<String> readFunctionFromFile() {
        List<String> strings = new ArrayList<>();
        try {
            strings = Files.readAllLines(Paths.get(functionFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        List<String> functionsFromFileList = new ArrayList<>();
        for (String string : strings) {
            if (string.equalsIgnoreCase("")) {
                functionsFromFileList.add(sb.toString().replaceAll("\\s{2,}", " ").trim());
                sb = new StringBuilder();
            } else {
                sb.append(string);
            }
        }
        return functionsFromFileList;
    }
}
