package hu.bep.lingowords.logic;


import com.google.common.io.Files;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reader")
class ReaderTest {
    private Reader reader = new Reader.ReaderBuilder().addExtension("csv").addExtension("txt").build();

    @Test
    @DisplayName("een bestaand file geeft een URL terug")
    void getFile_FileExists_ValidUrl(){
        String fileName = "test.txt";
        URL url = reader.getFile(fileName);

        assertTrue(url != null);
    }

    @Test
    @DisplayName("een niet bestaand file geeft een throw terug")
    void getFile_FileNotExists_ExceptionThrown(){
        String fileName = "niet_bestaand.txt";
        assertThrows(NullPointerException.class, () -> reader.getFile(fileName));
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
        URL validURL = reader.getFile(fileName);

        assertTrue(readerTxt.readFile(validURL));
    }

    @Test
    @DisplayName("Een valid csv URL moet true returnen")
    void readFileCsv_UrlValid_ReturnTrue(){
        String fileName = "woorden2.csv";
        ReaderCsv readerCsv = new ReaderCsv(",");
        URL validURL = reader.getFile(fileName);

        assertTrue(readerCsv.readFile(validURL));
    }

    @Test
    @DisplayName("Test of alleen de toegestane file extensions in de lijst worden meegegeven")
    void readAllFilesInResource_OnlyCsvTxt_ReturnsTxtCsvFiles(){
        Set<String> files = reader.readAllFilesInResource();

        for(String file : files){
            String fileExtension = Files.getFileExtension(file);

            assertTrue(fileExtension.equals("txt") || fileExtension.equals("csv"));
        }

    }

}
