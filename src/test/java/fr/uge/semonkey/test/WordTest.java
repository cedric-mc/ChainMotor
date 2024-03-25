package fr.uge.semonkey.test;

import fr.uge.semonkey.model.Word;
import org.junit.Test;
import static org.junit.Assert.*;

public class WordTest {
    @Test
    public void testWordCreation() {
        Word word = new Word("chien");
        assertNotNull(word);
        assertEquals("chien", word.word());
    }
}
