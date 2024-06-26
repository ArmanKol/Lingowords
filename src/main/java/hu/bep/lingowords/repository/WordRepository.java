package hu.bep.lingowords.repository;


import hu.bep.lingowords.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findAll();

    @Query(value = "select wd.id, wd.word from word wd where wd.word = :word", nativeQuery = true)
    Word findWord(@Param("word") String word);

    @Query(value = "select MIN(id) from word;", nativeQuery = true)
    int getMinID();

    @Query(value = "select MAX(id) from word;", nativeQuery = true)
    int getMaxID();

    @Query(value = "select wd.id, wd.word from word wd where wd.id = :id", nativeQuery = true)
    Word findByID(@Param("id") int id);
}
