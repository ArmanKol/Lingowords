package hu.bep.lingowords.model;

import javax.persistence.*;

@Entity
@Table(name = "word")
public class Word {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name = "word")
    private String word;

    public Word(){}

    public Word(long id,String word){
        this.id = id;
        this.word = word;
    }

    public Word(String word){
        this.word = word;
    }

    public String getWord(){
        return word;
    }

    public long getId(){
        return id;
    }

    @Override
    public String toString(){
        return "{id:"+id+", word:"+word+"}";
    }
}
