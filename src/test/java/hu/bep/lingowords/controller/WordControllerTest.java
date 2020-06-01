package hu.bep.lingowords.controller;

import hu.bep.lingowords.LingoWordsApplication;
import hu.bep.lingowords.model.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LingoWordsApplication.class)
@WebAppConfiguration
@DisplayName("Word controller")
public class WordControllerTest {
    @Autowired
    WordController controller;


    @Test
    @DisplayName("Niet bestaand woord in database return -1")
    void searchForNotExistingWord(){
        controller.search("papa");

        assertTrue(controller.search("niet bestaande woord").getId() == -1);
    }

    @Test
    @DisplayName("Bestaand woord in de database returned id != -1")
    void searchForExistingWord(){
        String existingWord = controller.getRandom();

        assertTrue(controller.search(existingWord).getId() != -1);
    }

    @Test
    @DisplayName("Kan niet bestaand woord opslaan in de database")
    void cantAddWordThatAlreadyExist(){
        String existingWord = controller.getRandom();

        assertEquals(HttpStatus.BAD_REQUEST, controller.addWord(existingWord).getStatusCode());
    }

    @Test
    @DisplayName("Delete woord dat niet bestaat")
    void deleteWordThatNotExists(){
        Word newWord = new Word("ploft");

        assertEquals(HttpStatus.BAD_REQUEST, controller.deleteWord(newWord.getWord()).getStatusCode());
    }

}
