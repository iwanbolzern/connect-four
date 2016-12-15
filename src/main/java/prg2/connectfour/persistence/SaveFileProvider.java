package prg2.connectfour.persistence;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveFileProvider implements ISaveFileProvider {
    private static String FILENAME = ".connect-four-save";
    private String home = System.getProperty("user.home");

    @Override
    public File get() {
        Path file = Paths.get(home, FILENAME);
        return file.toFile();
    }
}
