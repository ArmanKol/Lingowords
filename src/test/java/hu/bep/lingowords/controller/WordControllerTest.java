package hu.bep.lingowords.controller;

import hu.bep.lingowords.model.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Word controller")
class WordControllerTest {
    @Autowired
    WordController controller;

    @Test
    @DisplayName("getWords returns lijst met woorden")
    void getWords_withWords_ListWordsObjects(){
        int size = controller.getWords().size();

        for(int i=0; i < size; i++){
            assertTrue(controller.getWords().get(i) instanceof Word);
        }
    }

    @Test
    @DisplayName("Niet bestaand woord in database return -1")
    void searchWord_DoesntExist_IdMinusOne(){
        Word woord = controller.search("niet bestaand woord");
        int idWoord = (int)woord.getId();

        assertSame(-1, idWoord);
    }

    @Test
    @DisplayName("Bestaand woord in de database returned geen woord met id -1")
    void searchWord_Exists_IdNotMinusOne(){
        String existingWord = controller.getRandom();

        assertNotSame(-1, controller.search(existingWord).getId());
    }

    @Test
    @DisplayName("Een nieuw woord opslaan geeft OK status terug")
    void addWord_NotExists_HttpStatusOk(){
        String inputWord = "eters";

        assertEquals(HttpStatus.OK, controller.addWord(inputWord).getStatusCode());
    }

    @Test
    @DisplayName("Delete woord dat niet bestaat")
    void deleteWord_NotExists_HttpStatusNotFound(){
        Word newWord = new Word("ploft");

        assertEquals(HttpStatus.NOT_FOUND, controller.deleteWord(newWord.getWord()).getStatusCode());
    }

    @Test
    @DisplayName("Delete woord dat bestaat returned OK")
    void deleteWord_Exists_HttpStatusOk(){
        String randomWord = controller.getRandom();
        HttpStatus status = controller.deleteWord(randomWord).getStatusCode();

        assertEquals(HttpStatus.OK, status);
        controller.addWord(randomWord);
    }

    @Test
    @DisplayName("Lees en save de woorden van een niet bestaand file moeten een error terug geven")
    void readFile_NotExists_HttpStatusNotFound(){
        String nonExistingFile = "nothing.txt";

        ResponseEntity response = controller.saveWordsFromFile(nonExistingFile);

        assertSame(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Leest een bestaande file uit die nog geen woorden bevat die in de database bevind")
    void readFile_ExistsWithoutDuplicates_HttpStatusOk(){
        String existingFile = "woorden.txt";

        ResponseEntity response = controller.saveWordsFromFile(existingFile);

        assertSame(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Leest een file uit die al bestaande woorden in de database heeft")
    void readFile_ExistsWithDuplicates_HttpStatusConflict(){
        String existingFile = "woorden2.csv";

        ResponseEntity response = controller.saveWordsFromFile(existingFile);
        assertSame(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("Alle files uitlezen geeft een Conflict. Dezelfde woorden als in database")
    void readFiles_WithoutDuplicates_HttpStatusOk(){
        ResponseEntity response = controller.saveAllWords();
        System.out.println(response);
        assertSame(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("Een bestaand woord kan niet weer opgeslagen worden in de database en returned CONFLICT")
    void addWord_WordExists_HttpStatusConflict(){
        String existingWord = controller.getRandom();

        assertEquals(HttpStatus.CONFLICT, controller.addWord(existingWord).getStatusCode());
    }

}
