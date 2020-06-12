package hu.bep.lingowords.logic;

import java.util.HashSet;
import java.util.Set;

public class WordChecker {
    private Set<Integer> wordLenghts = new HashSet<>();


    private WordChecker(WordCheckerBuilder builder){
        this.wordLenghts = builder.wordLenghts;
    }

    public Set<String> checkWords(Set<String> words){
        Set<String> correctWords = new HashSet<>();

        for(String word : words){
            if(wordLenghts.contains(word.length())){
                if(word.matches("([\\w]+['.-][\\w]+)|('\\w[A-Z-a-z]+)|(\\w[0-9]\\w[a-z])|(\\w+[éëäáíïúüöó]\\w+)")){
                    continue;
                }
                if(word.matches("(\\b[a-z]\\w+)")){
                    correctWords.add(word);
                }
            }
        }
        return correctWords;
    }

    public static class WordCheckerBuilder{
        private Set<Integer> wordLenghts = new HashSet<>();

        public WordCheckerBuilder addLength(int length){
            wordLenghts.add(length);
            return this;
        }

        public WordChecker build(){
            return new WordChecker(this);
        }
    }
}
