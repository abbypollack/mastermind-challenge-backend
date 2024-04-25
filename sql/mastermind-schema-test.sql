DROP DATABASE IF EXISTS mastermind_test;
CREATE DATABASE mastermind_test;
USE mastermind_test;

CREATE TABLE players (
    player_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    score INT NOT NULL DEFAULT 0
);

CREATE TABLE games (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    secret_code VARCHAR(10) NOT NULL,
    game_start_time DATETIME NOT NULL,
    game_end_time DATETIME,
    difficulty_level VARCHAR(50),
    winner_id INT NOT NULL,
    hint_count INT NOT NULL
);

CREATE TABLE guesses (
    guess_id INT AUTO_INCREMENT PRIMARY KEY,
    game_id INT NOT NULL,
    player_id INT NOT NULL,
    guess_sequence VARCHAR(10) NOT NULL,
    feedback VARCHAR(255) NOT NULL,
    guess_time DATETIME NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games(game_id),
    FOREIGN KEY (player_id) REFERENCES players(player_id)
);

CREATE TABLE game_players (
    game_id INT NOT NULL,
    player_id INT NOT NULL,
    PRIMARY KEY (game_id, player_id),
    FOREIGN KEY (game_id) REFERENCES games(game_id),
    FOREIGN KEY (player_id) REFERENCES players(player_id)
);

DELIMITER //

CREATE PROCEDURE set_known_good_state()
BEGIN
    SET SQL_SAFE_UPDATES = 0;

    DELETE FROM guesses;
    ALTER TABLE guesses AUTO_INCREMENT = 1;

    DELETE FROM game_players;
    DELETE FROM games;
    ALTER TABLE games AUTO_INCREMENT = 1;

    DELETE FROM players;
    ALTER TABLE players AUTO_INCREMENT = 1;

    INSERT INTO players (name, score) VALUES
    ('Player 1', 0),
    ('Player 2', 0),
    ('Player 3', 0),
    ('Player 4', 0);

    INSERT INTO games (secret_code, game_start_time, game_end_time, difficulty_level, winner_id, hint_count) VALUES
    ('0123', '2023-01-01 12:00:00', NULL, 'Easy', 0, 0),
    ('4567', '2023-01-02 12:00:00', NULL, 'Medium', 0, 0),
    ('89AB', '2023-01-03 12:00:00', NULL, 'Hard', 0, 0),
    ('2345', '2023-01-04 12:00:00', NULL, 'Very Hard', 0, 1);

    INSERT INTO game_players (game_id, player_id) VALUES
    (1, 1),
    (1, 2),
    (2, 3),
    (3, 4),
    (4, 1);

    INSERT INTO guesses (game_id, player_id, guess_sequence, feedback, guess_time) VALUES
    (2, 3, '5678', '2 correct numbers, 0 correct location', '2023-01-02 12:10:00'),
    (3, 4, '9876', '0 correct numbers', '2023-01-03 12:15:00'),
    (4, 1, '2345', '4 correct numbers and correct locations', '2023-01-04 12:20:00');

    SET SQL_SAFE_UPDATES = 1;
END //

DELIMITER ;

CALL set_known_good_state();