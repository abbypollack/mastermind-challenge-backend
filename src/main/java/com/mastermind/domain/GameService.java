package com.mastermind.domain;

import com.mastermind.data.GameJdbcRepository;
import com.mastermind.data.GamePlayerRepository;
import com.mastermind.data.GuessJdbcRepository;
import com.mastermind.models.Game;
import com.mastermind.models.Guess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class GameService {

    private static final int MAX_ATTEMPTS = 10;

    @Autowired
    private GameJdbcRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private GuessJdbcRepository guessRepository;

    @Autowired
    private RandomNumberService randomNumberService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private Validations validations;

    public Result<Game> startNewGame(List<Integer> playerIds, String difficultyLevel) {
        if (!validations.isValidDifficultyLevel(difficultyLevel) || playerIds == null || playerIds.isEmpty()) {
            Result<Game> result = new Result<>();
            result.addMessage("Invalid difficulty level or player IDs", ResultType.INVALID);
            return result;
        }

        Game game = new Game();
        game.setPlayerIds(playerIds);
        game.setSecretCode(randomNumberService.generateSecretCode());
        game.setDifficultyLevel(difficultyLevel.toLowerCase());
        game.setWinnerId(0);
        game.setHintCount(0);
        game.setGuesses(new ArrayList<>());
        game.setGameStartTime(LocalDateTime.now());

        Game createdGame = gameRepository.create(game);
        Result<Game> result = new Result<>();
        if (createdGame != null) {
            result.setPayload(createdGame);
            result.addMessage("Game created successfully.", ResultType.SUCCESS);
            for (Integer playerId : playerIds) {
                gamePlayerRepository.addPlayerToGame(createdGame.getGameId(), playerId);
            }
        } else {
            result.addMessage("Failed to start a new game.", ResultType.INVALID);
        }
        return result;
    }

    @Transactional
    public Result<Guess> makeGuess(int gameId, int playerId, String guessSequence) {
        if (!validations.isValidGameId(gameId) || !validations.isValidPlayerId(playerId) || !validations.isValidGuessSequence(guessSequence)) {
            Result<Guess> result = new Result<>();
            result.addMessage("Invalid game ID, player ID, or guess sequence.", ResultType.INVALID);
            return result;
        }

        Game game = gameRepository.findById(gameId);
        game.setGuesses(guessRepository.findByGameId(gameId));
        Result<Guess> result = new Result<>();
        if (game.getGameEndTime() != null) {
            result.addMessage("The game has already ended. No more guesses can be made.", ResultType.INVALID);
            return result;
        }

        if (game.getGuesses().size() >= MAX_ATTEMPTS) {
            game.setGameEndTime(LocalDateTime.now());
            gameRepository.update(game);
            result.addMessage("Maximum number of attempts reached. Game over.", ResultType.INVALID);
            return result;
        }

        Guess guess = new Guess();
        guess.setGameId(gameId);
        guess.setPlayerId(playerId);
        guess.setGuessSequence(guessSequence);
        guess.setGuessTime(LocalDateTime.now());

        Result<String> feedbackResult = calculateFeedback(game.getSecretCode(), guessSequence);
        if (!feedbackResult.isSuccess()) {
            for (String message : feedbackResult.getMessages()) {
                result.addMessage(message, ResultType.INVALID);
            }
            return result;
        }
        guess.setFeedback(feedbackResult.getPayload());
        Guess createdGuess = guessRepository.create(guess);
        game.getGuesses().add(createdGuess);

        gameRepository.update(game);
        result.setPayload(createdGuess);
        if (guess.getFeedback().equals("4 correct number(s) and 4 correct location(s)")) {
            game.setGameEndTime(LocalDateTime.now());
            game.setWinnerId(playerId);
            playerService.incrementScoreIfWon(playerId, true);
            gameRepository.update(game);
            result.addMessage("Congratulations! You've solved the puzzle.", ResultType.SUCCESS);
        }

        return result;
    }



    public Result<Boolean> stopTimer(int gameId) {
        Result<Boolean> result = new Result<>();

        if (!validations.isValidGameId(gameId)) {
            result.addMessage("Invalid game ID", ResultType.INVALID);
            return result;
        }

        Game game = gameRepository.findById(gameId);
        if (game == null) {
            result.addMessage("Game not found", ResultType.NOT_FOUND);
            return result;
        }

        if (game.getGameStartTime() == null) {
            result.addMessage("Cannot stop a timer for a game that hasn't started", ResultType.INVALID);
            return result;
        }

        if (game.getGameEndTime() != null) {
            result.addMessage("Cannot stop a timer for a game that has already ended", ResultType.INVALID);
            return result;
        }

        game.setGameEndTime(LocalDateTime.now());
        gameRepository.update(game);
        result.setPayload(true);
        result.addMessage("Timer stopped successfully", ResultType.SUCCESS);
        return result;
    }

    public Result<Game> updateSettings(int gameId, Map<String, String> settings) {
        if (!validations.isValidGameId(gameId)) {
            Result<Game> result = new Result<>();
            result.addMessage("Invalid game ID.", ResultType.NOT_FOUND);
            return result;
        }
        Game game = gameRepository.findById(gameId);
        Result<Game> result = new Result<>();
        if (game.getGameEndTime() != null) {
            result.addMessage("Cannot update settings for a game that has already ended", ResultType.INVALID);
            return result;
        }
        if (settings.containsKey("difficultyLevel")) {
            String difficultyLevel = settings.get("difficultyLevel").toLowerCase();
            if (!validations.isValidDifficultyLevel(difficultyLevel)) {
                result.addMessage("Invalid difficulty level", ResultType.INVALID);
                return result;
            }
            game.setDifficultyLevel(difficultyLevel);
        }
        Game updatedGame = gameRepository.update(game);
        if (updatedGame != null) {
            result.setPayload(updatedGame);
        } else {
            result.addMessage("Failed to update game settings.", ResultType.INVALID);
        }
        return result;
    }

    public Result<String> getHint(int gameId) {
        if (!validations.isValidGameId(gameId)) {
            Result<String> result = new Result<>();
            result.addMessage("Invalid game ID.", ResultType.NOT_FOUND);
            return result;
        }
        Game game = gameRepository.findById(gameId);
        Result<String> result = new Result<>();
        if (game != null) {
            String hint = null;
            String secretCode = game.getSecretCode();
            String difficultyLevel = game.getDifficultyLevel().toLowerCase();
            int hintPosition = game.getHintCount() % 4;

            switch (difficultyLevel) {
                case "easy":
                    hint = "The digit at position " + (hintPosition + 1) + " is " + secretCode.charAt(hintPosition);
                    break;
                case "medium":
                    hint = "The digit at position " + (hintPosition + 1) + " is " + (Character.getNumericValue(secretCode.charAt(hintPosition)) % 2 == 0 ? "even" : "odd");
                    break;
                case "hard":
                    int sum = 0;
                    for (char digit : secretCode.toCharArray()) {
                        sum += Character.getNumericValue(digit);
                    }
                    hint = "The sum of the digits is " + sum;
                    break;
            }

            if (hint != null) {
                game.setHintCount(game.getHintCount() + 1);
                gameRepository.update(game);
                result.setPayload(hint);
            } else {
                result.addMessage("No more hints available.", ResultType.INVALID);
            }
        } else {
            result.addMessage("Game does not exist.", ResultType.NOT_FOUND);
        }
        return result;
    }

    public Result<List<Guess>> getGameHistory(int gameId) {
        if (!validations.isValidGameId(gameId)) {
            Result<List<Guess>> result = new Result<>();
            result.addMessage("Invalid game ID.", ResultType.NOT_FOUND);
            return result;
        }
        List<Guess> guesses = guessRepository.findByGameId(gameId);
        Result<List<Guess>> result = new Result<>();
        if (!guesses.isEmpty()) {
            result.setPayload(guesses);
        } else {
            result.addMessage("No guesses found for this game.", ResultType.NOT_FOUND);
        }
        return result;
    }

    public Game findById(int gameId) {
        return gameRepository.findById(gameId);
    }

    public Result<String> calculateFeedback(String secretCode, String guess) {
        Result<String> result = new Result<>();

        if (!validations.isValidGuessSequence(secretCode) || !validations.isValidGuessSequence(guess)) {
            result.addMessage("Invalid secretCode or guess.", ResultType.INVALID);
            return result;
        }

        int correctPositions = 0;
        Map<Character, Integer> secretFrequency = new HashMap<>();
        Map<Character, Integer> guessFrequency = new HashMap<>();

        for (int i = 0; i < secretCode.length(); i++) {
            char secretChar = secretCode.charAt(i);
            char guessChar = guess.charAt(i);
            if (secretChar == guessChar) {
                correctPositions++;
            } else {
                secretFrequency.put(secretChar, secretFrequency.getOrDefault(secretChar, 0) + 1);
                guessFrequency.put(guessChar, guessFrequency.getOrDefault(guessChar, 0) + 1);
            }
        }

        int correctNumbers = 0;
        for (Map.Entry<Character, Integer> entry : guessFrequency.entrySet()) {
            Integer secretCount = secretFrequency.getOrDefault(entry.getKey(), 0);
            correctNumbers += Math.min(secretCount, entry.getValue());
        }
        correctNumbers += correctPositions;

        result.setPayload(String.format("%d correct number(s) and %d correct location(s)", correctNumbers, correctPositions));
        return result;
    }
}