package hu.bep.lingowords.controller;

import hu.bep.lingowords.logic.Reader;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class WordController {
    private static Logger logger = LogManager.getLogger(WordController.class);
    private Reader reader = new Reader();

    @Autowired
    private WordRepository wordRepository;

    @GetMapping("/save/all")
    public ResponseEntity<String> saveAllWords(){
        reader.readAllWordsFiles();
        Set<String> wordsNotSaved = new HashSet<>();

        for(String word : reader.getWordsList()){
            if(search(word).getId() == -1){
                wordRepository.save(new Word(word));
            }else{
                wordsNotSaved.add(word);
            }
        }

        if(wordsNotSaved.size() > 0){
            return new ResponseEntity(wordsNotSaved, HttpStatus.CONFLICT);
        }

        return new ResponseEntity("All words saved", HttpStatus.OK);
    }

    @GetMapping("/save/{fileName}")
    public ResponseEntity<Set<String>> saveWordsFromFile(@PathVariable String fileName){
        URL url = reader.getWordsFile(fileName);
        Set<String> wordsNotSaved = new HashSet<>();

        for(String word : reader.getWordsList()){
            if(search(word).getId() == -1){
                wordRepository.save(new Word(word));
            }else{
                wordsNotSaved.add(word);
            }
        }

        if(wordsNotSaved.size() > 0){
            return new ResponseEntity(wordsNotSaved, HttpStatus.CONFLICT);
        }

        return new ResponseEntity("All words saved", HttpStatus.OK);
    }

    @GetMapping("/words")
    public List<Word> getWords(){
        return wordRepository.findAll();
    }

    @PostMapping("/words/add/{w}")
    public ResponseEntity<String> addWord(@PathVariable String word){
        if(search(word).getId() == -1){
            wordRepository.save(new Word(word));

            return new ResponseEntity<>("Word has been saved", HttpStatus.OK);
        }

        return new ResponseEntity<>("Word already exists in database", HttpStatus.CONFLICT);
    }

    @PostMapping("/words/delete/{w}")
    public ResponseEntity<String> deleteWord(@PathVariable String word){
        if(search(word).getId() == -1){
            return new ResponseEntity<>("Word doesn't exist", HttpStatus.NOT_FOUND);
        }else{
            wordRepository.delete(new Word(word));
            return new ResponseEntity<>("Word has been deleted", HttpStatus.OK);
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
