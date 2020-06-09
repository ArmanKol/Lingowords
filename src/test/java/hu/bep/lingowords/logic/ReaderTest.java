package hu.bep.lingowords.logic;


import com.google.common.io.Files;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reader")
public class ReaderTest {

    Reader reader = new Reader();

    @Test
    @DisplayName("een bestaand file geeft een URL terug")
    void getValidFile(){
        String fileName = "basiswoorden-gekeurd.txt";
        URL url = reader.getWordsFile(fileName);

        assertTrue(url instanceof URL);
    }

    @Test
    @DisplayName("een niet bestaand file geeft een throw terug")
    void getInvalidFile(){
        String fileName = "niet_bestaand.txt";
        assertThrows(NullPointerException.class, () -> reader.getWordsFile(fileName));
    }

    @Test
    @DisplayName("Een invalid URL moet false returnen")
    void invalidURLRead(){
        String fileName = "niet_bestaand.txt";
        URL invalidURL = this.getClass().getClassLoader().getResource("lingowords/"+fileName);


        assertFalse(reader.readWordsFileTxt(invalidURL));
    }

    @Test
    @DisplayName("Een valid URL moet true returnen")
    void validURLRead(){
        String fileName = "test.txt";
        URL validURL = reader.getWordsFile(fileName);

        assertTrue(reader.readWordsFileTxt(validURL));
    }

    @Test
    @DisplayName("test file bestaat uit 7 woorden zou 2 moeten terugsturen")
    void checkWoorden(){
        String fileName = "test.txt";
        URL url = reader.getWordsFile(fileName);
        reader.readWordsFileTxt(url);

        assertSame(2, reader.getWordsList().size());
    }

    @Test
    @DisplayName("Test of alleen de toegestane file extensions in de lijst worden meegegeven")
    void getBackOnlyAllowedExtensions(){
        Set<String> files = reader.getListOfFiles();

        for(String file : files){
            String fileExtension = Files.getFileExtension(file);

            assertTrue(fileExtension.equals("txt") || fileExtension.equals("csv"));
        }

    }

//    @Test
//    @DisplayName("Het niet kunnen openen van een URL geeft done = false terug")
//    void returnFalseWhenCantRead() throws IOException {
//        Reader readermock = mock(Reader.class);
//        URLConnection url = mock(URLConnection.class);
//
//        URL file = reader.getWordsFile("basiswoorden-gekeurd.txt");
//
//        Mockito.when(url.getURL()).thenReturn(file);
//        Mockito.when(url.getInputStream()).thenThrow(new IOException());
//        Mockito.when(readermock.readWordsFile(url.getURL())).thenCallRealMethod();
//        URL newFile = url.getURL();
//
//        readermock.readWordsFile(newFile);
//
//        System.out.println(verify(readermock).readWordsFile(newFile));
//    }

}
