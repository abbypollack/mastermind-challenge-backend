package com.mastermind.data;

import com.mastermind.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PlayerJdbcRepositoryTest {

    @Autowired
    private PlayerJdbcRepository repository;

    @Autowired
    private KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }
    @Test
    void findByIdExistingPlayer() {
        Player player = repository.findById(1);
        assertNotNull(player, "Should find player by ID.");
        assertEquals(1, player.getPlayerId(), "Player ID should match.");
    }

    @Test
    void findByIdNonExistingPlayer() {
        assertThrows(EmptyResultDataAccessException.class,
                () -> repository.findById(999), "Should throw exception for non-existent player ID.");
    }
    @Test
    void createPlayerSuccessfully() {
        Player newPlayer = new Player();
        newPlayer.setName("John Doe");
        newPlayer.setScore(100);

        Player createdPlayer = repository.create(newPlayer);
        assertNotNull(createdPlayer, "New player should be created successfully.");
        assertNotNull(createdPlayer.getPlayerId(), "New player should have a valid ID.");
    }

    @Test
    void createPlayerFailure() {
        Player invalidPlayer = new Player();
        invalidPlayer.setName(null);
        invalidPlayer.setScore(100);

        assertThrows(DataIntegrityViolationException.class,
                () -> repository.create(invalidPlayer), "Should fail to create player with invalid data.");
    }
    @Test
    void updateExistingPlayer() {
        Player player = repository.findById(1);
        player.setName("Jane Doe");
        player.setScore(150);

        Player updatedPlayer = repository.update(player);
        assertNotNull(updatedPlayer, "Should update player successfully.");
        assertEquals("Jane Doe", updatedPlayer.getName(), "Name should be updated.");
    }

    @Test
    void updateNonExistingPlayer() {
        Player player = new Player();
        player.setPlayerId(999);
        player.setName("Ghost");
        player.setScore(50);

        Player result = repository.update(player);
        assertNull(result, "Should not update non-existent player.");
    }

}
