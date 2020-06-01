package hu.bep.lingowords.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.InputMismatchException;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Random generator")
public class RandomIntGeneratorTest {

    @Test
    @DisplayName("als min groter is dan max dan throw exception")
    void minGreaterThanMax(){
        assertThrows(InputMismatchException.class, () -> {
            RandomIntGenerator generator = new RandomIntGenerator(10, 5);
        });
    }


    @Test
    @DisplayName("min = 5, max = 5 random number altijd 5")
    void numberAlwaysSame(){
        RandomIntGenerator generator = new RandomIntGenerator(5,5);

        for(int x=0; x < 1000; x++){
            assertTrue(generator.getRandomNumber() == 5);
        }
    }

    @Test
    @DisplayName("random nummer mag nooit buiten de min en max vallen")
    void randomNumberInBound(){
        RandomIntGenerator generator = new RandomIntGenerator(10, 50);

        for(int x=0; x < 1000; x++){
            int randomNumber = generator.getRandomNumber();
            assertTrue(randomNumber >= generator.getMin() && randomNumber <= generator.getMax());
        }
    }
}
