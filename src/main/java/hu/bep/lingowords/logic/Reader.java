package hu.bep.lingowords.logic;

import com.google.common.io.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.*;

public class Reader {
    private static final Logger LOGGER = LogManager.getLogger(Reader.class);
    private Set<String> wordsList = new HashSet<>();

    private ReaderCsv readerCsv = new ReaderCsv(",");
    private ReaderTxt readerTxt = new ReaderTxt();

    public Reader(){

    }


    //TODO: extensions niet hardcoden vervangen door builder.
    public Set<String> readAllFilesInResource(){
        Set<String> returnFiles = new HashSet<String>();

        try{
            URL path = this.getClass().getClassLoader().getResource("lingowords");
            String[] files = new File(path.toURI()).list();

            for(String file : files){
                String extension = Files.getFileExtension(file);
                if(extension.equals("txt") || extension.equals("csv")){
                    returnFiles.add(file);
                }
            }
        }catch(URISyntaxException us){
            LOGGER.error(us.getInput()+" could not be parsed");
        }

        return returnFiles;
    }

    public URL getFile(final String file){
        URL url = this.getClass().getClassLoader().getResource("lingowords/"+file);

        if(url == null){
            LOGGER.error("The given filename doesnt exist! Check if it's in the correct directory or spelt correctly");
            throw new NullPointerException("The given filename doesnt exist");
        }

        return url;
    }

    public boolean readAllWordsFiles(){
        Set<String> listOfFiles = readAllFilesInResource();
        boolean returnValue = false;

        for(String file: listOfFiles){
            URL url = this.getClass().getClassLoader().getResource("lingowords/"+file);
            String extension = Files.getFileExtension(file);

            if(extension.equals("csv")){
                readerCsv.readFile(url);
                wordsList = readerCsv.getWordsList();
                readerCsv.clearWords();
            }else if(extension.equals("txt")){
                readerTxt.readFile(url);
                wordsList = readerTxt.getWordsList();
                readerTxt.clearWords();
            }

            returnValue = true;
        }

        return returnValue;
    }

    public void readCorrectReader(URL url){
        String extension = Files.getFileExtension(url.getFile());

        if(extension.equals("csv")){
            readerCsv.readFile(url);
            wordsList = readerCsv.getWordsList();
            readerCsv.clearWords();
        }else if(extension.equals("txt")){
            readerTxt.readFile(url);
            wordsList = readerTxt.getWordsList();
            readerTxt.clearWords();
        }

    }

    private void clearList(final boolean done){
        if(done){
            wordsList.clear();
        }
    }

    public Set<String> getWordsList(){
        return wordsList;
    }

}
