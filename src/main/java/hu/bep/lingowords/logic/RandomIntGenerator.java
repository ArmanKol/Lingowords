package hu.bep.lingowords.logic;

import java.util.InputMismatchException;
import java.util.Random;

public class RandomIntGenerator {

    private int min, max;

    public RandomIntGenerator(int min, int max){
        if(min > max){
            throw new InputMismatchException("Min cannot be greater than max");
        }
        this.min = min;
        this.max = max;
    }


    public int getRandomNumber(){
        int randomInt;
        Random random = new Random();

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
