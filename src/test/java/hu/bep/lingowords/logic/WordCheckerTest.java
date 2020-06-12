package hu.bep.lingowords.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reader")
class WordCheckerTest {
    private Reader reader = new Reader.ReaderBuilder().addExtension("csv").addExtension("txt").build();

    @Test
    @DisplayName("Woorden met de lengte 5,6 en 7 worden gereturned")
    void checkWords_LengthFiveSixSeven_ReturnCorrectWords(){
        Set<String> input = new HashSet<String>();
        WordChecker wordChecker = new WordChecker.WordCheckerBuilder().addLength(5).addLength(6).addLength(7).build();

        //Woorden met 4 letters
        input.add("amen");
        input.add("anus");

        //Woorden met 5 letters
        input.add("doden");
        input.add("astma");

        //Woorden met 6 letters
        input.add("eiland");
        input.add("export");


        //Woorden met 7 letters
        input.add("naturel");
        input.add("neusgat");


        //Woordenm met 8 letters
        input.add("paashaas");
        input.add("telkaart");

        assertSame(6, wordChecker.checkWords(input).size());
    }

    @Test
    @DisplayName("test file bestaat uit 7 woorden zou 2 moeten terugsturen")
    void checkWords_ValidWords_ReturnValidWords(){
        Set<String> input = new HashSet<String>();
        WordChecker wordChecker = new WordChecker.WordCheckerBuilder().addLength(5).addLength(6).addLength(7).build();

        input.add("papier,");
        input.add("päpéér!");
        input.add("hallo");
        input.add("cover");
        input.add("Houten");
        input.add("bieër");
        input.add("bakker?");

        assertSame(2, wordChecker.checkWords(input).size());
    }

}
