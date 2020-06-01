package hu.bep.lingowords.controller;

import hu.bep.lingowords.Reader;
import hu.bep.lingowords.logic.RandomIntGenerator;
import hu.bep.lingowords.model.Word;
import hu.bep.lingowords.repository.WordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.List;

@RestController
public class WordController {
    private static Logger logger = LogManager.getLogger(WordController.class);
    private Reader reader = new Reader();

    @Autowired
    private WordRepository wordRepository;

    @GetMapping("/saveAll")
    public void saveAllWords(){
        URL url = reader.getWordsFile("basiswoorden-gekeurd.txt");
        reader.readWordsFile(url);

        for(String word : reader.getWordsList()){
            if(search(word).getId() == -1){
                wordRepository.save(new Word(word));
            }
        }
    }

    @GetMapping("/words")
    public List<Word> getWords(){
        return wordRepository.findAll();
    }

    @PostMapping("/add/{w}")
    public ResponseEntity<String> addWord(@PathVariable String word){
        if(search(word).getId() == -1){
            wordRepository.save(new Word(word));

            return new ResponseEntity<>("Word has been saved", HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("Word already exists in database", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/delete/{w}")
    public ResponseEntity<String> deleteWord(@PathVariable String word){
        if(search(word).getId() == -1){
            return new ResponseEntity<>("Word doesn't exist", HttpStatus.BAD_REQUEST);
        }else{
            wordRepository.delete(new Word(word));
            return new ResponseEntity<>("Word has been deleted", HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/words/{w}")
    public Word search(@PathVariable String w){
        try{
            if(wordRepository.findWord(w) == null){
                throw new NullPointerException();
            }
            return wordRepository.findWord(w);
        }catch(NullPointerException npe){
            return new Word(-1,"Word is not in database");
        }
    }

    @GetMapping("/words/randomword")
    public String getRandom(){
        RandomIntGenerator randomGenerator = new RandomIntGenerator(wordRepository.getMinID(), wordRepository.getMaxID());
        int wordID = randomGenerator.getRandomNumber();

        String word = wordRepository.findByID(wordID).getWord();

        return word;
    }

}
