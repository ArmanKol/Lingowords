package hu.bep.lingowords;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {
    private static Logger logger = LogManager.getLogger(Reader.class);
    private List<String> wordsList = new ArrayList<>();

    public String[] getListOfFiles(){
        try{
            URL path = this.getClass().getClassLoader().getResource("lingowords");

            return new File(path.toURI()).list();
        }catch(URISyntaxException us){
            logger.info(us.getInput()+" could not be parsed");
            return new String[]{};
        }
    }

    public URL getWordsFile(){
        URL url = this.getClass().getClassLoader().getResource("lingowords/basiswoorden-gekeurd.txt");

        return url;
    }

    //TODO: Regex verbeteren
    public boolean readWordsFile(URL input){
        boolean done;

        try{
            Scanner scanFile = new Scanner(input.openStream());
            while(scanFile.hasNext()){
                String word = scanFile.nextLine();

                if(word.length() == 5 || word.length() == 6 || word.length() == 7){
                    if(word.matches("([\\w]+['.-][\\w]+)|('\\w[A-Z-a-z]+)|(\\w[0-9]\\w[a-z])|(\\w+[éëäáíïúüöó]\\w+)")){
                        continue;
                    }
                    if(word.matches("(\\w[a-z]\\w+)")){
                        wordsList.add(word);
                    }
                }
            }

            scanFile.close();

            done = true;
        }catch(IOException ioe){
            logger.error("Not able to open/read file");
            done = false;
        }

        clearList(done);
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
