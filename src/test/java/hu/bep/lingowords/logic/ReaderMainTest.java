package hu.bep.lingowords.logic;


import com.google.common.io.Files;
import hu.bep.lingowords.logic.reader.ReaderCsv;
import hu.bep.lingowords.logic.reader.ReaderTxt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reader")
class ReaderMainTest {
    private ReaderMain readerMain = new ReaderMain.ReaderBuilder().addExtension("csv").addExtension("txt").build();

    @Test
    @DisplayName("een bestaand file geeft een URL terug")
    void getFile_FileExists_ValidUrl(){
        String fileName = "test.txt";
        URL url = readerMain.getFile(fileName);

        assertNotNull(url);
    }

    @Test
    @DisplayName("een niet bestaand file geeft een throw terug")
    void getFile_FileNotExists_ExceptionThrown(){
        String fileName = "niet_bestaand.txt";
        assertThrows(NullPointerException.class, () -> readerMain.getFile(fileName));
    }

    @Test
    @DisplayName("Een invalid txt URL moet false returnen")
    void readFileTxt_UrlInvalid_ReturnFalse(){
        String fileName = "niet_bestaand.txt";
        ReaderTxt readerTxt = new ReaderTxt();
        URL invalidURL = this.getClass().getClassLoader().getResource("lingowords/"+fileName);


        assertFalse(readerTxt.readFile(invalidURL));
    }

    @Test
    @DisplayName("Een invalid csv URL moet false returnen")
    void readFileCsv_UrlInvalid_ReturnFalse(){
        String fileName = "niet_bestaand.csv";
        ReaderCsv readerCsv = new ReaderCsv(",");
        URL invalidURL = this.getClass().getClassLoader().getResource("lingowords/"+fileName);

        assertFalse(readerCsv.readFile(invalidURL));
    }

    @Test
    @DisplayName("Een valid txt URL moet true returnen")
    void readFileTxt_UrlValid_ReturnTrue(){
        String fileName = "test.txt";
        ReaderTxt readerTxt = new ReaderTxt();
        URL validURL = readerMain.getFile(fileName);

        assertTrue(readerTxt.readFile(validURL));
    }

    @Test
    @DisplayName("Een valid csv URL moet true returnen")
    void readFileCsv_UrlValid_ReturnTrue(){
        String fileName = "woorden2.csv";
        ReaderCsv readerCsv = new ReaderCsv(",");
        URL validURL = readerMain.getFile(fileName);

        assertTrue(readerCsv.readFile(validURL));
    }

    @Test
    @DisplayName("Test of alleen de toegestane file extensions in de lijst worden meegegeven")
    void readAllFilesInResource_OnlyCsvTxt_ReturnsTxtCsvFiles(){
        Set<String> files = readerMain.readAllFilesInResource();

        String csvExtension = "csv";
        String txtExtension = "txt";

        for(String file : files){
            String fileExtension = Files.getFileExtension(file);

            assertTrue(txtExtension.equals(fileExtension) || csvExtension.equals(fileExtension));
        }

    }

}
