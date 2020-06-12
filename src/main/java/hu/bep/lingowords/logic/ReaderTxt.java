package hu.bep.lingowords.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ReaderTxt implements IReader{
    private static final Logger LOGGER = LogManager.getLogger(Reader.class);
    private final Set<String> words = new HashSet<>();

    public ReaderTxt(){

    }

    @Override
    public boolean readFile(URL input) {
        boolean done = false;

        try{
            Scanner scanFile = new Scanner(input.openStream());
            while(scanFile.hasNext()){
                words.add(scanFile.nextLine());
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

    @Override
    public Set<String> getWordsList() {
        return words;
    }

    @Override
    public boolean clearWords() {
        return false;
    }
}
