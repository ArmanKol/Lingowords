package hu.bep.lingowords.controller;

import com.google.gson.JsonObject;
import hu.bep.lingowords.logic.ReaderMain;
import hu.bep.lingowords.logic.RandomIntGenerator;
import hu.bep.lingowords.model.Word;
import hu.bep.lingowords.repository.WordRepository;
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
    private final ReaderMain readerMain = new ReaderMain.ReaderBuilder().addExtension("txt").addExtension("csv").build();

    @Autowired
    private WordRepository wordRepository;

    @GetMapping("/api/save/all")
    public ResponseEntity<String> saveAllWords(){
        ResponseEntity<String> response;

        Set<String> words = readerMain.readAllWordsFiles();
        Set<String> wordsNotSaved = new HashSet<>();

        for(String word : words){
            if(search(word).getId() == -1){
                wordRepository.save(new Word(word));
            }else{
                wordsNotSaved.add(word);
            }
        }

        if(!wordsNotSaved.isEmpty()){
            response = new ResponseEntity<>(wordsNotSaved.toString(), HttpStatus.CONFLICT);
        }else{
            response = new ResponseEntity<>("All words saved", HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/api/save/{fileName}")
    public ResponseEntity<String> saveWordsFromFile(@PathVariable String fileName){
        ResponseEntity<String> response;
        try{
            URL url = readerMain.getFile(fileName);
            Set<String> wordsNotSaved = new HashSet<>();

            for(String word : readerMain.readCorrectReader(url)){
                if(search(word).getId() == -1){
                    wordRepository.save(new Word(word));
                }else{
                    wordsNotSaved.add(word);
                }
            }

            if(!wordsNotSaved.isEmpty()){
                response = new ResponseEntity<>(wordsNotSaved.toString(), HttpStatus.CONFLICT);
            }else{
                response = new ResponseEntity<>("All words saved", HttpStatus.OK);
            }
        }catch(NullPointerException npe){
            response = new ResponseEntity<>("File is not found", HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/api/words")
    public List<Word> getWords(){
        return wordRepository.findAll();
    }

    @PostMapping("/api/words/add")
    public ResponseEntity<String> addWord(@RequestBody String word){
        ResponseEntity<String> response;
        JsonObject body = new JsonObject();

        if(search(word).getId() == -1 && readerMain.getWordChecker().checkSingleWord(word)){
            body.addProperty("message", "Word has been saved");
            wordRepository.save(new Word(word));
            response = new ResponseEntity<>(body.toString(), HttpStatus.OK);
        }else{
            body.addProperty("message", "Word already exists in database");
            response = new ResponseEntity<>(body.toString(), HttpStatus.CONFLICT);
        }

        return response;
    }

    @DeleteMapping("/api/words/delete")
    public ResponseEntity<String> deleteWord(@RequestBody String word){
        ResponseEntity<String> response;
        JsonObject body = new JsonObject();

        Word foundWord = search(word);

        if(foundWord.getId() == -1){
            body.addProperty("message", "Word doesn't exist");
            response = new ResponseEntity<>(body.toString(), HttpStatus.NOT_FOUND);
        }else{
            body.addProperty("message", "Word has been deleted");
            wordRepository.delete(foundWord);
            response = new ResponseEntity<>(body.toString(), HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/api/words/{word}")
    public Word search(@PathVariable String word){
        Word responseWord;

        if(wordRepository.findWord(word) == null){
            responseWord = new Word(-1,"Word is not in database");
        }else{
            responseWord = wordRepository.findWord(word);
        }
        return responseWord;
    }

    @GetMapping("/api/words/randomword")
    public String getRandom(){
        RandomIntGenerator randomGenerator = new RandomIntGenerator(wordRepository.getMinID(), wordRepository.getMaxID());
        int wordID = randomGenerator.getRandomNumber();

        return wordRepository.findByID(wordID).getWord();
    }

}
