package hu.bep.lingowords.logic;

import java.util.InputMismatchException;
import java.util.Random;

public class RandomIntGenerator {

    private final int min;
    private final int max;
    private Random random = new Random();

    public RandomIntGenerator(final int min, final int max){
        if(min > max){
            throw new InputMismatchException("Min cannot be greater than max");
        }
        this.min = min;
        this.max = max;
    }


    public int getRandomNumber(){
        int randomInt;

        randomInt = random.nextInt(max - min + 1) + min;

        if(!(randomInt >= min && randomInt <= max)){
            randomInt = random.nextInt(max - min + 1) + min;
        }

        return randomInt;
    }

    public int getMin(){
        return min;
    }

    public int getMax(){
        return max;
    }

}
