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
    private static final Logger LOGGER = LogManager.getLogger(WordController.class);
    private final Reader reader = new Reader();

    @Autowired
    private WordRepository wordRepository;

    @GetMapping("/save/all")
    public ResponseEntity<String> saveAllWords(){
        ResponseEntity<String> response;

        reader.readAllWordsFiles();
        Set<String> wordsNotSaved = new HashSet<>();

        for(String word : reader.getWordsList()){
            if(search(word).getId() == -1){
                wordRepository.save(new Word(word));
            }else{
                wordsNotSaved.add(word);
            }
        }

        if(!wordsNotSaved.isEmpty()){
            response = new ResponseEntity(wordsNotSaved, HttpStatus.CONFLICT);
        }else{
            response = new ResponseEntity("All words saved", HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/save/{fileName}")
    public ResponseEntity<Set<String>> saveWordsFromFile(@PathVariable String fileName){
        ResponseEntity<Set<String>> response;

        URL url = reader.getWordsFile(fileName);
        Set<String> wordsNotSaved = new HashSet<>();

        for(String word : reader.getWordsList()){
            if(search(word).getId() == -1){
                wordRepository.save(new Word(word));
            }else{
                wordsNotSaved.add(word);
            }
        }

        if(!wordsNotSaved.isEmpty()){
            response = new ResponseEntity(wordsNotSaved, HttpStatus.CONFLICT);
        }else{
            response = new ResponseEntity("All words saved", HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/words")
    public List<Word> getWords(){
        return wordRepository.findAll();
    }

    @PostMapping("/words/add/{w}")
    public ResponseEntity<String> addWord(@PathVariable String word){
        ResponseEntity<String> response;

        if(search(word).getId() == -1){
            wordRepository.save(new Word(word));

            response = new ResponseEntity<>("Word has been saved", HttpStatus.OK);
        }else{
            response = new ResponseEntity<>("Word already exists in database", HttpStatus.CONFLICT);
        }

        return response;
    }

    @PostMapping("/words/delete/{w}")
    public ResponseEntity<String> deleteWord(@PathVariable String word){
        ResponseEntity<String> response;

        if(search(word).getId() == -1){
            response = new ResponseEntity<>("Word doesn't exist", HttpStatus.NOT_FOUND);
        }else{
            wordRepository.delete(new Word(word));
            response = new ResponseEntity<>("Word has been deleted", HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/words/{word}")
    public Word search(@PathVariable String word){
        Word responseWord;

        if(wordRepository.findWord(word) == null){
            responseWord = new Word(-1,"Word is not in database");
        }else{
            responseWord = wordRepository.findWord(word);
        }
        return responseWord;
    }

    @GetMapping("/words/randomword")
    public String getRandom(){
        RandomIntGenerator randomGenerator = new RandomIntGenerator(wordRepository.getMinID(), wordRepository.getMaxID());
        int wordID = randomGenerator.getRandomNumber();

        return wordRepository.findByID(wordID).getWord();
    }

}
