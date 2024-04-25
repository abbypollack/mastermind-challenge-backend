package com.mastermind.data;

import com.mastermind.models.Guess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GuessJdbcRepositoryTest {

    @Autowired
    private GuessJdbcRepository repository;

    @Autowired
    private KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAllGuesses() {
        List<Guess> guesses = repository.findAll();
        assertNotNull(guesses);
        assertTrue(guesses.size() >= 3);
    }

    @Test
    void shouldFindById() {
        Guess guess = repository.findById(1);
        assertNotNull(guess);
        assertEquals("5678", guess.getGuessSequence());
    }

    @Test
    void shouldFindByGameId() {
        List<Guess> guesses = repository.findByGameId(2);
        assertNotNull(guesses);
        assertFalse(guesses.isEmpty());
        assertEquals("5678", guesses.get(0).getGuessSequence());
    }

    @Test
    void shouldCreateGuess() {
        Guess newGuess = new Guess(0, 2, 3, "8765", "1 correct number, 1 correct location", LocalDateTime.now());
        Guess createdGuess = repository.create(newGuess);
        assertNotNull(createdGuess);
        assertTrue(createdGuess.getGuessId() == 0);
    }

    @Test
    void shouldUpdateGuess() {
        Guess guess = repository.findById(2);
        String newFeedback = "Updated feedback";
        guess.setFeedback(newFeedback);
        assertTrue(repository.update(guess));

        Guess updatedGuess = repository.findById(2);
        assertEquals(newFeedback, updatedGuess.getFeedback());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(2));
        assertThrows(Exception.class, () -> repository.findById(2));
    }

    @Test
    void shouldNotUpdateNonExistingGuess() {
        Guess nonExistingGuess = new Guess(999, 1, 1, "0000", "No feedback", LocalDateTime.now());
        assertFalse(repository.update(nonExistingGuess));
    }

    @Test
    void shouldNotDeleteNonExistingGuess() {
        assertFalse(repository.deleteById(999));
    }

    @Test
    void shouldNotFindGuessesForNonexistentGameId() {
        List<Guess> guesses = repository.findByGameId(999);
        assertTrue(guesses.isEmpty());
    }

    @Test
    void shouldNotFindByIdNonexistent() {
        assertThrows(EmptyResultDataAccessException.class, () -> repository.findById(999));
    }


}
