package hu.bep.lingowords.logic;

import java.net.URL;
import java.util.Set;

public interface IReader {
    public boolean readFile(final URL input);
    public Set<String> getWordsList();
    public void clearList();

}
