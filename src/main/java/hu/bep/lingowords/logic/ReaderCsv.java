package hu.bep.lingowords.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ReaderCsv implements IReader{
    private static final Logger LOGGER = LogManager.getLogger(Reader.class);
    private final Set<String> words = new HashSet<String>();

    private String delimiter;

    public ReaderCsv(String delimiter){
        this.delimiter = delimiter;
    }

    @Override
    public boolean readFile(URL input) {
        boolean done = false;

        try{
            Scanner scanFile = new Scanner(input.openStream());
            while(scanFile.hasNext()){
                String wordLine = scanFile.nextLine();
                String[] wordArray = wordLine.split(delimiter);

                words.addAll(Arrays.asList(wordArray));
            }

            scanFile.close();

            done = true;
        }catch(IOException ioe){
            LOGGER.error("Problemen bij het openen/lezen van de file");
        }catch(NullPointerException npe){
            LOGGER.error("URL is niet geldig");
        }

        return done;
    }

    public void setDelimiter(String delimiter){
        this.delimiter = delimiter;
    }

    @Override
    public Set<String> getWordsList() {
        return words;
    }

    @Override
    public void clearList() {
        words.clear();
    }
}
