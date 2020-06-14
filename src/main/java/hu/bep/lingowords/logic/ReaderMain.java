package hu.bep.lingowords.logic;

import com.google.common.io.Files;
import hu.bep.lingowords.logic.reader.IReader;
import hu.bep.lingowords.logic.reader.ReaderCsv;
import hu.bep.lingowords.logic.reader.ReaderTxt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.*;

public class ReaderMain {
    private static final Logger LOGGER = LogManager.getLogger(ReaderMain.class);
    private Set<String> fileExtensions;

    private IReader readerCsv = new ReaderCsv(",");
    private IReader readerTxt = new ReaderTxt();

    private WordChecker wordChecker = new WordChecker.WordCheckerBuilder().addLength(5).addLength(6).addLength(7).build();

    private ReaderMain(ReaderBuilder readerBuilder){
        fileExtensions = readerBuilder.fileExtensions;
    }

    public Set<String> readAllFilesInResource(){
        Set<String> allFiles = new HashSet<>();

        try{
            URL path = this.getClass().getClassLoader().getResource("lingowords");
            String[] files = new File(path.toURI()).list();

            for(String file : files){
                String extension = Files.getFileExtension(file);
                if(fileExtensions.contains(extension)){
                    allFiles.add(file);
                }
            }
        }catch(URISyntaxException us){
            LOGGER.error("Could not be parsed");
        }

        return allFiles;
    }

    public URL getFile(final String file){
        URL url = this.getClass().getClassLoader().getResource("lingowords/"+file);

        if(url == null){
            LOGGER.error("The given filename doesnt exist! Check if it's in the correct directory or spelt correctly");
            throw new NullPointerException("The given filename doesnt exist");
        }

        return url;
    }

    public Set<String> readAllWordsFiles(){
        Set<String> validWords = new HashSet<>();
        Set<String> listOfFiles = readAllFilesInResource();

        for(String file: listOfFiles){
            URL url = this.getClass().getClassLoader().getResource("lingowords/"+file);
            String extension = Files.getFileExtension(file);

            String csvExtension = "csv";
            String txtExtension = "txt";

            if(csvExtension.equals(extension)){
                readerCsv.readFile(url);
                validWords.addAll(wordChecker.checkWords(readerCsv.getWordsList()));
                readerCsv.clearList();
            }else if(txtExtension.equals(extension)){
                readerTxt.readFile(url);
                validWords.addAll(wordChecker.checkWords(readerTxt.getWordsList()));
                readerTxt.clearList();
            }
        }

        return validWords;
    }

    public Set<String> readCorrectReader(URL url){
        Set<String> validWords = new HashSet<>();
        String extension = Files.getFileExtension(url.getFile());

        String csvExtension = "csv";
        String txtExtension = "txt";

        if(csvExtension.equals(extension)){
            readerCsv.readFile(url);
            validWords = wordChecker.checkWords(readerCsv.getWordsList());
            readerCsv.clearList();
        }else if(txtExtension.equals(extension)){
            readerTxt.readFile(url);
            validWords =  wordChecker.checkWords(readerTxt.getWordsList());
            readerTxt.clearList();
        }

        return validWords;
    }

    public static class ReaderBuilder{
        private final Set<String> fileExtensions = new HashSet<>();

        public ReaderMain.ReaderBuilder addExtension(String extension){
            fileExtensions.add(extension);
            return this;
        }

        public ReaderMain build(){
            return new ReaderMain(this);
        }
    }
}
