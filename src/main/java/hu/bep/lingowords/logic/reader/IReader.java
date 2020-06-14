package hu.bep.lingowords.logic.reader;

import java.net.URL;
import java.util.Set;

public interface IReader {
    boolean readFile(final URL input);
    Set<String> getWordsList();
    void clearList();

}
