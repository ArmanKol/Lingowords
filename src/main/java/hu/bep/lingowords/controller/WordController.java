package hu.bep.lingowords.controller;

import hu.bep.lingowords.Reader;
import hu.bep.lingowords.model.Word;
import hu.bep.lingowords.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.List;

@RestController
public class WordController {
    //private static Logger logger = LogManager.getLogger(WordController.class);
    private Reader reader = new Reader();

    @Autowired
    private WordRepository wordRepository;

    @GetMapping("/saveAll")
    public void saveAllWords(){
        URL url = reader.getWordsFile();
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

    @PostMapping(path = "/add/{w}", produces = "application/json")
    public void addWord(@RequestBody Word word){
        wordRepository.save(word);
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
}
