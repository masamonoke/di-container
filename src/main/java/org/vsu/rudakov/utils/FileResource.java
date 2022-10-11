package org.vsu.rudakov.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileResource {
    //передавать значения вида "src/main/resources/..."
    public static BufferedReader readFile(String localPath) throws IOException {
        FileReader fileReader;
        try {
            fileReader = new FileReader(localPath);
        } catch (FileNotFoundException e) {
            var file = new File(localPath);
            if (file.createNewFile()) {
                fileReader = new FileReader(localPath);
            } else {
                return null;
            }
        }
        return new BufferedReader(fileReader);
    }

    public static void writeFile(String localPath, List<String> data) {
        try (var fileWriter = new FileWriter(localPath)) {
            if (data == null) {
                fileWriter.write("");
                return;
            }
            for (var line : data) {
                fileWriter.write(line);
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getLines(String localPath) {
        var fileLines = new ArrayList<String>();
        try (var bf = FileResource.readFile(localPath)) {
            if (bf == null) {
                throw new RuntimeException();
            }
            String line;
            while ((line = bf.readLine()) != null) {
                fileLines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileLines;
    }

}
