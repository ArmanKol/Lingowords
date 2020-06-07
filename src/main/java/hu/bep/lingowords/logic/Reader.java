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
    private static Logger logger = LogManager.getLogger(Reader.class);
    private List<String> wordsList = new ArrayList<>();

    public Set<String> getListOfFiles(){
        try{
            Set<String> returnfiles = new HashSet<String>();
            URL path = this.getClass().getClassLoader().getResource("lingowords");
            String[] files = new File(path.toURI()).list();

            for(String file : files){
                String extension = Files.getFileExtension(file);
                if(extension.equals("txt") || extension.equals("csv")){
                    returnfiles.add(file);
                }
            }

            return returnfiles;
        }catch(URISyntaxException us){
            logger.info(us.getInput()+" could not be parsed");
            return new HashSet<String>();
        }
    }

    public URL getWordsFile(String file){
        URL url = this.getClass().getClassLoader().getResource("lingowords/"+file);

        if(url == null){
            throw new NullPointerException("Het opgegeven bestand bestaat niet.");
        }

        return url;
    }

    public boolean readAllWordsFiles(){
        Set<String> listOfFiles = getListOfFiles();
        boolean returnValue = false;

        for(String file: listOfFiles){
            URL url = this.getClass().getClassLoader().getResource("lingowords/"+file);
            String extension = Files.getFileExtension(file);

            if(extension.equals("csv")){
                readWordsFileCsv(url, ",");
            }else if(extension.equals("txt")){
                readWordsFileTxt(url);
            }

            returnValue = readWordsFileTxt(url);
        }

        return returnValue;
    }

    //TODO: Regex verbeteren
    public boolean readWordsFileTxt(URL input){
        boolean done;

        try{
            Scanner scanFile = new Scanner(input.openStream());
            while(scanFile.hasNext()){
                String word = scanFile.nextLine();

                if(word.length() == 5 || word.length() == 6 || word.length() == 7){
                    if(word.matches("([\\w]+['.-][\\w]+)|('\\w[A-Z-a-z]+)|(\\w[0-9]\\w[a-z])|(\\w+[éëäáíïúüöó]\\w+)")){
                        continue;
                    }
                    if(word.matches("(\\b[a-z]\\w+)")){
                        wordsList.add(word);
                    }
                }
            }

            scanFile.close();

            done = true;
        }catch(IOException ioe){
            logger.error("Problemen bij het openen/lezen van de file");
            done = false;
        }catch(NullPointerException npe){
            logger.error("URL is niet geldig");
            done = false;
        }

        //clearList(done);
        return done;
    }

    public boolean readWordsFileCsv(URL input, String delimiter){
        boolean done;

        try{
            Scanner scanFile = new Scanner(input.openStream());
            while(scanFile.hasNext()){
                String wordLine = scanFile.nextLine();
                String[] wordArray = wordLine.split(delimiter);

                for(String word : wordArray){
                    if(word.length() == 5 || word.length() == 6 || word.length() == 7){
                        if(word.matches("([\\w]+['.-][\\w]+)|('\\w[A-Z-a-z]+)|(\\w[0-9]\\w[a-z])|(\\w+[éëäáíïúüöó]\\w+)")){
                            continue;
                        }
                        if(word.matches("(\\b[a-z]\\w+)")){
                            wordsList.add(word);
                        }
                    }
                }
            }

            scanFile.close();

            done = true;
        }catch(IOException ioe){
            logger.error("Problemen bij het openen/lezen van de file");
            done = false;
        }catch(NullPointerException npe){
            logger.error("URL is niet geldig");
            done = false;
        }

        return done;
    }

    private void clearList(boolean done){
        if(done){
            wordsList.clear();
        }
    }

    public List<String> getWordsList(){
        return wordsList;
    }

}
