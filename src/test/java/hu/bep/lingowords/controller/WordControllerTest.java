package hu.bep.lingowords.controller;

import hu.bep.lingowords.model.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Word controller")
public class WordControllerTest {
    @Autowired
    WordController controller;


    @Test
    @DisplayName("Niet bestaand woord in database return -1")
    void searchForNotExistingWord(){
        Word woord = controller.search("niet bestaand woord");
        int idWoord = (int)woord.getId();

        assertSame(-1, idWoord);
    }

    @Test
    @DisplayName("Bestaand woord in de database returned geen woord met id -1")
    void searchForExistingWord(){
        String existingWord = controller.getRandom();

        assertNotSame(-1, controller.search(existingWord).getId());
    }

    @Test
    @DisplayName("Een bestaand woord kan niet weer opgeslagen worden in de database en returned CONFLICT")
    void cantAddWordThatAlreadyExist(){
        String existingWord = controller.getRandom();

        assertEquals(HttpStatus.CONFLICT, controller.addWord(existingWord).getStatusCode());
    }

    @Test
    @DisplayName("Delete woord dat niet bestaat")
    void deleteWordThatNotExists(){
        Word newWord = new Word("ploft");

        assertEquals(HttpStatus.NOT_FOUND, controller.deleteWord(newWord.getWord()).getStatusCode());
    }

    @Test
    @DisplayName("Delete woord dat bestaat returned OK")
    void deleteWordThatExists(){
        String randomWord = controller.getRandom();
        HttpStatus status = controller.deleteWord(randomWord).getStatusCode();

        assertEquals(HttpStatus.OK, status);
        controller.addWord(randomWord);
    }

}
